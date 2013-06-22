import java.util.Hashtable;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.lang.Object;
import java.lang.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.text.DecimalFormat;
import java.io.FileNotFoundException;
import java.lang.Double;
import java.io.FileOutputStream;
import java.lang.String;
public class GetVector {
	private static BufferedImage BinarizedImage;     
	private static int DatabaseSize=610;
	private static int name=1;  
	private static Hashtable<String,Integer> LoopsInCharacter= new Hashtable<String,Integer>();
	private static PrintStream original = new PrintStream(System.out);                                       
	private static double[][] Vectors = new double[DatabaseSize][8];
	private static String[] Colors = new String[DatabaseSize];
//	private static 
	private static String[] Characters = new String[DatabaseSize];
	public static void main(String[] args) throws IOException {
		ReadDatabase(Vectors,Characters,Colors);
		DeclareHash.Declare(LoopsInCharacter); 
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		BinarizedImage = ImageIO.read(FilePointer);
		BinarizedImage = Binarization.GetBmp(BinarizedImage);      
		GetAllCharacter();     
		 
		                                            
	}
	public static int ReadDatabase(double[][] value,String[] c,String[] col)
	{
		int i=0,k=0;
		String str;			
		Scanner scan;
		File file = new File("database.txt");
		try {
			scan = new Scanner(file);
			while(scan.hasNext())
			{
				str = scan.nextLine();
				String[] parts = str.split("\t");
				c[i] = parts[0];
				col[i]=parts[1];
				for(k=0;k<8;k++)
					value[i][k] = Double.parseDouble(parts[k+2]);
				i++;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	
	public static int GetAllCharacter()
	{
		int Height=BinarizedImage.getHeight();
		int Width =BinarizedImage.getWidth();
		int[] BlackFreq=new int[Height];
		int[][] Visited= new int[Height][Width];
		for(int i=0; i<Height;i++)
			for(int j=0; j<Width;j++)
				Visited[i][j]=0;
		ImageLib.FrequencyOfBlack(BinarizedImage,BlackFreq);
		String[] Text=new String[100];
		for(int i=0 ; i<100; i++)
			Text[i]="";
		int LineNo=0;
		for(int i=0; i<Height; i++)
		{
			if(BlackFreq[i]!=0)
			{
				int MaxIndex=i;
				while(BlackFreq[i]!=0)
				{
					if(BlackFreq[i]>BlackFreq[MaxIndex])
						MaxIndex=i;
					i++;
					if(i>=Height)
						break;
				}
				//MaxIndex=(MaxIndex+i)/2;
				
				GetOneLine(BinarizedImage,Text[LineNo],MaxIndex,Visited);
				LineNo++;
			}
		}
		return 0;
	}
	public static int GetOneLine(BufferedImage Image,String CurrentLine,int Index,int[][] Visited)
	{
		
		
		int Height=Image.getHeight();
		int Width =Image.getWidth();		
		double[] t=new double[8];
		int[][] Coordinates=new int[2][2];
		int[] Difference=new int[100];
		int NoOfFrames=0,ArraySize=0;
		int XMinCurrent=0,XMaxLast=0;
		
		for(int j=0; j<Width; j++)
		{
			int Pixel=BinarizedImage.getRGB(j,Index);
			int Red=new Color(Pixel).getRed();
				
				
			if((Red==0) && (Visited[Index][j]==0))
			{
			
				Coordinates[0][0]=10000000;Coordinates[0][1]=-10000000;
				Coordinates[1][0]=10000000;Coordinates[1][1]=-10000000;
				int Error=ImageLib.GetFrame(Image,Coordinates,Visited,Index,j,0);
			
				if(Error==0)                                                  //No error in GetFrame
				{
					if(NoOfFrames==0)
						XMaxLast=Coordinates[0][1];
					
					if(NoOfFrames>0)
					{
						XMinCurrent=Coordinates[0][0];
						int Spacing=XMinCurrent-XMaxLast;
						Difference[ArraySize]=Spacing;
						ArraySize++;
						XMaxLast=Coordinates[0][1];
					}
										
					BufferedImage CharacterImage = new BufferedImage(Coordinates[0][1]-Coordinates[0][0]+1, Coordinates[1][1]-Coordinates[1][0]+1, BinarizedImage.getType());
					GetPureSubImage(CharacterImage,Index,j,Coordinates);
					ImageLib.vector(CharacterImage,t);
					char CurrentCharacter;
					CurrentCharacter=Characters[RecognizeCharacter(CharacterImage,t)].charAt(0);
					CurrentLine=CurrentLine+CurrentCharacter;
					writeImage(CharacterImage,Integer.toString(name));
					name++;
					NoOfFrames++;
				}
			}
		}
		int[] Spacing=new int[ArraySize];
		for(int i=0; i<ArraySize; i++)
			Spacing[i]=Difference[i];

		//for(int i=0; i<ArraySize; i++)
		//	original.print(Spacing[i]+" ");
		
		int threshold=Statistics.Threshold(Spacing);
		int index=1;
		for(int i=0; i<Spacing.length; i++)
		{
				if(Spacing[i]>threshold)
				{
					CurrentLine=CurrentLine.substring(0,index)+" "+CurrentLine.substring(index,CurrentLine.length());
					index++;
				}
				index++;
		}
		original.println(CurrentLine);
		
		return 0;
	}
	
	public static int GetPureSubImage(BufferedImage image,int Row,int Column,int[][] Coordinates)
	{
		for(int x=Coordinates[0][0]; x<=Coordinates[0][1]; x++)
		{
			for(int y=Coordinates[1][0]; y<=Coordinates[1][1]; y++)
			{
				int newpixel=BinarizedImage.getRGB(x,y);
				image.setRGB(x-Coordinates[0][0],y-Coordinates[1][0],newpixel);
			}
		}
		int[][] Visited=new int[image.getHeight()][image.getWidth()];
		for(int i=0; i<image.getHeight(); i++)
		{
			for(int j=0; j<image.getWidth(); j++)
				Visited[i][j]=0;
		}
		Row=Row-Coordinates[1][0];
		Column=Column-Coordinates[0][0];
		ImageLib.RemoveExtra(image,Visited,Row,Column);
		int WhitePixel;
		int Pixel=image.getRGB(Column,Row);
		int Alpha=new Color(Pixel).getAlpha();
		WhitePixel=ImageLib.colorToRGB(Alpha,255,255,255);
		for(int i=0; i<image.getHeight(); i++)
		{
			for(int j=0; j<image.getWidth(); j++)
			{
				if(Visited[i][j]==0)
				{
					image.setRGB(j,i,WhitePixel);
				}

			}
		}

		return 0;
	}
	
	
	public static int RecognizeCharacter(BufferedImage Image,double[] t)
	{
	
		try {
		PrintStream out = new PrintStream(new FileOutputStream(Integer.toString(name)+".txt"));
                System.setOut(out);
		      for(int i=0; i<8; i++)
                     System.out.print(t[i]+"\t");
              System.out.println();
		}
		catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                }


		
		int[] best5char=new int[5];
		double[] best5dev=new double[5];
		for(int x=0;x<5;x++)
		{
			best5dev[x]=1000000;
		}
		char CurrentChar;
		double min=1000000;
		int index=0;
		for(int i=0; i<DatabaseSize; i++)
		{
			
			int size=0;
			int flag=0;
			if(LoopsInCharacter.containsKey(Characters[i]))
			{
				if((LoopsInCharacter.get(Characters[i])!=CharFeatures.GetLoop(Image))&&(LoopsInCharacter.get(Characters[i])!=-1))
					continue;
			}
			for(int j=0; j<8; j++)
			{
				if((Vectors[i][j]!=0&&t[j]==0))
					flag=1;
				if(!(Vectors[i][j]==0 || t[j]==0))
					size++;
			}
			if(flag==1)
				continue;
			double[] Magnification = new double[size];
			int k=0;
			for(int j=0; j<8; j++)
			{
				if(!(Vectors[i][j]==0 || t[j]==0))
				{
					Magnification[k]=Vectors[i][j]/t[j];
					k++;
				}
			}
			
			double value;
			value = Statistics.getStdDev(Magnification);
			CurrentChar=Characters[i].charAt(0);
			{
				int max_index=1;
				int flag2=0;
				int x;
				for(x=0;x<5;x++)
				{
					if(CurrentChar==Characters[best5char[x]].charAt(0))
					{
						flag2=1;
						break;
					}
					if(best5dev[x]>best5dev[max_index])
						max_index=x;
				}
				if(flag2==1)
				{
					if(value<best5dev[x])
						best5dev[x]=value;
				
				}
				else if(value<best5dev[max_index])
				{
					best5dev[max_index]=value;
					best5char[max_index]=i;
				}
			}
                	//System.out.println(CurrentChar+"\t"+value);
			if(value<min)
			{
				index=i;
				min=value;			
		
			}

		}
		for(int x=0; x<5;x++)
		{
			System.out.println(Characters[best5char[x]].charAt(0)+"\t"+best5dev[x]);
		}
		CurrentChar=Characters[index].charAt(0);
                System.out.println("Matched With :  "+CurrentChar+"\t"+min);
		return index;
	}
	
	private static void writeImage(BufferedImage image,String output) {
		File file = new File(output+".bmp");
		try {
			ImageIO.write(image, "bmp", file);
		}catch(IOException e) {
			System.out.println("Not worked");
		}
		finally {
			//System.out.println("Works fine");
		}

	}
}


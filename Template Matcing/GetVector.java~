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
	private static BufferedImage binarized_image;     
	private static int database_size=52;
	private static int name=1;  
	private static PrintStream original = new PrintStream(System.out);                                       
	private static int[][][] database = new int[database_size][15][15];
	private static String[] Colors = new String[database_size];
	private static String[] characters = new String[database_size];
	public static void main(String[] args) throws IOException {
		ReadDatabase(database,characters,Colors);
	
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		binarized_image = ImageIO.read(FilePointer);
		binarized_image = Binarization.GetBmp(binarized_image);      
		GetAllCharacter();     
		 
		                                            
	}
	public static int ReadDatabase(int[][][] value,String[] c,String[] col)
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
				c[i] = str;
				for(k=0;k<15;k++)
				{
					str = scan.nextLine();
					for(int j=0;j<15;j++)
						value[i][k][j] = str.charAt(j)-'0';
					
				}
				str = scan.nextLine();	
				i++;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	
	public static int GetAllCharacter()
	{
		int Height=binarized_image.getHeight();
		int Width =binarized_image.getWidth();
		int[] BlackFreq=new int[Height];
		int[][] Visited= new int[Height][Width];
		for(int i=0; i<Height;i++)
			for(int j=0; j<Width;j++)
				Visited[i][j]=0;
		ImageLib.FrequencyOfBlack(binarized_image,BlackFreq);
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
				
				GetOneLine(binarized_image,Text[LineNo],MaxIndex,Visited);
				LineNo++;
			}
		}
		return 0;
	}
	public static int GetOneLine(BufferedImage Image,String CurrentLine,int Index,int[][] Visited)
	{
		
		
		int Height=Image.getHeight();
		int Width =Image.getWidth();		
		int[][] Coordinates=new int[2][2];
		int[] Difference=new int[100];
		int NoOfFrames=0,ArraySize=0;
		int XMinCurrent=0,XMaxLast=0;
		
		for(int j=0; j<Width; j++)
		{
			int Pixel=binarized_image.getRGB(j,Index);
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
										
					BufferedImage character_image = new BufferedImage(Coordinates[0][1]-Coordinates[0][0]+1, Coordinates[1][1]-Coordinates[1][0]+1, binarized_image.getType());
					GetPureSubImage(character_image,Index,j,Coordinates);

					char CurrentCharacter;
					
					CurrentCharacter=characters[CharFeatures.recognize(character_image,database,characters,database_size,name)].charAt(0);
					CurrentLine=CurrentLine+CurrentCharacter;
					writeImage(character_image,Integer.toString(name));
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
				int newpixel=binarized_image.getRGB(x,y);
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


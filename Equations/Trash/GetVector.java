import java.util.Hashtable;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.lang.Object;
import java.lang.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private static int database_size=124;
	private static int name=1;  
	private static Map<String, Integer> dictionary = new HashMap<String, Integer>();
	private static PrintStream original = new PrintStream(System.out);                                       
	private static int[][][] database = new int[database_size][15][15];
	private static String[] Colors = new String[database_size];
	private static String[] characters = new String[database_size];
	public static void main(String[] args) throws IOException {
		ReadDatabase(database,characters,Colors);
		dictionary_reader();
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		binarized_image = ImageIO.read(FilePointer);
		binarized_image = Binarization.GetBmp(binarized_image);      
		GetAllCharacter();     


	}
	public static  void dictionary_reader()
	{
		String str;			
		Scanner scan;
		File file = new File("dictionary.txt");
		try {
			scan = new Scanner(file);
			while(scan.hasNext())
			{
				str = scan.nextLine();
				dictionary.put(str, 1);
			}
		}
		catch (FileNotFoundException e1) {

			e1.printStackTrace();

		}
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
				int upper_limit=i;
				while(BlackFreq[i]!=0)
				{
					if(BlackFreq[i]>BlackFreq[MaxIndex])
						MaxIndex=i;
					i++;
					if(i>=Height)
						break;
				}
				//MaxIndex=(MaxIndex+i)/2;

				Text[LineNo]=GetOneLine(binarized_image,Text[LineNo],MaxIndex,Visited,upper_limit);
				LineNo++;
			}
		}
		for(int i=0; i<LineNo; i++)
		{
			String current_line=Text[i];
			String[] parts=current_line.split(" ");
			for(int j=0; j<parts.length; j++)
			{
				original.print(SpellingCorrector.getCorrect(parts[j].toLowerCase(),dictionary)+" ");
			}
			original.println();
		}
		return 0;
	}
	public static int find_height(BufferedImage Image,int[][] Coordinates,int high)
	{
		int min=high;
		for(int i=Coordinates[0][0];i<=Coordinates[0][1];i++)
		{
			int j;
			for(j=high;j<Coordinates[1][0];j++)
			{
				if(new Color(Image.getRGB(i, j)).getRed() == 0)
					break;
			}
			if(j > min)
				min=j;
		}
		return Coordinates[1][0]-min;
	}
	public static BufferedImage add_extra(BufferedImage Image,int[][] coordinates,int extra_height)
	{
		int height=coordinates[1][1]-coordinates[1][0]+1+extra_height;
		int width=coordinates[0][1]-coordinates[0][0]+1;
		BufferedImage character_image = new BufferedImage(width, height, binarized_image.getType());

		for(int i=0; i<extra_height; i++)
		{
			for(int j=0; j<width; j++)
			{
				int newpixel=binarized_image.getRGB(coordinates[0][0]+j,coordinates[1][0]-extra_height+i);
				character_image.setRGB(j,i,newpixel);	
			}
		}
		for(int i=extra_height; i<height; i++)
		{
			for(int j=0; j<width; j++)
			{
				int newpixel=Image.getRGB(j,-extra_height+i);
				character_image.setRGB(j,i,newpixel);	
			}
		}

		return character_image;
	}
	public static String GetOneLine(BufferedImage Image,String CurrentLine,int Index,int[][] Visited,int upper_limit)
	{


		int Height=Image.getHeight();
		int Width =Image.getWidth();		
		int[][] coordinates=new int[2][2];
		int[] Difference=new int[100];
		int NoOfFrames=0,ArraySize=0;
		int XMinCurrent=0,XMaxLast=0;

		for(int j=0; j<Width; j++)
		{
			int Pixel=binarized_image.getRGB(j,Index);
			int Red=new Color(Pixel).getRed();


			if((Red==0) && (Visited[Index][j]==0))
			{

				coordinates[0][0]=10000000;coordinates[0][1]=-10000000;
				coordinates[1][0]=10000000;coordinates[1][1]=-10000000;
				int Error=ImageLib.GetFrame(Image,coordinates,Visited,Index,j,0);

				int extra_height=find_height(Image,coordinates,upper_limit);
				

				if(Error==0)                                                  //No error in GetFrame
				{
					if(NoOfFrames==0)
						XMaxLast=coordinates[0][1];

					if(NoOfFrames>0)
					{
						XMinCurrent=coordinates[0][0];
						int Spacing=XMinCurrent-XMaxLast;
						Difference[ArraySize]=Spacing;
						ArraySize++;
						XMaxLast=coordinates[0][1];
					}

					BufferedImage character_image = new BufferedImage(coordinates[0][1]-coordinates[0][0]+1, coordinates[1][1]-coordinates[1][0]+1, binarized_image.getType());
					GetPureSubImage(character_image,Index,j,coordinates);

					character_image=add_extra(character_image,coordinates,extra_height);

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
		//original.println(CurrentLine);
		return CurrentLine;

	}

	public static int GetPureSubImage(BufferedImage image,int Row,int Column,int[][] coordinates)
	{
		for(int x=coordinates[0][0]; x<=coordinates[0][1]; x++)
		{
			for(int y=coordinates[1][0]; y<=coordinates[1][1]; y++)
			{
				int newpixel=binarized_image.getRGB(x,y);
				image.setRGB(x-coordinates[0][0],y-coordinates[1][0],newpixel);
			}
		}
		int[][] Visited=new int[image.getHeight()][image.getWidth()];
		for(int i=0; i<image.getHeight(); i++)
		{
			for(int j=0; j<image.getWidth(); j++)
				Visited[i][j]=0;
		}
		Row=Row-coordinates[1][0];
		Column=Column-coordinates[0][0];
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


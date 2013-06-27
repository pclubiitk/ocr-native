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
	private static int database_size=63;
	private static String database_file = "database.txt";
	private static String special_database_file = "special_database.txt";	
	private static int special_database_size=4;	

	private static int name=1;  
	private static Map<String, Integer> dictionary = new HashMap<String, Integer>();
	private static PrintStream original = new PrintStream(System.out);                                       
	private static int[][][] database = new int[database_size][15][15];

	private static int[][][] special_database = new int[special_database_size][15][15];

	private static String[] characters = new String[database_size];

	private static String[] special_characters = new String[special_database_size];

	public static void main(String[] args) throws IOException {
		ReadDatabase(database,characters,database_file);
		
		ReadDatabase(special_database,special_characters,special_database_file);

		//test

		for(int i=0 ; i<special_database_size; i++)
			//System.out.println(special_characters[i]);
//

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
	public static int ReadDatabase(int[][][] value,String[] c,String file_name)
	{
		int i=0,k=0;
		String str;			
		Scanner scan;
		File file = new File(file_name);
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
		int low=0,high=0;

		String[] Text=new String[100];
		String[] special_text=new String[100];
		int[][][] Text_coordinates = new int[100][2][2];
		int[][][] special_Text_coordinates = new int[100][2][2];

		int[] BlackFreq=new int[Height];
		int[][] Visited= new int[Height][Width];
		for(int i=0; i<Height;i++)
			for(int j=0; j<Width;j++)
				Visited[i][j]=0;
		ImageLib.FrequencyOfBlack(binarized_image,BlackFreq);
		
		for(int i=0 ; i<100; i++)
		{
			Text[i]="";special_text[i]="";

		}
		int LineNo=0;
		for(int i=0; i<Height; i++)
		{
			if(BlackFreq[i]!=0)
			{
				int MaxIndex=i;
				low=i;
				int upper_limit=i;
				while(BlackFreq[i]!=0)
				{
					if(BlackFreq[i]>BlackFreq[MaxIndex])
						MaxIndex=i;
					i++;
					if(i>=Height)
						break;
				}
				high=i-1;
				//MaxIndex=(MaxIndex+i)/2;

				Text[LineNo]=GetOneLine(binarized_image,MaxIndex,Visited,upper_limit);
				original.println(Text[LineNo]);	
				String special_char="";
				special_char=GetOneLine_sp(binarized_image,low,high,Visited);
				original.println(special_char);
				LineNo++;
			}
		}
		for(int i=0; i<0; i++)
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
	public static int find_height(BufferedImage Image,int[][] Coordinates,int high,int[][] Visited)
	{
		int min=high;
		for(int i=Coordinates[0][0];i<=Coordinates[0][1];i++)
		{
			int j;
			for(j=high;j<Coordinates[1][0];j++)
			{
				if(new Color(Image.getRGB(i, j)).getRed() == 0)
				{
					int[][] trash=new int[2][2];
					ImageLib.GetFrame(Image,trash,Visited,i,j);
					break;
				}
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
	public static String GetOneLine(BufferedImage Image,int Index,int[][] Visited,int upper_limit)
	{

		String current_line="";
		int Height=Image.getHeight();
		int Width =Image.getWidth();		
		int[][] coordinates=new int[2][2];

		for(int j=0; j<Width; j++)
		{
			int Pixel=binarized_image.getRGB(j,Index);
			int Red=new Color(Pixel).getRed();

			if((Red==0) && (Visited[Index][j]==0))
			{

				coordinates[0][0]=10000000;coordinates[0][1]=-10000000;
				coordinates[1][0]=10000000;coordinates[1][1]=-10000000;
				ImageLib.GetFrame(Image,coordinates,Visited,Index,j);

				int extra_height=find_height(Image,coordinates,upper_limit,Visited);


				BufferedImage character_image = new BufferedImage(coordinates[0][1]-coordinates[0][0]+1, coordinates[1][1]-
						coordinates[1][0]+1, binarized_image.getType());

				GetPureSubImage(character_image,Index,j,coordinates);

				character_image=add_extra(character_image,coordinates,extra_height);

				char CurrentCharacter;

				CurrentCharacter=characters[CharFeatures.recognize(character_image,database,characters,database_size,name)].charAt(0);

				current_line=current_line+CurrentCharacter;
				writeImage(character_image,Integer.toString(name));
				name++;
			}
		}
		return current_line;
	}
	public static String GetOneLine_sp(BufferedImage Image,int low,int high,int[][] Visited)
	{
		String current_line="";
		int Height=Image.getHeight();
		int Width =Image.getWidth();		
		int[][] coordinates=new int[2][2];
		for(int i=low; i<=high; i++) {
		for(int j=0; j<Width; j++) {
			int Pixel=binarized_image.getRGB(j,i);
			int Red=new Color(Pixel).getRed();

			if((Red==0) && (Visited[i][j]==0))
			{

				coordinates[0][0]=10000000;coordinates[0][1]=-10000000;
				coordinates[1][0]=10000000;coordinates[1][1]=-10000000;
				ImageLib.GetFrame(Image,coordinates,Visited,i,j);
				
				BufferedImage character_image = new BufferedImage(coordinates[0][1]-coordinates[0][0]+1, coordinates[1][1]-
						coordinates[1][0]+1, binarized_image.getType());

				GetPureSubImage(character_image,i,j,coordinates);

				char CurrentCharacter;

				CurrentCharacter=special_characters[CharFeatures.recognize(character_image,special_database,special_characters,special_database_size,name)].charAt(0);

				current_line=current_line+CurrentCharacter;
				writeImage(character_image,Integer.toString(name));
				name++;
			}
		}
		}
		return current_line;
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


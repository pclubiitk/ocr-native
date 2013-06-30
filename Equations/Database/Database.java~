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
public class Database {
	private static BufferedImage binarized_image;     
	private static int name=1;  
	private static PrintStream original = new PrintStream(System.out);                                       
	public static void main(String[] args) throws IOException {
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		binarized_image = ImageIO.read(FilePointer);
		binarized_image = Binarization.GetBmp(binarized_image);      
		GetAllCharacter();                                                       
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
				
				GetOneLine(binarized_image,MaxIndex,Visited,upper_limit);
				LineNo++;
			}
		}
		return 0;
	}
	public static int find_height(BufferedImage Image,int[][] coordinates,int high)
	{
		int min=coordinates[1][0];
		for(int i=coordinates[0][0];i<=coordinates[0][1];i++)
		{
			int j;
			for(j=high;j<coordinates[1][0];j++)
			{
				if(new Color(Image.getRGB(i, j)).getRed() == 0)
					break;
			}
			if(j < min)
				min=j;
		}
		return coordinates[1][0]-min;
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
	public static int GetOneLine(BufferedImage Image,int Index,int[][] Visited,int upper_limit)
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
					
					character_image=ImageLib.resize(character_image);
					writeImage(character_image,Integer.toString(name));

		String charac="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		System.out.print(charac.charAt(name-1)+"\n");
		
		for(int p=0;p<15;p++)
		{
			for(int q=0;q<15;q++)	
			{		
				if((new Color(character_image.getRGB(q,p)).getRed())==0)				
					System.out.print("0");
				else
					System.out.print("1");

			}
			System.out.println();
		}
		System.out.println();
		
					name++;
					NoOfFrames++;
				}
			}
		}
		
		return 0;
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


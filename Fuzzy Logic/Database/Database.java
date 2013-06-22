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
	private static BufferedImage BinarizedImage;     
	private static int name=1;  
	private static PrintStream original = new PrintStream(System.out);                                       
	public static void main(String[] args) throws IOException {
		String FileName = args[0];
		File FilePointer = new File(FileName);                                     
		BinarizedImage = ImageIO.read(FilePointer);
		BinarizedImage = Binarization.GetBmp(BinarizedImage);      
		GetAllCharacter();                                                       
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
				
				GetOneLine(BinarizedImage,MaxIndex,Visited);
				LineNo++;
			}
		}
		return 0;
	}
	public static int GetOneLine(BufferedImage Image,int Index,int[][] Visited)
	{
		
		
		int Height=Image.getHeight();
		int Width =Image.getWidth();		
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
										
					BufferedImage ChracterImage = new BufferedImage(Coordinates[0][1]-Coordinates[0][0]+1, Coordinates[1][1]-Coordinates[1][0]+1, BinarizedImage.getType());
					GetPureSubImage(ChracterImage,Index,j,Coordinates);
					ImageLib.vector(ChracterImage,name);
					writeImage(ChracterImage,Integer.toString(name));
					name++;
					NoOfFrames++;
				}
			}
		}
		
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


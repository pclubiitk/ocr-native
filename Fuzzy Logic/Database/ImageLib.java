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
public class ImageLib {

public static int FrequencyOfBlack(BufferedImage image,int[] Black)
	{
		int Height=image.getHeight();
		int Width=image.getWidth();
		for(int i=0; i<Height; i++)
		{
			Black[i]=0;
			for(int j=0; j<Width; j++)
			{
				int Pixel= image.getRGB(j,i);
				int Red= new Color(Pixel).getRed();
				if(Red==0)	
					Black[i]+=1;
			}
		}
		return 1;
	}

public static int GetFrame(BufferedImage Image,int[][] Coordinates,int[][] Visited,int Row,int Column,int Error)
	{
	
		if(Error==1)
			return 1;
		try {
		if(Row<0||Row>=Image.getHeight())
			return Error;
		if(Column<0||Column>=Image.getWidth())
			return Error;
		int Pixel=Image.getRGB(Column,Row);
		int Red=new Color(Pixel).getRed();
		if(Red==255 || Visited[Row][Column]==1)
		{
			return Error;
		}
		else
		{
			Visited[Row][Column]=1;
			if(Row<Coordinates[1][0])
				Coordinates[1][0]=Row;
			if(Row>Coordinates[1][1])
				Coordinates[1][1]=Row;
			if(Column<Coordinates[0][0])
				Coordinates[0][0]=Column;
			if(Column>Coordinates[0][1])
				Coordinates[0][1]=Column;
			try {
			Error=GetFrame(Image,Coordinates,Visited,Row+1,Column+1,Error);
			Error=GetFrame(Image,Coordinates,Visited,Row,Column+1,Error);
			Error=GetFrame(Image,Coordinates,Visited,Row-1,Column+1,Error);
			Error=GetFrame(Image,Coordinates,Visited,Row+1,Column,Error);
			Error=GetFrame(Image,Coordinates,Visited,Row-1,Column,Error);
			Error=GetFrame(Image,Coordinates,Visited,Row+1,Column-1,Error);
			Error=GetFrame(Image,Coordinates,Visited,Row,Column-1,Error);
			Error=GetFrame(Image,Coordinates,Visited,Row-1,Column-1,Error);
			}
			catch(Exception e) {
				e.printStackTrace();
				
				Error=1;
				
			}
		}
		}
		catch (Exception e)
		{
			Error=1;
			
		}
		return Error;
	}


	public static int vector(BufferedImage image,int name){

		int width = image.getWidth();
		double[] t=new double[16];
		int height = image.getHeight();
		int size = t.length;
		int [][] v = new int[size][2];
		int i,j,pixel;
		int Xs=0,Ys=0,count=0;;

		for(i=0;i<width;i++) {
			for(j=0;j<height;j++)
			{
				pixel = new Color(image.getRGB(i, j)).getRed();
				if(pixel == 0)
				{
					Xs = Xs + i;
					Ys = Ys + j;
					count = count + 1;
				}
			}
		}
		Xs = Xs/count;
		Ys = Ys/count;
		for(i=0;i<16;i++)
		{
			v[i][0]=Xs;
			v[i][1]=Ys;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && i>v[0][0] )
				v[0][0] = i;
			i++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && i<v[4][0] )
				v[4][0] = i;
			i--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j<v[2][1] )
				v[2][1] = j;
			j--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j>v[6][1] )
				v[6][1] = j;
			j++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j>v[7][1] )
			{
				v[7][0] = i;
				v[7][1] = j;
			}
			i++;
			j++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j<v[1][1] )
			{
				v[1][0] = i;
				v[1][1] = j;
			}
			i++;
			j--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
			if((new Color(image.getRGB(i, j)).getRed()==0) && j<v[3][1] )
			{
				v[3][0] = i;
				v[3][1] = j;
			}
			i--;
			j--;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[5][1] )
			{
				v[5][0] = i;
				v[5][1] = j;
			}
			i--;
			j++;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j<v[8][1] )
			{
				v[8][0] = i;
				v[8][1] = j;
			}
			i=i+2;
			j=j-1;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j<v[9][1] )
			{
				v[9][0] = i;
				v[9][1] = j;
			}
			i=i+1;
			j=j-2;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j<v[10][1] )
			{
				v[10][0] = i;
				v[10][1] = j;
			}
			i=i-1;
			j=j-2;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j<v[11][1] )
			{
				v[11][0] = i;
				v[11][1] = j;
			}
			i=i-2;
			j=j-1;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[12][1] )
			{
				v[12][0] = i;
				v[12][1] = j;
			}
			i=i-2;
			j=j+1;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[13][1] )
			{
				v[13][0] = i;
				v[13][1] = j;
			}
			i=i-1;
			j=j+2;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[14][1] )
			{
				v[14][0] = i;
				v[14][1] = j;
			}
			i=i+1;
			j=j+2;
		}
		i=Xs;j=Ys;
		while(i < width && i>=0 && j < height && j>=0)
		{
		
			if((new Color((int)image.getRGB(i, j)).getRed()==0) && j>v[15][1] )
			{
				v[15][0] = i;
				v[15][1] = j;
			}
			i=i+2;
			j=j+1;
		}
		for(i=0;i<16;i++){
			t[i] = Math.pow((v[i][0]-Xs),2) + Math.pow((v[i][1]-Ys),2);
			t[i] = Math.sqrt(t[i]);
		}
		String charac="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$^&*()-+\\{}[]?/<>.....................";
		if(name>=3&& name<=64)
		{
		System.out.print(charac.charAt(name-3)+"\t");
		if((new Color(image.getRGB(Xs,Ys)).getRed())==0)
			System.out.print("B"+"\t");
		else
			System.out.print("W"+"\t");
		for(i=0;i<8;i++)
			System.out.print(t[i]+"\t");
		System.out.println();
		}
		return 1;
	}
	public static int colorToRGB(int alpha, int red, int green, int blue) {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red; newPixel = newPixel << 8;
		newPixel += green; newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;

	}
	public static int RemoveExtra(BufferedImage image,int[][] Visited,int Row,int Column)
	{
		if(Row<0||Row>=image.getHeight())
			return 0;
		if(Column<0||Column>=image.getWidth())
			return 0;
		int Pixel=image.getRGB(Column,Row);
		int Red=new Color(Pixel).getRed();
		if(Red==255 || Visited[Row][Column]==1)
		{
			return 0;
		}
		else
		{
			Visited[Row][Column]=1;
			RemoveExtra(image,Visited,Row+1,Column+1);
			RemoveExtra(image,Visited,Row,Column+1);
			RemoveExtra(image,Visited,Row-1,Column+1);
			RemoveExtra(image,Visited,Row+1,Column);
			RemoveExtra(image,Visited,Row-1,Column);
			RemoveExtra(image,Visited,Row+1,Column-1);
			RemoveExtra(image,Visited,Row,Column-1);
			RemoveExtra(image,Visited,Row-1,Column-1);
		}
		return 0;
	}

	
}

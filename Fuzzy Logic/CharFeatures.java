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
public class CharFeatures {

public static int Get_loop(BufferedImage Image,int[][] Visited,int Row,int Column)
	{
		
		if(Row<0||Row>=Image.getHeight())
		{
						
			return 0;
		}
		if(Column<0||Column>=Image.getWidth())
			return 0;
		int Pixel=Image.getRGB(Column,Row);
		int Red=new Color(Pixel).getRed();
		if(Red==0 || Visited[Row][Column]==1)
			return 0;
		else
		{
			Visited[Row][Column]=1;
			Get_loop(Image,Visited,Row+1,Column+1);
			Get_loop(Image,Visited,Row,Column+1);
			Get_loop(Image,Visited,Row-1,Column+1);
			Get_loop(Image,Visited,Row+1,Column);
			Get_loop(Image,Visited,Row-1,Column);
			Get_loop(Image,Visited,Row+1,Column-1);
			Get_loop(Image,Visited,Row,Column-1);
			Get_loop(Image,Visited,Row-1,Column-1);
		}
	
		return 1;
	}
	public static int GetLoop(BufferedImage Image)
	{
		int Height=Image.getHeight();
		int Width=Image.getWidth();
		int answer=0;
		int[][] Visited=new int[Height][Width];
		for(int i=0; i<Height; i++)
			for(int j=0;j<Width; j++)
				Visited[i][j]=0;
		for(int i=0; i<1; i++)
		{
			for(int j=0;j<Width; j++)
			{
				if(Visited[i][j]==0&&(new Color(Image.getRGB(j,i)).getRed()==255))
					Get_loop(Image,Visited,i,j);
			
			}
		}
		for(int i=Height-1; i<Height; i++)
		{
			for(int j=0;j<Width; j++)
			{
				if(Visited[i][j]==0&&(new Color(Image.getRGB(j,i)).getRed()==255))
					Get_loop(Image,Visited,i,j);
			
			}
		}
		for(int j=0;j<1; j++)
		{
			for(int i=0; i<Height; i++)
			{
				if(Visited[i][j]==0&&(new Color(Image.getRGB(j,i)).getRed()==255))
					Get_loop(Image,Visited,i,j);
			
			}
		}
		for(int j=Width-1;j<Width; j++)
		{
			for(int i=0; i<Height; i++)
			{
				if(Visited[i][j]==0&&(new Color(Image.getRGB(j,i)).getRed()==255))
					Get_loop(Image,Visited,i,j);
			
			}
		}
		for(int i=0; i<Height; i++)
		{
			for(int j=0;j<Width; j++)
			{
				if(Visited[i][j]==0&&(new Color(Image.getRGB(j,i)).getRed()==255))
					answer+=Get_loop(Image,Visited,i,j);
			
			}
		}
		return answer;
	}


}

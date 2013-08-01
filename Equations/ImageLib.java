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
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
public class ImageLib {
	private static int IMG_WIDTH = 15;
	private static int IMG_HEIGHT = 15;

	public static void FrequencyOfBlack(BufferedImage image,int[] Black)
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
					Black[i]++;
			}
		}
	}
	public static void FrequencyOfBlack_vertical(BufferedImage image,int low,int high,int[] Black_vertical)
	{
		int k,l,count;
		for(k=0;k<image.getWidth();k++)
		{
			Black_vertical[k]=0;
			for(l=low;l<=high;l++)
			{
				int Pixel= image.getRGB(k,l);
				int Red= new Color(Pixel).getRed();
				if(Red==0)	
					Black_vertical[k]++;
			}
		}
	
	}
	public static boolean allblack(BufferedImage image,int[][] coordinates)	
	{
		int k,l,count;
		for(k=coordinates[0][0];k<coordinates[0][1];k++)
		{
			for(l=coordinates[1][0];l<=coordinates[1][1];l++)
			{
				int Pixel= image.getRGB(k,l);
				int Red= new Color(Pixel).getRed();
				if(Red!=0)	
					return false;
			}
		}
		return true;
	}

	public static void GetFrame(BufferedImage Image,int[][] Coordinates,int low,int high)
	{
		int i,j,count;
		for(i=low;i<=high;i++)
		{
			count=0;
			for(j=Coordinates[0][0];j<=Coordinates[0][1];j++)
			{
				int Pixel= Image.getRGB(j,i);
				int Red= new Color(Pixel).getRed();
				if(Red == 0)
					count++;
			}
			if(count > 0)
			 break;
		}
		Coordinates[1][0] = i;
		for(i=high;i>=low;i--)
		{
			count=0;
			for(j=Coordinates[0][0];j<=Coordinates[0][1];j++)
			{
				int Pixel= Image.getRGB(j,i);
				int Red= new Color(Pixel).getRed();
				if(Red == 0)
					count++;
			}
			if(count > 0)
			 break;
		}
		Coordinates[1][1] = i;		
/*
		if(Row<0||Row>=Image.getHeight())
			return;
		if(Column<0||Column>=Image.getWidth())
			return;
		int Pixel=Image.getRGB(Column,Row);
		int Red=new Color(Pixel).getRed();
		if(Red==255 || Visited[Row][Column]==1)
			return;
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
			GetFrame(Image,Coordinates,Visited,Row+1,Column+1);
			GetFrame(Image,Coordinates,Visited,Row,Column+1);
			GetFrame(Image,Coordinates,Visited,Row-1,Column+1);
			GetFrame(Image,Coordinates,Visited,Row+1,Column);
			GetFrame(Image,Coordinates,Visited,Row-1,Column);
			GetFrame(Image,Coordinates,Visited,Row+1,Column-1);
			GetFrame(Image,Coordinates,Visited,Row,Column-1);
			GetFrame(Image,Coordinates,Visited,Row-1,Column-1);
		}
*/
	}

	public static BufferedImage resize(BufferedImage originalImage) {

		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizeImage = resizeImage(originalImage, type);
		return resizeImage;


	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type){
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}

	private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){

		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
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

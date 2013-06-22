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
	public static int absolute(int i)
	{
		if(i<0)
			return -i;
		else
			return i;
	}
	public static int recognize(BufferedImage Image,int[][][] database,String[] Characters,int DatabaseSize)
	{
		ImageLib.resize(Image);
		int[][] Array=new int[12][12];	
		int[]  best5char  =new int[5];
		double[] best5dev=new double[5];
		int value=0;
		for(int x=0;x<5;x++)
			best5dev[x]=1000000;
		char CurrentChar;
		double min=1000000;
		int index=0;
		for(int i=0; i<Image.getHeight(); i++)
			for(int j=0; j<Image.getWidth(); j++)
				Array[i][j]=new Color(Image.getRGB(j,i)).getRed();
		for(int p=0; p<DatabaseSize; p++)
		{
			
			int size=0;
			int flag=0;
			for(int i=0; i<15; i++)
				for(int j=0;j<15;j++)
					value=absolute(Array[i][j]-database[p][i][j]);
			if(value<min)
			{
				index=p;
				min=value;			
		
			}

		}
		for(int x=0; x<5;x++)
			System.out.println(Characters[best5char[x]].charAt(0)+"\t"+best5dev[x]);
		CurrentChar=Characters[index].charAt(0);
                System.out.println("Matched With :  "+CurrentChar+"\t"+min);
		return index;
	}
}

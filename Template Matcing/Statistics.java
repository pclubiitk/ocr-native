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
public class Statistics {
public static int Threshold(int[] space){

	int i,j,sum=0;

	for(i=0;i<space.length;i++)
	{
		sum+=space[i]*space[i];
	}
	if(space.length!=0)
		return (int)Math.sqrt(sum/space.length);
	else
		return -1;		
}
public static double getMean(double[] data)
	{
		double size = data.length;
		double sum = 0.0;
		for(double a : data)
			sum += a;
		return sum/size;
	}

	public static double getVariance(double[] data)
	{
		double mean = getMean(data);
		double temp = 0;
		double size = data.length;
		for(int i=0; i<size; i++)
			temp += (mean-data[i])*(mean-data[i]);
		return temp/size;
	}

	public static double getStdDev(double[] data)
	{
		return Math.sqrt(getVariance(data));
	}
}

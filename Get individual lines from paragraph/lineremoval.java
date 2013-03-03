import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Object;
import java.lang.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
public class lineremoval {
	private static BufferedImage binarizedImage;                                              //input image
	public static void main(String[] args) throws IOException {

		File orignal_name = new File("photot.png");                                       //Reading file
		binarizedImage = ImageIO.read(orignal_name);                                      //reading image in buffered Image variable
		Get_all_individual_lines();                                                       //calling function
	}
	public static int FindBottomOfLine(BufferedImage input_image, int topOfLine)
	{
		int x=0;
		boolean no_black_pixel;
		no_black_pixel = false;
		int to_match;
		while (no_black_pixel == false)
		{
			topOfLine++;
			int white=new Color(input_image.getRGB(0,0)).getRed();
			no_black_pixel = true; 
			for (x = 0; x < input_image.getWidth() && topOfLine < input_image.getHeight(); x++)
			{
				to_match = new Color(input_image.getRGB(x,topOfLine)).getRed();
				if (to_match!=white)
					no_black_pixel = false;
			}
		}
		return topOfLine - 1;
	}
	public static int  Get_all_individual_lines()
	{
		int y = 0;
		int x = 0;
		boolean line_present = true;
		ArrayList<Integer> line_top = new ArrayList<Integer>(1000);
		ArrayList<Integer> line_bottom = new ArrayList<Integer>(1000);
		while (line_present)
		{
			x = 0;
			y = FindNextLine(binarizedImage, y, x);
			if (y == -1)
				break;
			if (y >= binarizedImage.getHeight())
			{
				line_present = false;
			}
			if (line_present)
			{
				line_top.add(y);
				y = FindBottomOfLine(binarizedImage, y) + 1;
				line_bottom.add(y);
			}
		}
		for (int line_number = 0; line_number < line_top.size(); line_number++)
                {
                        int height = line_bottom.get(line_number) - line_top.get(line_number) + 1;
                        BufferedImage bmp = new BufferedImage(binarizedImage.getWidth(), height + 2,binarizedImage.getType());
                        for(int i=0; i<bmp.getWidth(); i++) {
                                for(int j=0; j<bmp.getHeight(); j++) {
                                        int red;
					int column = line_top.get(line_number)+j;
                                        red =new Color(binarizedImage.getRGB(i,column)).getRed();
                                        int alpha = new Color(binarizedImage.getRGB(i,column)).getAlpha();
                                        int newPixel;
                                        newPixel = colorToRGB(alpha,red,red,red);
                                        bmp.setRGB(i,j,newPixel);

                                }
                        }

                        writeImage(bmp,line_number);
                }

		return 1;
	}
	private static void writeImage(BufferedImage bmp,int number)  {
		String strI = Integer.toString(number);	
		File file = new File("output"+strI+".png");
		try {
			ImageIO.write(bmp, "png", file);
		}catch(IOException e) {
			System.out.println("Not worked");
		} 
		finally {
			System.out.println("Works fine");
		}
	}
	private static int colorToRGB(int alpha, int red, int green, int blue) {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red; newPixel = newPixel << 8;
		newPixel += green; newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;

	}
	public static int FindNextLine(BufferedImage input_image, int y,int x)
	{
		if (y >= input_image.getHeight())
			return -1;
		int white=new Color(input_image.getRGB(0,0)).getRed();
		int to_match = new Color(input_image.getRGB(x,y)).getRed();
		while (to_match==white)
		{

			x++;
			if (x == input_image.getWidth())
			{
				x = 0;
				y++;
			}
			if (y >= input_image.getHeight())
			{
				break;
			}
			to_match = new Color(input_image.getRGB(x,y)).getRed();
		}
		return y < input_image.getHeight() ? y : -1;
	}

}


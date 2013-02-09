import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.awt.Color;
public class rotation {
	public static void main(String[] args) throws IOException
	{
		double a,b,a_new,b_new,angle;
		int a1,b1;
		File input_image = new File(args[0]);
		String[] parts = args[0].split("\\.");
		File output_name = new File(parts[0]+"_rotated"+parts[1]);
		BufferedImage oldImage = ImageIO.read(input_image);
		a=oldImage.getWidth();
		b=oldImage.getHeight();
		angle= 9;//Integer.parseInt(args[1]);
		a_new=a*Math.sin(angle)+b*Math.cos(angle);
		b_new=b*Math.sin(angle)+a*Math.cos(angle);
		//a1=(int)a_new;
		//b1=(int)b_new;
		b1=1*oldImage.getWidth();
		a1=3*oldImage.getHeight();
		BufferedImage newImage = new BufferedImage(a1,b1,oldImage.getType());
		Graphics2D graphics = (Graphics2D) newImage.getGraphics();
		graphics.rotate(Math.toRadians(9), newImage.getWidth() / 2, newImage.getHeight() / 2);
		graphics.translate((newImage.getWidth() - oldImage.getWidth()) / 2, (newImage.getHeight() - oldImage.getHeight()) / 2);
		graphics.drawImage(oldImage, 0, 0, oldImage.getWidth(), oldImage.getHeight(), null);
		ImageIO.write(newImage,parts[1],output_name);
	}
}


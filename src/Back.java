import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.awt.Graphics2D;

public class Back{
	
	private double x;
	private double y;
	
	private double moveScale;
	private BufferedImage image;
	
	public Back(String s, double ms){
		try{
			File file = new File(s);
			image = ImageIO.read(file);
		}catch(Exception e){}
		
		moveScale = ms;
	}
	
	public void setPosition(double x, double y){
		this.x = x * moveScale;
		this.y = y * moveScale;
	}
	
	public void draw(Graphics2D g){
		g.drawImage(image, (int)x, (int)y, null);
	}	
}
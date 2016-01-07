import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;

public class Save extends MapObject{
	
	private int x;
	private int y;
	private int saveX;
	private int saveY;
	
	private int width;
	private int height;
	
	private int tX;
	private int tY;
	
	private Animation animation;
	private BufferedImage[] save;
	private BufferedImage image;
	
	public Save(Map mp){
		super(mp);
		
		width = 60;
		height = 40;
		
		try{
			save = new BufferedImage[12];
			
			File saveFile = new File("Sprites/SAVE.png");
			image = ImageIO.read(saveFile);
			for(int i = 0; i < save.length; i++){
				save[i] = image.getSubimage(0, i * height, width, height);
			}
		}catch(Exception e){}
		
		animation = new Animation();
	}
	
	public void setPosition(int i, int j){
		x = i;
		saveX = i;
		
		y = j;
		saveY = j;
	}
	
	public void update(){
		animation.setFrame(save);
		animation.setDelay(100);
		animation.setSpeed(5);
		animation.update();
	}
	
	public void draw(Graphics2D g){
		tX = mp.getX();
		tY = mp.getY();
		
		g.drawImage(animation.getImage(), (int)(tX + x),(int)(tY + y), null);
	}
	
	public int getSaveX(){ return saveX; }
	public int getSaveY(){ return saveY; }
	
	public int getX(){ return x; }
	public int getY(){ return y; }
}
import java.awt.Graphics2D;
import java.awt.Color;

import java.io.File;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Door extends MapObject{
	
	private int x;
	private int y;
	
	private int tX;
	private int tY;
	
	private int width;
	private int height;
	
	private boolean exit;
	private boolean endGame;
	
	private BufferedImage[] door;
	private BufferedImage doorOn;
	private BufferedImage image;
	
	private Animation animation;
	
	public Door(Map mp){
		super(mp);
		width = 150;
		height = 145; 
		exit = false;
		endGame = false;
		
		x = 9770;
		y = 357;
		
		try{
			door = new BufferedImage[14];
			File doorFile = new File("Sprites/DOORnew.png");
			image = ImageIO.read(doorFile);
			for(int i = 0; i < door.length; i++){
				door[i] = image.getSubimage(i * width, 0, width, height);
			}
			
			File doorFileOn = new File("Sprites/der.png");
			doorOn = ImageIO.read(doorFileOn);
			
		}catch(Exception e){}
		
		animation = new Animation();
	}
	
	public void update(){
		
		if(exit){
			animation.setFrame(door);
			animation.setDelay(100);
			animation.setSpeed(1);
			animation.update();
		}
		
		if(animation.getCurrent() == door.length - 1){
			GamePanel.GameOFF = true;
			endGame = true;
		}
	}
	
	public void draw(Graphics2D g){
		tX = mp.getX();
		tY = mp.getY();
		
		if(!endGame){
			if(!exit){
				g.drawImage(doorOn, (int)(tX + x),(int)(tY + y), null);
			}else{
				g.drawImage(animation.getImage(), (int)(tX + x),(int)(tY + y),null);
			}
		}
	}
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	
	public int getHeight(){ return height; }
	public int getWidth(){ return width; }
	
	public void setDoorExit(boolean b){
		exit = b;
	}
	
	public boolean getExit(){ return exit; }
}
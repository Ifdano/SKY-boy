import java.awt.Graphics2D;
import java.awt.Color;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class VerBlocks extends Blocks{
	
	private File blockFile;
	
	public VerBlocks(Map mp, boolean door){
		super(mp);
		this.door = door;

		if(door){
			width = 30;
			height = 95;
			down = true;
		}else{
			width = 95;
			height = 30;
			up = true;
		}
		
		try{
			block = new BufferedImage[10];
			if(!door){
				blockFile = new File("sprites/BLOCKvertical.png");
				BufferedImage image = ImageIO.read(blockFile);
				for(int i = 0; i < block.length; i++){
					block[i] = image.getSubimage(0, i * height, width, height);
				}
			}else{
				blockFile = new File("sprites/BLOCKdoor.png");
				BufferedImage image = ImageIO.read(blockFile);
				for(int i = 0; i < block.length; i++){
					block[i] = image.getSubimage(i * width, 0, width, height);
				}
			}
		}catch(Exception e){}
		
		vertical = true;
		animation = new Animation();
	}
	
	public void update(){
		nextPosition();
		
		if(doorOpen && !doorEndOpen){
			mp.setX((int)(GamePanel.WIDTH/2 - x));
			mp.setY((int)(GamePanel.HEIGHT/2 - y));
			
			if(up && dy == 0){ doorEndOpen = true; Player.blockMove = false;}
			
			up = true;
		}
		
		animation.setFrame(block);
		animation.setDelay(100);
		animation.setSpeed(1);
		animation.update();
	}
	
	public void draw(Graphics2D g){
		tX = mp.getX();
		tY = mp.getY();
		
		g.drawImage(animation.getImage(), (int)(tX + x - width/2),(int)(tY + y - height/2), null);
	}
}
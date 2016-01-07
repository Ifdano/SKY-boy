import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;

public class Robot extends Enemies{

	private Animation animation;
	private BufferedImage[] walkSprites;
	private BufferedImage[] deadSprites;
	private int numMapCol;
	
	private boolean factingRight;
	
	public Robot(Map mp, boolean reverse){
		super(mp);
		this.reverse = reverse;
		
		try{
			
			walkSprites = new BufferedImage[23];
			deadSprites = new BufferedImage[10];
			
			File file = new File("sprites/WALKspritesRed.png");
			File deadFile = new File("sprites/DEATHsprite.png");
			
			BufferedImage image = ImageIO.read(file);
			for(int i = 0; i < walkSprites.length; i++){
				walkSprites[i] = image.getSubimage(i * width, 0 , width, height);
			}
			
			image = ImageIO.read(deadFile);
			for(int i = 0; i < deadSprites.length; i++){
				deadSprites[i] = image.getSubimage(i * width, 0 , width, height );
			}
		}catch(Exception w){}
		
		animation = new Animation();
		factingRight = true;
		
	}
	
	public void setPosition(int i, int j){
		x = i;
		y = j;
	}
		
	
	public void update(){
		if(!dead)
			nextPosition();
		
		if(right && dx == 0){
			right  = false;
			left = true;
			factingRight = false;
		}else{
			if(left && dx == 0){
				right = true;
				left = false;
				factingRight = true;
			}
		}
		
		if(dead){
			y += 5;
			if(y > mp.getMapHeight() * mp.getMapSize()){
				remove = true;
			}
		}
		
		if(dead){
			animation.setFrame(deadSprites);
			animation.setDelay(400);
			animation.setSpeed(3);
		}else{
			animation.setFrame(walkSprites);
			animation.setDelay(400);
		}
		animation.update();
	}
	
	public void draw(Graphics2D g){
		tX = mp.getX();
		tY = mp.getY();
		
		if(reverse){
			if(factingRight){
				g.drawImage(animation.getImage(), (int)(tX + x - width/2),(int)(tY + y + height/2), width, -height,  null);
			}else{
				g.drawImage(animation.getImage(), (int)(tX + x + width/2),(int)(tY + y + height/2),-width, -height, null);
			}
		}else{
			if(factingRight){
				g.drawImage(animation.getImage(), (int)(tX + x - width/2),(int)(tY + y - height/2), null);
			}else{
				g.drawImage(animation.getImage(), (int)(tX + x + width/2),(int)(tY + y - height/2),-width, height, null);
			}
		}
	}
}
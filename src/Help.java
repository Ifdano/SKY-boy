import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;

public class Help extends MapObject{
	
	private BufferedImage[] helps;
	private BufferedImage helpSpace;
	private BufferedImage helpGravityOn;
	private BufferedImage helpGravityOff;
	private BufferedImage helpTelekinesOn;
	private BufferedImage helpTelekinesOff;
	private BufferedImage image;
	
	private BufferedImage[] doit;
	private BufferedImage destroy;
	
	private int tX;
	private int tY;
	private int width;
	private int height;
	
	private Animation animation;
	private Animation animationDoIt;
	
	public Help(Map mp){
		super(mp);
		
		width = 60;
		height = 40;
		
		try{
			helps = new BufferedImage[12];
			
			File helpFile = new File("Sprites/HELP.png");
			image = ImageIO.read(helpFile);
			for(int i = 0; i < helps.length; i++){
				helps[i] = image.getSubimage(0, i * height, width, height);
			}
			
			File spaceFile = new File("Sprites/space.png");
			File gravOnFile = new File("Sprites/gravityOn.png");
			File gravOffFile = new File("Sprites/gravityOff.png");
			File telekOnFile = new File("Sprites/telekinesOn.png");
			File telekOffFile = new File("Sprites/telekinesOff.png");
			
			helpSpace = ImageIO.read(spaceFile);
			helpGravityOn = ImageIO.read(gravOnFile);
			helpGravityOff = ImageIO.read(gravOffFile);
			helpTelekinesOn = ImageIO.read(telekOnFile);
			helpTelekinesOff = ImageIO.read(telekOffFile);
			
			doit = new BufferedImage[12];
			
			File doitFile = new File("Sprites/DO _IT.png");
			image = ImageIO.read(doitFile);
			for(int i = 0; i < doit.length; i++){
				doit[i] = image.getSubimage(0, i * height, width, height);
			}
			
			File destroyFile = new File("Sprites/destroy.png");
			destroy = ImageIO.read(destroyFile);
			
		}catch(Exception e){}
		
		animation = new Animation();
		animationDoIt = new Animation();
	}
	
	public void update(){
		animation.setFrame(helps);
		animation.setDelay(100);
		animation.setSpeed(5);
		animation.update();
		
		animationDoIt.setFrame(doit);
		animationDoIt.setDelay(100);
		animationDoIt.setSpeed(5);
		animationDoIt.update();
	}
	
	public void draw(Graphics2D g){
		tX = mp.getX();
		tY = mp.getY();
		
		if(tX > -1500){
			g.drawImage(animation.getImage(), (int)(tX + 600),(int)(tY + 2630), null);
			g.drawImage(helpSpace, (int)(tX + 530),(int)(tY + 2550), null);
		}else{
			if(tX < -1500 && tX > -3000){
				g.drawImage(animation.getImage(), (int)(tX + 3000),(int)(tY + 2630), null);
				g.drawImage(helpGravityOff, (int)(tX + 3000),(int)(tY + 2550), null);
			}else{
				if(tX < -3000 && tX > -4400){
					g.drawImage(animation.getImage(), (int)(tX + 4300),(int)(tY + 2480), null);
					g.drawImage(helpGravityOn, (int)(tX + 4300),(int)(tY + 2410), null);
				}else{
					if(tX < -4400 && tX > -5500){
						g.drawImage(animation.getImage(), (int)(tX + 5500),(int)(tY + 2570), null);
						g.drawImage(helpTelekinesOn, (int)(tX + 5500),(int)(tY + 2500), null);
					}else{
						if(tX < -5500){
							g.drawImage(animation.getImage(), (int)(tX + 6300),(int)(tY + 2570), null);
							g.drawImage(helpTelekinesOff, (int)(tX + 6300),(int)(tY + 2500), null);
						}
					}
				}
			}
		}
	
		if(tX < -1100 && tX > -3400){
			g.drawImage(animationDoIt.getImage(), (int)(tX + 2180),(int)(tY + 2630), null);
			g.drawImage(destroy, (int)(tX + 2180),(int)(tY + 2580), null);
		}else{
			if(tX < -3400 && tX > -5770){
				g.drawImage(animationDoIt.getImage(), (int)(tX + 4660),(int)(tY + 2630), null);
				g.drawImage(destroy, (int)(tX + 4660),(int)(tY + 2590), null);
			}else{
				if(tX < -5700 && tX > -7500){
					g.drawImage(animationDoIt.getImage(), (int)(tX + 7170),(int)(tY + 2630), null);
					g.drawImage(destroy, (int)(tX + 7170),(int)(tY + 2590), null);
				}
			}
		}
	}
}
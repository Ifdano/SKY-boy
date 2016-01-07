import java.awt.Graphics2D;
import java.awt.Color;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HorBlocks extends Blocks{
	
	private File blockFile;
	public HorBlocks(Map mp, boolean telekines){
		super(mp);
		this.telekines = telekines;
		
		width = 95;
		height = 30;
		
		try{
			block = new BufferedImage[10];
			if(telekines){
				blockFile = new File("sprites/BLOCKtelekines.png");
			}else{ blockFile = new File("sprites/BLOCKhor.png"); }
				
			BufferedImage image = ImageIO.read(blockFile);
			for(int i = 0; i < block.length; i++){
				block[i] = image.getSubimage(0, i * height, width, height);
			}
			
		}catch(Exception e){}
		
		horizontal = true;
		if(!telekines)
			right = true;
		
		animation = new Animation();
	}
	
	public void update(){
		nextPosition();
		
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
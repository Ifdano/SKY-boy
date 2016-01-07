import java.awt.image.BufferedImage;

public class Animation{
	
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay ;
	private long upgradeTime;
	
	private int speed;
	
	public Animation(){
		speed = 10;
	}
	
	public void setFrame(BufferedImage[] images){
		frames = images;
		if(currentFrame >= frames.length)
			currentFrame = 0;
	}
	
	public void setSpeed(int i){ speed = i; }
	
	public void setDelay(long d){ delay = d; }
	
	public void update(){
		if(delay == -1)
			return;
		
		upgradeTime = (System.nanoTime() - startTime) / ( 1000000 / speed );
		if(upgradeTime > delay){
			currentFrame++;
			startTime = System.nanoTime();
		}
		
		if(currentFrame == frames.length)
			currentFrame = 0;
	}
	
	public int getCurrent(){ return currentFrame; }
	
	public BufferedImage getImage(){ return frames[currentFrame]; }
	
}
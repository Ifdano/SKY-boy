import java.awt.Graphics2D;
import java.awt.Color;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;

public class Fire extends MapObject{
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double tempY;
	private double tempX;
	private double toX;
	private double toY;
	
	private double gravity;
	private double maxFallingSpeed;
	
	private int curRow;
	private int curCol;
	
	private int width;
	private int height;
	
	private boolean reverse;
	private boolean falling;
	
	private int leftMap;
	private int rightMap;
	private int topMap;
	private int bottomMap;
	
	private boolean topLeft;
	private boolean topRight;
	private boolean bottomLeft;
	private boolean bottomRight;
	
	private int tX;
	private int tY;
	
	private BufferedImage[] fire;
	private BufferedImage image;
	private Animation animation;
	
	public Fire(Map mp, boolean reverse, int x, int y){
		super(mp);
		this.reverse = reverse;
		this.x = x;
		this.y = y;
		falling = true;
		
		width = 192;
		height = 55;
		
		gravity = 12.0;
		maxFallingSpeed = 12.0;
		
		try{
			fire = new BufferedImage[5];
			File fireFile = new File("Sprites/FIRElong.png");
			image  = ImageIO.read(fireFile);
			for(int i = 0; i < fire.length; i++){
				fire[i] = image.getSubimage(0, i * height, width, height);
			}
			
		}catch(Exception e){}
		
		animation = new Animation();
	}
	
	public void calculateCorners(double x, double y){
		leftMap = mp.getColMap((int)(x - width/2));
		rightMap = mp.getColMap((int)(x + width/2) - 1);
		topMap = mp.getRowMap((int)(y - height/2));
		bottomMap = mp.getRowMap((int)(y + height/2) - 1);
		
		topLeft = mp.isBlocked(topMap, leftMap);
		topRight = mp.isBlocked(topMap, rightMap);
		bottomLeft = mp.isBlocked(bottomMap, leftMap);
		bottomRight = mp.isBlocked(bottomMap, rightMap);
	}
	
	public void update(){
		
		if(falling){
			if(reverse){
				if(gravity > 0)
					gravity *= -1;
				
				dy += gravity;
				if(dy < -maxFallingSpeed)
					dy = -maxFallingSpeed;
			}else{
				if(gravity < 0)
					gravity *= -1;
				
				dy += gravity;
				if(dy > maxFallingSpeed)
					dy = maxFallingSpeed;
			}
		}
		
		curRow = mp.getRowMap((int)y);
		curCol = mp.getColMap((int)x);
		
		toX = x + dx;
		toY = y + dy;
		
		tempX = x;
		tempY = y;
		
		calculateCorners(x, toY);
		if(dy < 0){
			if(reverse){
				if(topLeft || topRight){
					dy = 0;
					falling = false;
					tempY = curRow * mp.getMapSize() + height/2;
				}else{
					tempY += dy;
				}
			}
		}
		if(dy > 0){
			if(bottomLeft || bottomRight){
				dy = 0;
				falling = false;
				tempY = (curRow + 1) * mp.getMapSize() - height/2;
			}else{
				tempY += dy;
			}
		}
		
		if(!falling){
			if(reverse){
				calculateCorners(x, y - 1);
			}else{
				calculateCorners(x, y + 1);
			}
			if(!bottomRight && !bottomLeft)
				falling = true;
		}
		
		x = tempX;
		y = tempY;
		
		animation.setFrame(fire);
		animation.setDelay(100);
		animation.setSpeed(1);
		animation.update();
	}
	
	public void draw(Graphics2D g){
		tX = mp.getX();
		tY = mp.getY();
		
		if(reverse){
			g.drawImage(animation.getImage(), (int)(tX + x - width/2), (int)(tY + y + height/2), width, -height, null);
		}else{
			g.drawImage(animation.getImage(), (int)(tX + x - width/2), (int)(tY + y - height/2), null);
		}
	}
	
	public int getX(){ return (int)x; }
	public int getY(){ return (int)y; }
	
	public int getHeight(){ return height; }
	public int getWidth(){ return width;  }
	
	public boolean isReverse(){ return reverse; }
}
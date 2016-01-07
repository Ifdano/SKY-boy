import java.awt.Graphics2D; 

public class Enemies extends MapObject{
	
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	protected int width;
	protected int height;
	
	protected double moveSpeed;
	protected double maxSpeed;
	protected double gravity;
	protected double maxFallingSpeed;
	
	protected boolean right;
	protected boolean left;
	protected boolean falling;
	
	protected double tempX;
	protected double tempY;
	protected double toX;
	protected double toY;
	
	protected int leftMap;
	protected int rightMap;
	protected int topMap;
	protected int bottomMap;
	
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	protected int tX;
	protected int tY;
	protected int curRow;
	protected int curCol;
	
	protected int health;
	protected boolean dead;
	protected boolean reverse;
	protected boolean remove;
	
	public Enemies(Map mp){
		super(mp);
		
		health = 1;
		
		moveSpeed = 3.0;
		maxSpeed = 3.0;
		gravity = 12.0;
		maxFallingSpeed = 12.0;
		
		width = 45;
		height = 33;
		
		falling = true;
		
		right = true;
		left = false;
	}
	
	public void hit(){
		if(dead)
			return;
		
		health--;
		if(health < 0)
			health = 0;
		if(health == 0)
			dead = true; 
	}
	
	public void isCrush(boolean b){
		left = b;
		if(!left)
			right = true;
	}
	
	protected void calculateCorners(double x, double y){
		leftMap = mp.getColMap((int)(x - width/2));
		rightMap = mp.getColMap((int)(x + width/2) - 1);
		topMap = mp.getRowMap((int)(y - height/2));
		bottomMap = mp.getRowMap((int)(y + height/2) - 1);
		
		topLeft = mp.isBlocked(topMap, leftMap);
		topRight = mp.isBlocked(topMap, rightMap);
		bottomLeft = mp.isBlocked(bottomMap, leftMap);
		bottomRight = mp.isBlocked(bottomMap, rightMap);
	}
	
	protected void nextPosition(){
		if(left){
			dx -= moveSpeed;
			if(dx < -maxSpeed)
				dx = -maxSpeed;
		}else{
			if(right){
				dx += moveSpeed;
				if(dx > maxSpeed)
					dx = maxSpeed;
			}
		}
		
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
		
		
		calculateCorners(toX, y);
		if(dx < 0){
			if(topLeft || bottomLeft){
				dx = 0;
				tempX = curCol * mp.getMapSize() + width/2;
			}else{
				tempX += dx;
			}
		}
		if(dx > 0){
			if(topRight || bottomRight){
				dx = 0;
				tempX = (curCol + 1) * mp.getMapSize() - width/2;
			}else{
				tempX += dx;
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
		
	}
	
	public boolean isDead(){ return dead; }
	protected boolean isRemove(){ return remove; }
	
	protected int getX(){ return (int)x; }
	protected int getY(){ return (int)y; }
	
	protected int getHeight(){ return height; }
	protected int getWidth(){ return width;  }
	
	protected boolean isReverse(){ return reverse; }
	
	public void update(){}
	
	public void draw(Graphics2D g){}
}
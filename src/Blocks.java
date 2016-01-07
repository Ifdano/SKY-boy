import java.awt.Graphics2D;
import java.awt.Color;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Blocks extends MapObject{
	
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	protected double moveSpeed;
	protected double maxSpeed;
	protected double gravity;
	protected double maxFallingSpeed;
	
	protected int curRow;
	protected int curCol;
	protected int tX;
	protected int tY;
	
	protected int width;
	protected int height;
	
	protected double tempY;
	protected double tempX;
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
	
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	
	protected boolean horizontal;
	protected boolean vertical;
	
	protected boolean door;
	protected boolean doorOpen;
	protected boolean doorEndOpen;
	
	protected boolean telekines;
	protected boolean telekinesTo;
	protected boolean telekinesFrom;
	protected boolean leftTelekines;
	protected boolean rightTelekines;
	
	protected BufferedImage[] block;
	protected Animation animation;
	
	public Blocks(Map mp){
		super(mp);
		
		moveSpeed = 2.0;
		maxSpeed = 2.0;
		gravity = 2.0;
		maxFallingSpeed = 2.0;
	}
	
	protected void setPosition(int i, int j){
		x = i;
		y = j;
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
		
		if(telekines){
			if(telekinesTo){
				if(leftTelekines){
					dx -= ( moveSpeed - 1);
					if(dx < - (maxSpeed -1))
						dx = -(maxSpeed-1);
				}else{
					if(rightTelekines){
						dx += moveSpeed-1;
						if(dx > maxSpeed-1)
							dx = maxSpeed-1;
					}
				}
			}else{
				if(telekinesFrom){
					if(leftTelekines){
						dx -= (moveSpeed-1);
						if(dx < -(maxSpeed-1))
							dx = -(maxSpeed-1);
					}else{
						if(rightTelekines){
							dx += moveSpeed-1;
							if(dx > maxSpeed-1)
								dx = maxSpeed-1;
						}
					}
				}else{
					dx = 0;
				}
			}
		}
		
		if(!telekines && horizontal){
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
		}
		
		if(vertical){
			if(up){
				if(gravity > 0)
					gravity *= -1;
				
				dy += gravity;
				if(dy < -maxFallingSpeed)
					dy = -maxFallingSpeed;
			}else{
				if(down){
					if(gravity < 0)
						gravity *= -1;
					
					dy += gravity;
					if(dy > maxFallingSpeed)
						dy = maxFallingSpeed;
				}
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
			if(topLeft || topRight){
				dy = 0;
				tempY = curRow * mp.getMapSize() + height/2;
			}else{
				tempY += dy;
			}
		}
		if(dy > 0){
			if(bottomLeft || bottomRight){
				dy = 0;
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
		
		if(!telekines && right && dx == 0){
			right = false;
			left = true;
		}else{
			if(!telekines && left && dx == 0){
				right = true;
				left = false;
			}
		}
		
		if(!door && up && dy == 0){
			up = false;
			down = true;
		}else{
			if(!door && down && dy == 0){
				up = true;
				down = false;
			}
		}
		
		x = tempX;
		y = tempY;
	}
	
	protected boolean isHorizontal(){ return horizontal; }
	protected boolean isVertical(){ return vertical; }
	
	protected int getX(){ return (int)x; }
	protected int getY(){ return (int)y; }
	
	protected int getWidth(){ return width; }
	protected int getHeight(){ return height; }
	
	protected int getDX(){ return (int)dx; } 
	
	protected boolean isDoor(){ return door; }
	protected boolean isDoorOpen(){ return doorOpen; }
	
	protected void setDoor(boolean b){ doorOpen = true; }
	
	protected void setTelekinesTo(boolean b, boolean c){
		telekinesTo = b;
		leftTelekines = c;
		
		if(!leftTelekines)
			rightTelekines = true;
	}
	
	protected void setTelekinesFrom(boolean b, boolean c){
		telekinesFrom = b;
		leftTelekines = c;
		
		if(!leftTelekines)
			rightTelekines = true;
	}
	
	protected boolean isTelekines(){ return telekines; }
	
	protected void update(){}
	
	protected void draw(Graphics2D g){}
}
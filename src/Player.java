import java.awt.Graphics2D;
import java.awt.Color;

import java.io.File;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends MapObject{

	public static boolean blockMove;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double health;
	private int healthWidth;
	private int healthHeight;
	
	private int startX;
	private int startY;
	
	private double moveSpeed;
	private double maxSpeed;
	private double stopSpeed;
	private double gravity;
	private double maxFallingSpeed;
	private double startJumping;
	
	private int width;
	private int height;
	private int width1;
	private int height1;
	
	private int curRow;
	private int curCol;
	private double tempX;
	private double tempY;
	private double toY;
	private double toX;
	
	private int tX;
	private int tY;
	
	private int leftMap;
	private int rightMap;
	private int topMap;
	private int bottomMap;
	
	private boolean topLeft;
	private boolean topRight;
	private boolean bottomLeft;
	private boolean bottomRight;
	
	private boolean left;
	private boolean right;
	private boolean jumping;
	private boolean falling;
	private boolean reverse;
	
	private boolean crush;
	private boolean leftCrush;
	private boolean rightCrush;
	private boolean upCrush;
	
	private boolean telekinesTo;
	private boolean telekinesFrom;

	private Animation animation;
	private BufferedImage[] idSprites;
	private BufferedImage[] jumpSprites;
	private BufferedImage[] fallSprites;
	private BufferedImage[] walkSprites;
	private BufferedImage[] crushSprites;
	private BufferedImage[] telekinesSprites;
	
	private BufferedImage[] healths;
	
	private boolean factingRight;
	
	private int enemiesSize;
	
	private HashMap<String, AudioPlayer> sfx;
	
	public Player(Map mp){
		super(mp);
		
		moveSpeed = 1.1;
		maxSpeed = 6.2;
		stopSpeed = 1.1;
		gravity = 0.64;
		maxFallingSpeed = 12.0;
		startJumping = -12.0;
		
		width = 42;
		width1 = 25;
		height = 60;
		height1 = 60;
		
		health = 6;
		healthWidth = 150;
		healthHeight = 40;
		
		try{
			
			idSprites = new BufferedImage[21];
			jumpSprites = new BufferedImage[9];
			fallSprites = new BufferedImage[9];
			walkSprites = new BufferedImage[30];
			crushSprites = new BufferedImage[17];
			telekinesSprites = new BufferedImage[10];
			
			File idFile = new File("sprites/IDsprite.png");
			File jumpFile = new File("sprites/JUMPsprite.png");
			File fallFile = new File("sprites/FALLsprite.png");
			File walkFile = new File("sprites/WALKsprite.png");
			File crushFile = new File("sprites/CRUSHsprite.png");
			File telekinesFile = new File("sprites/TELEKINESsprite.png");
			
			
			BufferedImage image = ImageIO.read(walkFile);
			for(int i = 0; i < walkSprites.length; i++){
				walkSprites[i] = image.getSubimage(i * width, 0, width, height);
			}
			
			image = ImageIO.read(idFile);
			for(int i = 0; i < idSprites.length; i++){
				idSprites[i] = image.getSubimage(i * width, 0, width, height);
			}
			
			image = ImageIO.read(crushFile);
			for(int i = 0; i < crushSprites.length; i++){
				crushSprites[i] = image.getSubimage(i * width, 0, width, height );
			}
			
			image = ImageIO.read(jumpFile);
			for(int i = 0; i < jumpSprites.length; i++){
				jumpSprites[i] = image.getSubimage(i * width, 0, width, height);
			}
			
			image = ImageIO.read(fallFile);
			for(int i = 0; i < fallSprites.length; i++){
				fallSprites[i] = image.getSubimage(i * width, 0 , width, height);
			}
			
			image = ImageIO.read(telekinesFile);
			for(int i = 0; i < telekinesSprites.length; i ++){
				telekinesSprites[i] = image.getSubimage(i * width, 0, width, height);
			}
			
			healths = new BufferedImage[3];
			File healthFile = new File("Sprites/HEALTH_big.png");
			image = ImageIO.read(healthFile);
			for(int i = 0; i < healths.length; i++){
				healths[i] = image.getSubimage(0, i * healthHeight, healthWidth, healthHeight);
			}
			
		}catch(Exception e){}

		animation = new Animation();
		factingRight = true;
		
		sfx = new HashMap<String, AudioPlayer>();
		sfx.put("jumping", new AudioPlayer("audio/jump.mp3"));
	}
	
	public void setPosition(int i , int j){
		x = i;
		startX = i;
		
		y = j;
		startY = j;
	}
	
	public void setLeft(boolean b){ left = b; }
	public void setRight(boolean b){ right = b; }
	public void setJumping(boolean b){
		if(!falling && !crush && !blockMove || upCrush)
			jumping = b;
	} 
	
	public void setReverse(boolean b){
		if(!crush && !falling)
			reverse = b; 
	}
	
	public void isCrush(boolean b, boolean c, boolean d){
		if(!crush){
			crush = b;
			leftCrush = c;
			upCrush = d;
			
			if(!leftCrush)
				rightCrush = true;
		}
	}
	
	public void setTelekinesTo(boolean b){ telekinesTo = b; }
	public void setTelekinesFrom(boolean b){ telekinesFrom = b; }
	
	public void calculateCorners(double x, double y){
		leftMap = mp.getColMap((int)(x - width1/2));
		rightMap = mp.getColMap((int)(x + width1/2) - 1);
		
		if(reverse){
			topMap = mp.getRowMap((int)(y + height1/2) - 1);
			bottomMap = mp.getRowMap((int)(y - height1/2));
		}else{
			topMap = mp.getRowMap((int)(y - height1/2));
			bottomMap = mp.getRowMap((int)(y + height1/2) - 1);
		}
		
		topLeft = mp.isBlocked(topMap, leftMap);
		topRight = mp.isBlocked(topMap, rightMap);
		bottomLeft = mp.isBlocked(bottomMap, leftMap);
		bottomRight = mp.isBlocked(bottomMap, rightMap);
	}
	
	public void update(){
		
		if(crush){
			if(animation.getCurrent() == crushSprites.length - 1){
				crush = false;
			}
		}
		
		if(crush){
			if(leftCrush){
				dx -= (moveSpeed + 0.3);
				if(dx < -(maxSpeed + 2.0))
					dx = -(maxSpeed + 2.0);
			}else{
				if(rightCrush){
					dx += moveSpeed + 0.3;
					if(dx > maxSpeed + 2.0)
						dx = maxSpeed + 2.0;
				}else{
					if(dx < 0){
						dx += stopSpeed;
						if(dx > 0)
							dx = 0;
					}
					if(dx > 0){
						dx -= stopSpeed;
						if(dx < 0)
							dx = 0;
					}
				}
			}
		}
		
		if(!crush && !blockMove && left){
			dx -= moveSpeed;
			if(dx < -maxSpeed)
				dx = -maxSpeed;
		}else{
			if(!crush && !blockMove && right){
				dx += moveSpeed;
				if(dx > maxSpeed)
					dx = maxSpeed;
			}else{
				if(dx < 0){
					dx += stopSpeed;
					if(dx > 0)
						dx = 0;
				}
				if(dx > 0){
					dx -= stopSpeed;
					if(dx < 0)
						dx = 0;
				}
			}
		}
		
		if(jumping){
			sfx.get("jumping").play();
			
			falling = true;
			jumping = false;
			dy = startJumping;
			upCrush = false;
		}
		
		if(falling){
			if(reverse){
				if(gravity > 0){
					gravity *= -1;
					startJumping *= -1;
				}
				
				dy += gravity;
				if(dy < -maxFallingSpeed)
					dy = -maxFallingSpeed;
				
			}else{
				if(gravity < 0){
					gravity *= -1;
					startJumping *= -1;
				}
				
				dy += gravity;
				if(dy > maxFallingSpeed)
					dy = maxFallingSpeed;
				
			}
		}else{
			dy = 0;
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
				if(bottomLeft || bottomRight){
					dy = 0;
					falling = false;
					tempY = curRow * mp.getMapSize() + height1/2;
				}else{
					tempY += dy;
				}
			}else{
				if(topLeft || topRight){
					dy = 0;
					tempY = curRow * mp.getMapSize() + height1/2;
				}else{
					tempY += dy;
				}
			}
		}
		if(dy > 0){
			if(reverse){
				if(topLeft || topRight){
					dy = 0;
					tempY = (curRow + 1) * mp.getMapSize() - height1/2;
				}else{
					tempY += dy;
				}
			}else{
				if(bottomLeft || bottomRight){
					dy = 0;
					falling = false;
					tempY = (curRow + 1) * mp.getMapSize() - height1/2;
				}else{
					tempY += dy;
				}
			}
		}
		
		
		calculateCorners(toX, y);
		if(dx < 0){
			if(topLeft || bottomLeft){
				dx = 0;
				tempX = curCol * mp.getMapSize() + width1/2;
			}else{
				tempX += dx;
			}
		}
		if(dx > 0){
			if(topRight || bottomRight){
				dx = 0;
				tempX = (curCol + 1) * mp.getMapSize() - width1/2;
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
		
		if(!blockMove){
			mp.setX((int)(GamePanel.WIDTH/2 - x));
			mp.setY((int)(GamePanel.HEIGHT/2 - y));
		}
		
		if(crush){
			animation.setFrame(crushSprites);
			animation.setDelay(400);
			animation.setSpeed(10);
		}else{
			if(left || right){
				animation.setFrame(walkSprites);
				animation.setDelay(100);
				animation.setSpeed(15);
			}else{
				if(telekinesTo || telekinesFrom){
					animation.setFrame(telekinesSprites);
					animation.setDelay(400);
					animation.setSpeed(8);
				}else{
					animation.setFrame(idSprites);
					animation.setDelay(400);
					animation.setSpeed(5);
				}
			}
		}
		
		if(dy < 0){
			if(!reverse){
				animation.setFrame(jumpSprites);
				animation.setDelay(100);
				animation.setSpeed(10);
			}else{
				animation.setFrame(fallSprites);
				animation.setDelay(100);
				animation.setSpeed(10);
			}
		}
		if(dy > 0){
			if(reverse){
				animation.setFrame(jumpSprites);
				animation.setDelay(100);
				animation.setSpeed(10);
			}else{
				animation.setFrame(fallSprites);
				animation.setDelay(100);
				animation.setSpeed(10);
			}
		}
		
		animation.update();
		
		if(left){
			factingRight = false;
		}else{
			if(right){
				factingRight = true;
			}
		}
	}
	
	public void draw(Graphics2D g){
		tX = mp.getX();
		tY = mp.getY();
		
		if(reverse){
			if(factingRight){
				g.drawImage(animation.getImage(), (int)(tX + x - width/2), (int)(tY + y + height/2),width, -height, null);
			}else{
				g.drawImage(animation.getImage(), (int)(tX + x + width/2), (int)(tY + y + height/2), -width, -height, null);
			}
		}else{
			if(factingRight){
				g.drawImage(animation.getImage(), (int)(tX + x - width/2), (int)(tY + y - height/2), null);
			}else{
				g.drawImage(animation.getImage(), (int)(tX + x + width/2), (int)(tY + y - height/2), -width, height, null);
			}
		}
		
		//health
		if(health > 4){
			g.drawImage(healths[0], 30, 30, null);
		}else{
			if(health > 2){
				g.drawImage(healths[1], 30, 30, null);
			}else{
				if(health > 0){
					g.drawImage(healths[2], 30, 30, null);
				}
			}
		}
	}
	
	//enemies crush
	public void checkEnemiesCrush(ArrayList<Enemies> enemies, int i){
		
		Enemies enemy = enemies.get(i);
		
		enemiesSize = enemies.size();
		
		if(!enemy.isDead()){
		if(enemy.isReverse()){
			if(x - width/2 < enemy.getX() - enemy.getWidth()/2 &&
			   x + width/2 >= enemy.getX() - enemy.getWidth()/2 &&
			   y + height/2 > enemy.getY() + enemy.getHeight()/2 &&
			   y - height/2 < enemy.getY() + enemy.getHeight()/2 - height/2){
				isCrush(true, true, false);
				hit();
			}else{
				if(x + width/2 > enemy.getX() + enemy.getWidth()/2 &&
				   x - width/2 <= enemy.getX() + enemy.getWidth()/2 &&
				   y + height/2 > enemy.getY() + enemy.getHeight()/2 &&
				   y - height/2 < enemy.getY() + enemy.getHeight()/2 - height/2){
					isCrush(true, false, false);
					hit();
				}else{
					if(x + width/2 > enemy.getX() - enemy.getWidth()/2 &&
					   x - width/2 < enemy.getX() + enemy.getWidth()/2  &&
					   y + height/2 > enemy.getY() + enemy.getHeight()/2 &&
					   y - height/2 <= enemy.getY() + enemy.getHeight()/2){
						enemies.get(i).hit();
						isCrush(false, false, true);
						jumping = true;
					}
				}
			}
		}else{
			if(x - width/2 < enemy.getX() - enemy.getWidth()/2 &&
			   x + width/2 >= enemy.getX() - enemy.getWidth()/2 &&
			   y - height/2 < enemy.getY() - enemy.getHeight()/2 &&
			   y + height/2 > enemy.getY() - enemy.getHeight()/2 + height/2){
				isCrush(true, true, false);
				hit();
			}else{
				if(x + width/2 > enemy.getX() + enemy.getWidth()/2 &&
				   x - width/2 <= enemy.getX() + enemy.getWidth()/2 &&
				   y - height/2 < enemy.getY() - enemy.getHeight()/2 &&
				   y + height/2 > enemy.getY() - enemy.getHeight()/2 + height/2){
					isCrush(true, false, false);
					hit();
				}else{
					if(x - width/2 < enemy.getX() + enemy.getWidth()/2 &&
					   x + width/2 > enemy.getX() - enemy.getWidth()/2 &&
					   y - height/2 < enemy.getY() - enemy.getHeight()/2 &&
					   y + height/2 >= enemy.getY() - enemy.getHeight()/2){
						enemies.get(i).hit();
						isCrush(false, false, true);
						jumping = true; } } } }
		}
		if(enemy.isRemove())
			enemies.remove(i);
	}
	
	//blocks
	public void checkBlocksCrush(ArrayList<Blocks> blocks, int i){
		
		Blocks block = blocks.get(i);
	
		if(block.isDoor() && !block.isDoorOpen()){
			if(enemiesSize <= 12 && enemiesSize > 7){
				if(block.getX() > 0 && block.getX() < 2600){
					blocks.get(i).setDoor(true);
					blockMove = true;
				}
			}else{
				if(enemiesSize <= 7 && enemiesSize > 3){
					if(block.getX() > 2600 && block.getX() < 5500){
						blocks.get(i).setDoor(true);
						blockMove = true;
					}
				}else{
					if(enemiesSize <= 3){
						if(block.getX() > 5500){
							blocks.get(i).setDoor(true);
							blockMove = true;
						}
					}
				}
			}
		}
		
		if(block.isTelekines()){
			if(x - width/2 > block.getX() + block.getWidth()/2 &&
			   y + height/2 > block.getY() - block.getHeight()/2 - height &&
			   y - height/2 < block.getY() + block.getHeight()/2 + height &&
			   !factingRight && x - width/2 - block.getX() < 370){
				blocks.get(i).setTelekinesTo(telekinesTo, false);
				
				if(!telekinesTo)
					blocks.get(i).setTelekinesFrom(telekinesFrom, true);
			}else{
				if(x + width/2 < block.getX() - block.getWidth()/2 &&
				 y + height/2 > block.getY() - block.getHeight()/2 - height &&
				 y - height/2 < block.getY() + block.getHeight()/2 + height &&
				 factingRight && block.getX() - x - width/2 < 370){
					blocks.get(i).setTelekinesTo(telekinesTo, true);
					
					if(!telekinesTo)
						blocks.get(i).setTelekinesFrom(telekinesFrom, false);
				}else{
					blocks.get(i).setTelekinesTo(false, true);
					blocks.get(i).setTelekinesFrom(false, false);
				}
			}
		}
		
		if(block.isHorizontal()){
			if(x + width/2 > block.getX() - block.getWidth()/2 + 5 &&
			   x - width/2 < block.getX() + block.getWidth()/2 - 5 &&
			   y - height/2 < block.getY() - block.getHeight()/2 &&
			   y + height/2 >= block.getY() - block.getHeight()/2){
				
				if(reverse){
					dy = -3;
					y = block.getY() - block.getHeight()/2 - height/2;
				}else{
					falling = false;
					x += block.getDX();
					y = block.getY() - block.getHeight()/2 - height/2;
				}
			}else{
				if(x + width/2 > block.getX() - block.getWidth()/2 + 5 &&
				   x - width/2 < block.getX() + block.getWidth()/2 - 5 &&
				   y + height/2 > block.getY() + block.getHeight()/2 &&
				   y - height/2 <= block.getY() + block.getHeight()/2){
					
					if(reverse){
						falling = false;
						x += block.getDX();
						y = block.getY() + block.getHeight()/2 + height/2;
					}else{
						dy = 3;
						y = block.getY() + block.getHeight()/2 + height/2;
					}
				}else{
					if(x - width/2 < block.getX() - block.getWidth()/2 &&
					   x + width/2 >= block.getX() - block.getWidth()/2 &&
					   y + height/2 > block.getY() - block.getHeight()/2 &&
					   y - height/2 < block.getY() + block.getHeight()/2){
						dx = 0;
						x = block.getX() - block.getWidth()/2 - width/2;
					}else{
						if(x + width/2 > block.getX() + block.getWidth()/2 &&
						   x - width/2 <= block.getX() + block.getWidth()/2 &&
						   y - height/2 < block.getY() + block.getHeight()/2 &&
						   y + height/2 > block.getY() - block.getHeight()/2){
							dx = 0;
							x = block.getX() + block.getWidth()/2 + width/2; } } } } }
		
		if(block.isVertical()){
			if(x + width/2 > block.getX() - block.getWidth()/2 + 5 &&
			   x - width/2 < block.getX() + block.getWidth()/2 - 5 &&
			   y - height/2 < block.getY() - block.getHeight()/2 &&
			   y + height/2 >= block.getY() - block.getHeight()/2){
				
				if(reverse){
					dy = -3;
					y = block.getY() - block.getHeight()/2 - height/2;
				}else{
					falling = false;
					y = block.getY() - block.getHeight()/2 - height/2 + 4;
				}
			}else{
				if(x + width/2 > block.getX() - block.getWidth()/2 + 5 &&
				   x - width/2 < block.getX() + block.getWidth()/2 - 5 &&
				   y + height/2 > block.getY() + block.getHeight()/2 &&
				   y - height/2 <= block.getY() + block.getHeight()/2){
					
					if(reverse){
						falling = false;
						y = block.getY() + block.getHeight()/2 + height/2 - 4;
					}else{
						dy = 3;
						y = block.getY() + block.getHeight()/2 + height/2;
					}
				}else{
					if(x - width/2 < block.getX() - block.getWidth()/2 &&
					   x + width/2 >= block.getX() - block.getWidth()/2 &&
					   y + height/2 > block.getY() - block.getHeight()/2 &&
				       y - height/2 < block.getY() + block.getHeight()/2){
						dx = 0;
						x = block.getX() - block.getWidth()/2 - width/2;
					}else{
						if(x + width/2 > block.getX() + block.getWidth()/2 &&
						   x - width/2 <= block.getX() + block.getWidth()/2 &&
						   y - height/2 < block.getY() + block.getHeight()/2 &&
						   y + height/2 > block.getY() - block.getHeight()/2){
							dx = 0;
							x = block.getX() + block.getWidth()/2 + width/2; } } } } }
		
	}
	
	//fires
	public void checkFireCrush(ArrayList<Fire> fires, int i){
		Fire fire = fires.get(i);
		
		if(fire.isReverse()){
			if(x - width/2 < fire.getX() - fire.getWidth()/2 &&
			   x + width/2 >= fire.getX() - fire.getWidth()/2 &&
			   y + height/2 > fire.getY() + fire.getHeight()/2 &&
			   y - height/2 < fire.getY() + fire.getHeight()/2 - height/2){
				x = startX;
				y = startY;
				reverse = false;
				falling = false;
				hit();
			}else{
				if(x + width/2 > fire.getX() + fire.getWidth()/2 &&
				   x - width/2 <= fire.getX() + fire.getWidth()/2 &&
				   y + height/2 > fire.getY() + fire.getHeight()/2 &&
				   y - height/2 < fire.getY() + fire.getHeight()/2 - height/2){
					x = startX;
					y = startY;
					reverse = false;
					falling = false;
					hit();
				}else{
					if(x + width/2 > fire.getX() - fire.getWidth()/2 &&
					   x - width/2 < fire.getX() + fire.getWidth()/2  &&
					   y + height/2 > fire.getY() + fire.getHeight()/2 &&
					   y - height/2 <= fire.getY() + fire.getHeight()/2){
						x = startX;
						y = startY;
						reverse = false;
						falling = false;
						hit();
					}
				}
			}
		}else{
			if(x - width/2 < fire.getX() - fire.getWidth()/2 &&
			   x + width/2 >= fire.getX() - fire.getWidth()/2 &&
			   y - height/2 < fire.getY() - fire.getHeight()/2 &&
			   y + height/2 > fire.getY() - fire.getHeight()/2 + height/2){
				x = startX;
				y = startY;
				reverse = false;
				falling = false;
				hit();
			}else{
				if(x + width/2 > fire.getX() + fire.getWidth()/2 &&
				   x - width/2 <= fire.getX() + fire.getWidth()/2 &&
				   y - height/2 < fire.getY() - fire.getHeight()/2 &&
				   y + height/2 > fire.getY() - fire.getHeight()/2 + height/2){
					x = startX;
					y = startY;
					reverse = false;
					falling = false;
					hit();
				}else{
					if(x - width/2 < fire.getX() + fire.getWidth()/2 &&
					   x + width/2 > fire.getX() - fire.getWidth()/2 &&
					   y - height/2 < fire.getY() - fire.getHeight()/2 &&
					   y + height/2 >= fire.getY() - fire.getHeight()/2){
						x = startX;
						y = startY;
						reverse = false;
						falling = false;
						hit();} } } }
	}
	
	//saves
	public void checkSave(ArrayList<Save> saves, int i){
		Save save = saves.get(i);
		
		if(x - width/2 < save.getX() &&
		   x + width/2 >= save.getX() &&
		   y - height/2 < save.getY() &&
		   y + height/2 > save.getY()){
			startX = save.getSaveX();
			startY = save.getSaveY();
		}
	}
	
	public void hit(){
		if(crush){
			health -= 0.05;
		}else{
			health -= 2;
		}
		
		if(health < 0)
			health = 0;
		if(health == 0)
			GamePanel.GameOFF = true;
	}
	
	//door exit
	public void checkDoor(Door door){
		if(x - width/2 >  door.getX() + width &&
		   y - height/2 > door.getY() + height &&
		   y + height/2 <= door.getY() + door.getHeight()){
		   door.setDoorExit(true);
		}
	}
}
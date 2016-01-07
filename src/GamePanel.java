import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

import java.io.File;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 600;
	
	public int FPS = 30;
	public int mainTime = 1000 / FPS;
	
	public Graphics2D g;
	
	public Thread thread;
	public boolean running;
	
	private Map map;
	private Player player;
	
	private ArrayList<Enemies> enemies;
	private Robot r;
	private Robot r1;
	private Robot r2;
	private Robot r3;
	private Robot r4;
	private Robot r5;
	private Robot r6;
	private Robot r7;
	private Robot r8;
	private Robot r9;
	private Robot r10;
	private Robot r11;
	private Robot r12;
	private Robot r13;
	private Robot r14;
	
	private ArrayList<Blocks> blocks;
	private HorBlocks hbl;
	private HorBlocks hbl1;
	private HorBlocks hbl2;
	private HorBlocks hbl3;
	private HorBlocks hbl4;
	private HorBlocks hbl5;
	private VerBlocks vbl; 
	private VerBlocks vbl1;
	private VerBlocks vbl2;
	private VerBlocks vbl3;
	private VerBlocks vbl4;
	private HorBlocks htelek;
	private HorBlocks htelek1;
	private HorBlocks htelek2;
	private HorBlocks htelek3;
	private HorBlocks htelek4;
	private HorBlocks htelek5;
	private HorBlocks htelek6;
	private HorBlocks htelek7;
	private VerBlocks vdoor;
	private VerBlocks vdoor1;
	private VerBlocks vdoor2;
	
	private Back back;
	
	private Help helps;

	private ArrayList<Fire> fires;
	private Fire fire;
	private int fireCol;
	
	private ArrayList<Save> saves;
	private Save save;
	private Save save1;
	private Save save2;
	
	private Door door;
	
	public static boolean GameON;
	public static boolean GameOFF;
	public BufferedImage gameOn;
	public BufferedImage gameOff;
	
	public AudioPlayer gameTheme;
	
	//constructor
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		try{
			File ONfile = new File("Sprites/GameON.png");
			File OFFfile = new File("Sprites/GameOFF.png");
			
			gameOn = ImageIO.read(ONfile);
			gameOff = ImageIO.read(OFFfile);
		}catch(Exception e){}
	}
	
	//thread
	public void addNotify(){
		super.addNotify();
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	
	//draw
	public void paint(Graphics q){
		g = (Graphics2D ) q;
		
		if(!GameON){
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH + 10, HEIGHT + 10);
			g.drawImage(gameOn, 5, 5, null);
		}else{
			if(GameOFF){
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, WIDTH + 10, HEIGHT + 10);
				g.drawImage(gameOff, 5, 5, null);
			}
		}
		
		if(GameON && !GameOFF){
			back.draw(g);
		
			map.draw(g);
		
			for(int i = 0; i < fires.size(); i++){
				fires.get(i).draw(g);
			}
			
			helps.draw(g);
		
			for(int i = 0; i < enemies.size(); i++){
				enemies.get(i).draw(g);
			}
			
			for(int i = 0; i < blocks.size(); i++){
				blocks.get(i).draw(g);
			}
		
			for(int i = 0; i < saves.size(); i++){
				saves.get(i).draw(g);
			}
		
			door.draw(g);
		
			if(!door.getExit())
				player.draw(g);
		}
	}
	
	//thread
	public void run(){
			
		init();
		
		long startTime;
		long upgradeTime;
		long waitTime;
		
		while(running){
			
			startTime = System.nanoTime();
			
			repaint();
			update();
		
			upgradeTime = ( System.nanoTime() - startTime ) / 1000000;
			waitTime = mainTime - upgradeTime;
			
			try{
				thread.sleep(waitTime);
			}catch(Exception e){}
		}
	}
	
	//initialize
	public void init(){
		gameTheme = new AudioPlayer("audio/gameTheme.mp3");
		gameTheme.play();
		
		map = new Map("Maps/map2.txt", 50);
		map.loadMap("Maps/Texture.gif");
		
		player = new Player(map);
		player.setPosition((map.getMapWidth() - 197) * map.getMapSize(), (map.getMapHeight() - 3) * map.getMapSize());
		
		enemies = new ArrayList<Enemies>();
		blocks = new ArrayList<Blocks>();
		
		back = new Back("Sprites/Back.gif", 1);
		
		helps = new Help(map);
		
		saves = new ArrayList<Save>();
		save = new Save(map);
		save.setPosition((map.getMapWidth() - 147) * map.getMapSize(), (map.getMapHeight() - 2) * map.getMapSize());
		save1 = new Save(map);
		save1.setPosition((map.getMapWidth() - 97) * map.getMapSize(), (map.getMapHeight() - 2) * map.getMapSize());
		save2 = new Save(map);
		save2.setPosition((map.getMapWidth() - 47) * map.getMapSize(), (map.getMapHeight() - 2) * map.getMapSize());
		
		saves.add(save);
		saves.add(save1);
		saves.add(save2);
		
		door = new Door(map);
		
		fires = new ArrayList<Fire>();
		fireCol = 0;
		
		r = new Robot(map, false);
		r.setPosition((map.getMapWidth() - 170) * map.getMapSize(), (map.getMapHeight() - 15) * map.getMapSize());
		r1 = new Robot(map, false);
		r1.setPosition((map.getMapWidth() - 164) * map.getMapSize(), (map.getMapHeight() - 21) * map.getMapSize());
		r2 = new Robot(map, false);
		r2.setPosition((map.getMapWidth() - 183) * map.getMapSize(), (map.getMapHeight() - 26) * map.getMapSize());
		r3 = new Robot(map, true);
		r3.setPosition((map.getMapWidth() - 130) * map.getMapSize(), (map.getMapHeight() - 5) * map.getMapSize());
		r4 = new Robot(map, true);
		r4.setPosition((map.getMapWidth() - 124) * map.getMapSize(), (map.getMapHeight() - 17) * map.getMapSize());
		r5 = new Robot(map, true);
		r5.setPosition((map.getMapWidth() - 137) * map.getMapSize(), (map.getMapHeight() - 17) * map.getMapSize());
		r6 = new Robot(map, true);
		r6.setPosition((map.getMapWidth() - 116) * map.getMapSize(), (map.getMapHeight() - 27) * map.getMapSize());
		r7 = new Robot(map, false);
		r7.setPosition((map.getMapWidth() - 137) * map.getMapSize(), (map.getMapHeight() - 35) * map.getMapSize());
		r8 = new Robot(map, true);
		r8.setPosition((map.getMapWidth() - 90) * map.getMapSize(), (map.getMapHeight() - 28) * map.getMapSize());
		r9 = new Robot(map, false);
		r9.setPosition((map.getMapWidth() - 90) * map.getMapSize(), (map.getMapHeight() - 35) * map.getMapSize());
		r10 = new Robot(map, true);
		r10.setPosition((map.getMapWidth() - 75) * map.getMapSize(), (map.getMapHeight() - 15) * map.getMapSize());
		r11 = new Robot(map, false);
		r11.setPosition((map.getMapWidth() - 8) * map.getMapSize(), (map.getMapHeight() - 15) * map.getMapSize());
		r12 = new Robot(map, true);
		r12.setPosition((map.getMapWidth() - 28) * map.getMapSize(), (map.getMapHeight() - 30) * map.getMapSize());
		r13 = new Robot(map, true);
		r13.setPosition((map.getMapWidth() - 22) * map.getMapSize(), (map.getMapHeight() - 40) * map.getMapSize());
		r14 = new Robot(map, false);
		r14.setPosition((map.getMapWidth() - 68) * map.getMapSize(), (map.getMapHeight() - 42) * map.getMapSize());
		
		hbl = new HorBlocks(map, false);
		hbl.setPosition((map.getMapWidth() - 160) * map.getMapSize(), (map.getMapHeight() - 5) * map.getMapSize()-15);
		hbl1 = new HorBlocks(map, false);
		hbl1.setPosition((map.getMapWidth() - 160) * map.getMapSize(), (map.getMapHeight() - 23) * map.getMapSize()-15);
		hbl2 = new HorBlocks(map, false);
		hbl2.setPosition((map.getMapWidth() - 130) * map.getMapSize(), (map.getMapHeight() - 17) * map.getMapSize()-15);
		hbl3 = new HorBlocks(map, false);
		hbl3.setPosition((map.getMapWidth() - 115) * map.getMapSize(), (map.getMapHeight() - 17) * map.getMapSize()-15);
		hbl4 = new HorBlocks(map, false);
		hbl4.setPosition((map.getMapWidth() - 76) * map.getMapSize(), (map.getMapHeight() - 30) * map.getMapSize()-15);
		hbl5 = new HorBlocks(map, false);
		hbl5.setPosition((map.getMapWidth() - 18) * map.getMapSize(), (map.getMapHeight() - 33) * map.getMapSize()-15);
		
		vbl = new VerBlocks(map, false);
		vbl.setPosition((map.getMapWidth() - 154) * map.getMapSize(), (map.getMapHeight() - 7) * map.getMapSize());
		vbl1 = new VerBlocks(map, false);
		vbl1.setPosition((map.getMapWidth() - 102) * map.getMapSize(), (map.getMapHeight() - 17) * map.getMapSize());
		vbl2 = new VerBlocks(map, false);
		vbl2.setPosition((map.getMapWidth() - 53) * map.getMapSize(), (map.getMapHeight() - 7) * map.getMapSize());
		vbl3 = new VerBlocks(map, false);
		vbl3.setPosition((map.getMapWidth() - 43) * map.getMapSize(), (map.getMapHeight() - 3) * map.getMapSize());
		vbl4 = new VerBlocks(map, false);
		vbl4.setPosition((map.getMapWidth() - 182) * map.getMapSize(), (map.getMapHeight() - 19) * map.getMapSize());
		
		htelek = new HorBlocks(map, true);
		htelek.setPosition((map.getMapWidth() - 79) * map.getMapSize() + 33, (map.getMapHeight() - 3) * map.getMapSize() - 20);
		htelek1 = new HorBlocks(map, true);
		htelek1.setPosition((map.getMapWidth() - 68) * map.getMapSize() + 33, (map.getMapHeight() - 3) * map.getMapSize() - 20);
		htelek2 = new HorBlocks(map, true);
		htelek2.setPosition((map.getMapWidth() - 65) * map.getMapSize(), (map.getMapHeight() - 16) * map.getMapSize() - 20);
		htelek3 = new HorBlocks(map, true);
		htelek3.setPosition((map.getMapWidth() - 66) * map.getMapSize() + 20, (map.getMapHeight() - 30) * map.getMapSize() - 20);
		htelek4 = new HorBlocks(map, true);
		htelek4.setPosition((map.getMapWidth() - 61) * map.getMapSize(), (map.getMapHeight() - 30) * map.getMapSize() - 20);
		htelek5 = new HorBlocks(map, true);
		htelek5.setPosition((map.getMapWidth() - 35) * map.getMapSize(), (map.getMapHeight() - 7) * map.getMapSize() - 20);
		htelek6 = new HorBlocks(map, true);
		htelek6.setPosition((map.getMapWidth() - 16) * map.getMapSize() , (map.getMapHeight() - 19) * map.getMapSize() - 20);
		htelek7 = new HorBlocks(map, true);
		htelek7.setPosition((map.getMapWidth() - 6) * map.getMapSize() + 40, (map.getMapHeight() - 33) * map.getMapSize() - 20);
		
		vdoor = new VerBlocks(map, true);
		vdoor.setPosition((map.getMapWidth() - 152) * map.getMapSize() + 33, (map.getMapHeight() - 3) * map.getMapSize());
		vdoor1 = new VerBlocks(map, true);
		vdoor1.setPosition((map.getMapWidth() - 102) * map.getMapSize() + 33, (map.getMapHeight() - 3) * map.getMapSize());
		vdoor2 = new VerBlocks(map, true);
		vdoor2.setPosition((map.getMapWidth() - 52) * map.getMapSize() + 33, (map.getMapHeight() - 3) * map.getMapSize());
		
		//2
		for(int i = 1; i <= 5; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 133 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 3) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;		
		for(int i = 1; i <= 6; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 139 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 13) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 8; i++){
			fires.add(new Fire(map, true,(map.getMapWidth() - ( 132 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 21) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 5; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 119 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 26) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 3; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 116 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 33) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		//3
		fireCol = 0;
		for(int i = 1; i <= 10; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 94 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 13) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 6; i++){
			fires.add(new Fire(map, true,(map.getMapWidth() - ( 73 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 22) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 4; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 65 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 25) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 2; i++){
			fires.add(new Fire(map, true,(map.getMapWidth() - ( 66 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 32) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		//4 
		fireCol = 0;
		for(int i = 1; i <= 2; i++){
			fires.add(new Fire(map, true,(map.getMapWidth() - ( 47 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 17) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 9; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 36 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 7) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 2; i++){
			fires.add(new Fire(map, true,(map.getMapWidth() - ( 9 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 7) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 5; i++){
			fires.add(new Fire(map, true,(map.getMapWidth() - ( 35 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 17) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 4; i++){
			fires.add(new Fire(map, true,(map.getMapWidth() - ( 24 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 24) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		fireCol = 0;
		for(int i = 1; i <= 6; i++){
			fires.add(new Fire(map, false,(map.getMapWidth() - ( 24 - fireCol)) * map.getMapSize(), (map.getMapHeight() - 28) * map.getMapSize() - 20 ));
			fireCol += 4;
		}
		
		enemies.add(r);
		enemies.add(r1);
		enemies.add(r2);
		enemies.add(r3);
		enemies.add(r4);
		enemies.add(r5);
		enemies.add(r6);
		enemies.add(r7);
		enemies.add(r8);
		enemies.add(r9);
		enemies.add(r10);
		enemies.add(r11);
		enemies.add(r12);
		enemies.add(r13);
		enemies.add(r14);
		
		blocks.add(hbl);
		blocks.add(hbl1);
		blocks.add(hbl2);
		blocks.add(hbl3);
		blocks.add(hbl4);
		blocks.add(hbl5);
		blocks.add(vbl);
		blocks.add(vbl1);
		blocks.add(vbl2);
		blocks.add(vbl3);
		blocks.add(vbl4);
		blocks.add(htelek);
		blocks.add(htelek1);
		blocks.add(htelek2);
		blocks.add(htelek3);
		blocks.add(htelek4);
		blocks.add(htelek5);
		blocks.add(htelek6);
		blocks.add(htelek7);
		blocks.add(vdoor);
		blocks.add(vdoor1);
		blocks.add(vdoor2);
		
		running = true;
	}
	
	//update
	private void update(){
		map.update();
		player.update();
		
		back.setPosition(map.getX(), map.getY());
		
		helps.update();
		
		door.update();
		player.checkDoor(door);
		
		for(int i = 0; i < saves.size(); i++){
			saves.get(i).update();
			player.checkSave(saves, i);
		}
		
		for(int i = 0; i < fires.size(); i++){
			fires.get(i).update();
			player.checkFireCrush(fires, i);
		}
				
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).update();
			player.checkEnemiesCrush(enemies, i);
		}
		
		for(int i = 0; i < blocks.size(); i++){
			blocks.get(i).update();
			player.checkBlocksCrush(blocks, i);
		}
	}
	
	//keyboard
	public void keyTyped(KeyEvent event){}
	
	public void keyPressed(KeyEvent event){
		int key = event.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT){ player.setLeft(true); }
		if(key == KeyEvent.VK_RIGHT){ player.setRight(true); }
		if(key == KeyEvent.VK_SPACE){ player.setJumping(true); }
		if(key == KeyEvent.VK_R){ player.setReverse(true); }
		if(key == KeyEvent.VK_SHIFT){ player.setReverse(false); }
		if(key == KeyEvent.VK_F){ player.setTelekinesTo(true); }
		if(key == KeyEvent.VK_G){ player.setTelekinesFrom(true); }
		
		if(!GameON){
			if(key == KeyEvent.VK_ENTER){ GameON = true; }
		}else{
			if(GameOFF){
				gameTheme.stop();
				if(key == KeyEvent.VK_ENTER){ System.exit(1); }
			}
		}
		
	}
	
	public void keyReleased(KeyEvent event){
		int key = event.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT){ player.setLeft(false); }
		if(key == KeyEvent.VK_RIGHT){ player.setRight(false); }
		if(key == KeyEvent.VK_F){ player.setTelekinesTo(false); }
		if(key == KeyEvent.VK_G){ player.setTelekinesFrom(false); }
		
		if(!GameON){
			if(key == KeyEvent.VK_ENTER){ GameON = true; }
		}
		
	} 	
}
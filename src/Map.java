import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Map{
	
	private int x;
	private int y;
	
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	
	private int mapHeight;
	private int mapWidth;
	
	private int mapSize;
	private int[][] map;
	
	private MapSet[][] maps;
	private BufferedImage mapImage;
	private BufferedImage subImage;
	private int numMapCol;
	
	public Map(String s, int mapSize){
		this.mapSize = mapSize;
		
		maxX = 0;
		maxY = 0;
		
		try{
			
			FileReader file = new FileReader(s);
			BufferedReader bf = new BufferedReader(file);
			
			mapWidth = Integer.parseInt(bf.readLine());
			mapHeight = Integer.parseInt(bf.readLine());
			
			minX = GamePanel.WIDTH - mapWidth * mapSize + 10;
			minY = GamePanel.HEIGHT - mapHeight * mapSize + 10;
			
			map = new int[mapHeight][mapWidth];
			String delimeters = "\\s+";
			
			for(int row = 0; row < mapHeight; row++){
				String line = bf.readLine();
				String[] tokens = line.split(delimeters);
				for(int col = 0; col < mapWidth; col++){
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}catch(Exception e){}
	}
	
	public void loadMap(String s){
		
		try{
			
			File file = new File(s);
			mapImage = ImageIO.read(file);
			numMapCol = mapImage.getWidth() / mapSize;
			maps = new MapSet[1][numMapCol];
			
			for(int col = 0; col < numMapCol; col++){
				subImage = mapImage.getSubimage(col * mapSize, 0, mapSize, mapSize);
				if(col == 0){
					maps[0][col] = new MapSet(subImage, false);
				}else{
					maps[0][col] = new MapSet(subImage, true);
				}
			}
		}catch(Exception e){}	
	}
	
	public void update(){}
	
	public int getRowMap(int y){ return y / mapSize; }
	public int getColMap(int x){ return x / mapSize; }
	
	public int getMapSize(){ return mapSize; }
	
	public int getMapHeight(){ return mapHeight; }
	public int getMapWidth(){ return mapWidth; }
	
	public void setX(int i){ 
		x = i;
		if(x < minX)
			x = minX;
		if(x > maxX)
			x = maxX;
	}
	public void setY(int i){ 
		y = i;
		if(y < minY)
			y = minY;
		if(y > maxY)
			y = maxY;
	}
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	
	public boolean isBlocked(int row, int col){
		int rc = map[row][col];
		
		int r = rc / maps[0].length;
		int c = rc % maps[0].length;
		
		return maps[r][c].isBlocked();
	}
	
	public void draw(Graphics2D g){
		for(int row = 0; row < mapHeight; row++){
			for(int col = 0; col < mapWidth; col++){
				if(col * mapSize * (-1) <= x + 50 && col * mapSize * (-1) >= x - 1000 &&
				   row * mapSize * (-1) <= y + 50 && row * mapSize * (-1) >= y - 600){
					int rc = map[row][col];
				
					int r = rc / maps[0].length;
					int c = rc % maps[0].length;
				
					g.drawImage(maps[r][c].getImage(), x + col * mapSize, y + row * mapSize, null);
				}
			}
		}
	}
	
}
import java.awt.image.BufferedImage;

public class MapSet{
	
	private BufferedImage image;
	private boolean blocked;
	
	public MapSet(BufferedImage image, boolean blocked){
		this.image = image;
		this.blocked = blocked;
	}
	
	public BufferedImage getImage(){ return image; }
	
	public boolean isBlocked(){ return blocked; }	
}
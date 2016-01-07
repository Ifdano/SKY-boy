import javax.swing.JFrame;

public class Main{
	public static void main(String[] args){
		JFrame window = new JFrame("SKY BOY");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.getContentPane().add(new GamePanel());
		
		window.pack();
		window.setVisible(true);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
	}
}
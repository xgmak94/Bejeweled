import javax.swing.*;

public class main {
	public static void main(String[] args) {
		Bejeweled game = new Bejeweled();
		game.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		game.setVisible(true);
		game.setResizable(false);
			
		game.pack();
	}
}

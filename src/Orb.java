import java.awt.Color;
import java.util.*; 
import javax.swing.*;
import javax.swing.border.LineBorder;

/*
 * 0	empty
 * 1 	red
 * 2	blue
 * 3	green
 * 4	light
 * 5	dark
 * 6	heart
 * 
 */

public class Orb extends JButton{
	int type;
	
	Random r;
	Color[] orbTypes= {Color.WHITE, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK, Color.PINK};
	int NUM_TYPES = orbTypes.length;
	
	LineBorder DEFAULTBORDER = new LineBorder(Color.BLACK, 2);
	LineBorder HIGHLIGHTEDBORDER = new LineBorder(Color.WHITE, 5);
	public Orb() {
		super();
		
		r = new Random();
		setType();
		setBackground();
	}
	
	public int getType() {
		return type;
	}
	
	public void setType() {
		type = r.nextInt(NUM_TYPES - 1) + 1; // between 0-5 + 1
		setBackground();
	}
	public void setType(int type) {
		this.type = type;
		setBackground();
	}
	
	public void setBackground() {
		this.setBackground(orbTypes[type]);
	}
	
	public void setDefaultBorder() {
		this.setBorder(DEFAULTBORDER);
	}
	
	public void setHighlightedBorder() {
		this.setBorder(HIGHLIGHTEDBORDER);
	}
}

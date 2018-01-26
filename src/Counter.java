import java.awt.*;

import javax.swing.*;

public class Counter extends JPanel{
	Font LABEL_FONT = new Font("Courier", Font.BOLD, 24);
	Font TEXT_FONT = new Font("Courier", Font.BOLD, 24);
	
	JLabel label;
	JTextField counter;
	int count = 0;
	
	public Counter(String name) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		textSetup();
		labelSetup(name);
		
		setCount();
		this.add(label);
		this.add(counter);
	}
	
	public void labelSetup(String name) {
		label = new JLabel(name);
		label.setFont(LABEL_FONT);
		label.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public void textSetup() {
		counter = new JTextField(10);
		counter.setAlignmentX(CENTER_ALIGNMENT);
		counter.setHorizontalAlignment(JTextField.CENTER);
		counter.setEditable(false);
		counter.setFont(TEXT_FONT);
	}
	
	public void increment(int n) {
		count += n;
		setCount();
	}
	
	public void setCount() {
		counter.setText(Integer.toString(count));
	}
}

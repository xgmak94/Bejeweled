import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Bejeweled extends JFrame{
	
	ActionListener orbListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Orb src = (Orb) e.getSource();
			
			if(first == null) {
				first = src;
				disableAllExcept(src);
			}
			else {
				if(first == src) {
					first = second = null;
					enableAll();
					return;
				}
				
				second = src;
				swap(first, second);
				
				enableAll();						
				numCleared.increment(match());
							
				while(emptyBoard() == false) {
					orbfall();
				}
				
				numMoves.increment(1);
				first = second = null;
			}
		}
	};
	
	static String TITLE = "Bejeweled";
	int WINDOW_WIDTH = 720, WINDOW_HEIGHT = 480;
	int NUM_COLUMNS = 6, NUM_ROWS = 5;
	
	Dimension BOARD_SIZE = new Dimension(WINDOW_WIDTH, 8 * WINDOW_HEIGHT / 10);
	Dimension OPTIONS_SIZE = new Dimension(WINDOW_WIDTH, 2 * WINDOW_HEIGHT / 10);
	
	JPanel gamePanel, boardPanel, optionsPanel;
	
	Counter numMoves, numCleared;
	Orb[][] board;
	Orb first, second;
	
	int NUM_TYPES = 6;
	int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
	
	public Bejeweled() {
		super(TITLE);
		this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		displaySetup(); boardSetup();		
		
		gamePanel = new JPanel();
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		
		addTogame();
		
		this.setContentPane(gamePanel);
	}
	
	void displaySetup() {	
		numMoves = new Counter("Moves");		
		numCleared = new Counter("Orbs Cleared");
		
		optionsPanel = new JPanel();
		optionsPanel.setPreferredSize(OPTIONS_SIZE);
		
		optionsPanel.add(numMoves); optionsPanel.add(numCleared);
	}
	
	void boardSetup() {
		boardPanel = new JPanel();
		boardPanel.setPreferredSize(BOARD_SIZE);
		boardPanel.setLayout(new GridLayout(NUM_ROWS, NUM_COLUMNS));
		
		board = new Orb[NUM_ROWS][NUM_COLUMNS];
		
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				Orb curr = board[i][j] = new Orb();			
				curr.setToolTipText("" + "row " + i + "," + "column " + j);			
				curr.setDefaultBorder();
				curr.addActionListener(orbListener);
				
				boardPanel.add(curr);
			}
		}
	}
	
	void addTogame() {
		gamePanel.add(optionsPanel);
		gamePanel.add(boardPanel);
	}
	
	int[] getCoordinates(Orb src) {
		int x = 0;
		int y = 0;		
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				if(board[i][j] == src) {
					x = i;
					y = j;
				}
			}
		}
		return new int[] {x, y};
	}
	
	void highlight(Orb src) {
		src.setHighlightedBorder();
	}
	
	void unhighlight(Orb src) {
		src.setDefaultBorder();
	}
	
	void enableAll() {
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				board[i][j].setEnabled(true);
				unhighlight(board[i][j]);
			}
		}
	}
	
	void disableAll() {
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				board[i][j].setEnabled(false);
			}
		}
	}
	
	void disableAllExcept(Orb src) {
		disableAll();
		
		ArrayList<Orb> adjacent = getAdjacent(src);
		for(Orb pair :adjacent) {
			pair.setEnabled(true); 
			highlight(pair);
		}
		src.setEnabled(true); highlight(src);
	}
	
	ArrayList<Orb> getAdjacent(Orb src) {
		int[] coord = getCoordinates(src);
		int x = coord[0]; int y = coord[1];
		ArrayList<Orb> list = new ArrayList<>();
			
		for(int[] dir :dirs) {
			int dx = x + dir[0]; int dy = y + dir[1];
			if(dx >= 0 && dx < NUM_ROWS && dy >= 0 && dy < NUM_COLUMNS) {
				list.add(board[dx][dy]);
			}
		}
		return list;
	}
	
	void swap(Orb x, Orb y) {
		int type1 = x.type;
		int type2 = y.type;
		x.setType(type2);
		y.setType(type1);
	};
	
	int match() {
		int numOrbs = 0;
		int[][] type = new int[NUM_ROWS][NUM_COLUMNS];
		boolean[][] clear = new boolean[NUM_ROWS][NUM_COLUMNS];
		
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				type[i][j] = board[i][j].getType();
			}
		}
		
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				if(clear[i][j] == false) {
					dfs(i, j, type, clear);
				}
			}
		}
		
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				if(clear[i][j] == true) {
					board[i][j].setType(0);
					numOrbs++;
				}
			}
		}
		return numOrbs;
	}
	
	void dfs(int x, int y, int[][] type, boolean[][] clear) {
		int currType = type[x][y];
		
		for(int[] dir : dirs) {
			int matches = 1;
			int dx = x + dir[0], dy = y + dir[1];
				
			while(dx >= 0 && dx < NUM_ROWS
					&& dy >= 0 && dy < NUM_COLUMNS
					&& currType == type[dx][dy]) {
				matches++;
				dx += dir[0]; dy += dir[1];
			}
			
			if(matches >= 3) { // 2 others in a row equal to first
				for(int i = 0 ; i < matches ; i++) {
					clear[x + i*dir[0]][y + i*dir[1]] = true;
				}
			}
		}
	}
	
	void orbfall() {
		Random r = new Random();
		int[][] newTypes = new int[NUM_ROWS][NUM_COLUMNS];
		
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				newTypes[i][j] = board[i][j].getType();
			}
		}
		
		for(int i = NUM_ROWS - 1 ; i >= 0  ; i--) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				if(newTypes[i][j] == 0) {
					if(i != 0) {
						newTypes[i][j] = newTypes[i-1][j];
						newTypes[i-1][j] = 0;
					}
					else if (i == 0) {
						newTypes[i][j] = r.nextInt(NUM_TYPES - 1) + 1;
					}
				}
			}
		}
		boardUpdate(newTypes);
	}
	
	boolean emptyBoard() {
		for(int i = 0 ; i < NUM_ROWS  ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				if(board[i][j].getType() == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	void boardUpdate(int[][] newTypes) {
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				board[i][j].setType(newTypes[i][j]);
			}
		}
	}
	
	void printBoard() {
		for(int i = 0 ; i < NUM_ROWS ; i++) {
			for(int j = 0 ; j < NUM_COLUMNS ; j++) {
				System.out.printf("%d ", board[i][j].getType());
			}
			System.out.println();
		}
		System.out.println();
	}
}


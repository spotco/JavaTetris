import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class GameManager {
	public int grid[][];
	public PlayerManager player;
	private int gridx;
	private int gridy;
	
	public GameManager(int x,int y) {
		grid = new int[x][y];
		gridx = x;
		gridy = y;
		player = new PlayerManager(gridx,gridy,this);
	}
	
	public void getPlayerInput(String key) {
		if (key.equals("RIGHT")) {
			player.actionRight();
		} else if (key.equals("LEFT")) {
			player.actionLeft();
		} else if (key.equals("ROTATE")) {
			player.actionRotate();
		} else if (key.equals("DOWN")) {
			player.actionDown();
		}
	}
	
	//0 - inactive
	//1 - static
	//9 - player controlled
	
	//checks all y rows if all blocks are 1 status
	//if satisfy conditions, converts all to 0's
	public int clearlines() {
		int retpoints = 0;
		for(int y = 0; y < gridy; y++) { 
			boolean clearthisline = true;
			for (int x = 0; x < gridx; x++) { 
				if (grid[x][y] != 1) { //checks if this line is in need of clearing
					clearthisline = false;
					break;
				}
			}
			if(clearthisline){
				InputStream ends = null;
				AudioStream endsa = null;
				try {
					ends = new FileInputStream("snd\\linebrk.wav");
					endsa = new AudioStream(ends); 
					} catch (FileNotFoundException e) {
					} catch (IOException e) {
				}
					
				AudioPlayer.player.start(endsa); 
				retpoints++;
				for (int x = 0; x < gridx; x++) {
					setInactive(x,y);
				}
				//this goes backwards
				//moves all lines above clearedline down by one
				for (int iy = y-1; iy > 0; iy--) { // all 1's above break line down by one sq
					for (int ix = 0; ix < gridx; ix++) {
						if (grid[ix][iy] == 1) {
							grid[ix][iy] = 0;
							grid[ix][iy+1] = 1;
						}
					}
				}
			}
		}
		return retpoints;
	}
	
	public boolean isStatic(int x, int y) {
		return (grid[x][y] == 1);
	}
	
	public boolean isActive(int x, int y) {
		return !(grid[x][y] == 0);
	}
	
	public void setActive(int x,int y) {
		grid[x][y] = 2;
	}
	
	public void setInactive(int x,int y) {
		grid[x][y] = 0;
	}
	
	//outputs view of current game
	//arraylist of strings, every element is new line
	public ArrayList<String> output() {
		ArrayList<String> ret = new ArrayList<String>();
		for(int y = 0; y < gridy; y++) {
			String add = "";
			for(int x = 0; x < gridx; x++){
				add = add + grid[x][y];
			}
			ret.add(add);
		}
		return ret;
	}
	
	public void viewPlayer() {
		for(int y = 0; y < gridy; y++) {
			for(int x = 0; x < gridx; x++){
				if (grid[x][y] == 9) {
					grid[x][y] = 0;
				}
			}
		}
		grid[player.ax][player.ay] = 9;
		grid[player.bx][player.by] = 9;
		grid[player.cx][player.cy] = 9;
		grid[player.dx][player.dy] = 9;
	}
	
	public void convertplayerstatic() {
		for(int y = 0; y < gridy; y++) {
			for(int x = 0; x < gridx; x++){
				if (grid[x][y] == 9) {
					grid[x][y] = 1;
				}
			}
		}
	}
}
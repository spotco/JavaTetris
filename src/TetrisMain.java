import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class TetrisMain {
	
	public static final int TIME = 25;
	//public static final int FALLSPD = 20; //20(slowest)-2(fastest)
	
	public static final int gridx = 11;
	public static final int gridy = 19;
	public static final int notypes = 7;

	public static void main(String args[]) {
		Random r = new Random();
		JFrame frame = new JFrame("javaTetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(225,420);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Font gamefont = new Font("Consolas", Font.ITALIC, 10);
		int highscore = 0;
		while(true) { //program loop
			frame.getContentPane().removeAll();
			JLabel highscore1 = new JLabel(); highscore1.setForeground(Color.yellow);
			highscore1.setText(""+highscore);
			highscore1.setFont(gamefont);
			highscore1.setBounds(123, 47, 225, 50);
			frame.add(highscore1);
			JLabel titlescreen = new JLabel();
			titlescreen.setIcon(new ImageIcon("img\\title.gif"));
			titlescreen.setBounds(0,-25,255,420);
			frame.add(titlescreen);
			frame.getContentPane().repaint();
			
			TitleListener titlekey = new TitleListener(frame);
			frame.addKeyListener(titlekey);
			InputStream in = null;
			AudioStream as = null;
			try {
				in = new FileInputStream("snd\\titlescreen.wav");
				as = new AudioStream(in); 
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
			}
			AudioPlayer.player.start(as);
			long init = System.currentTimeMillis();
			long titlecount = init;
			while (titlekey.cont) {
				try { 
					Thread.sleep(100); 
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
				titlecount = System.currentTimeMillis() - init;
				//System.out.println(titlecount);
				if (titlecount >= 28100) {
					init = System.currentTimeMillis();
					try {
						in = new FileInputStream("snd\\titlescreen.wav");
						as = new AudioStream(in); 
						} catch (FileNotFoundException e) {
						} catch (IOException e) {
					}
					AudioPlayer.player.start(as);
				}
				if (titlekey.trip) titlescreen.setIcon(new ImageIcon("img\\title0.gif"));
			}
			AudioPlayer.player.stop(as);
			frame.removeKeyListener(titlekey);
			GameManager game = new GameManager(gridx,gridy);
			
			JLabel staticdisp = new JLabel();
			staticdisp.setText("SCORE:      SPEED:      NEXT:");
			staticdisp.setBounds(11, 355, 225, 50);
			staticdisp.setForeground(Color.yellow);
			staticdisp.setFont(gamefont);
			
			JLabel pointdisp = new JLabel();
			pointdisp.setText("0");
			pointdisp.setBounds(55, 355, 225, 50);
			pointdisp.setForeground(Color.yellow);
			pointdisp.setFont(gamefont);
			
			JLabel spddisp = new JLabel();
			spddisp.setText("1");
			spddisp.setBounds(130, 355, 225, 50);
			spddisp.setForeground(Color.yellow);
			spddisp.setFont(gamefont);
			
			JLabel nxtdisp = new JLabel();
			nxtdisp.setIcon(new ImageIcon("img\\1.png"));
			nxtdisp.setBounds(195,365,30,30);
			
			ArrayList<JLabel> dispblock = new ArrayList<JLabel>();
			
			KeyListener klisten = new KeyListener(game, frame);
			frame.addKeyListener(klisten);
			game.player.generateNew(game.grid,r.nextInt(notypes)+1);
			int nextsto = r.nextInt(notypes)+1;
			changenext(nxtdisp,nextsto);
			titlecount = 0;
			int counter = 0;
			int fallspd = 20;
			int speedup = 0;
			int points = 0;
			InputStream ins = null;
			AudioStream asd = null;
			try {
				ins = new FileInputStream("snd\\mains.wav");
				asd = new AudioStream(ins); 
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
			}
			init = System.currentTimeMillis();
			AudioPlayer.player.start(asd); 
			while(true) { //game loop
				titlecount = System.currentTimeMillis() - init;
				//System.out.println(titlecount);
				if (titlecount >= 62400) { //CHECKTIME
					try {
						ins = new FileInputStream("snd\\mains.wav");
						asd = new AudioStream(ins); 
						} catch (FileNotFoundException e) {
						} catch (IOException e) {
					}
					init = System.currentTimeMillis();
				}
				AudioPlayer.player.start(asd); 
				if (game.player.hitcheck(game.grid)) { //player piece hit static
					game.convertplayerstatic();
					if (game.player.generateNew(game.grid,nextsto)) {
						
						game.viewPlayer();
						frame.removeKeyListener(klisten);
						//print(game.output());
						display(game.output(),frame,dispblock,titlekey.trip);
						frame.getContentPane().repaint();
						break;
					}
					nextsto = r.nextInt(notypes)+1;
				} else {
					if (counter >= fallspd) { //change this variable based on game speed
						game.player.fall();
						counter = 0;
					}
					game.player.event(game.grid); //player event (refresh every cycle)
				}
				counter++;
				game.viewPlayer();
				int stopoints = game.clearlines();
				if (stopoints > 0) {
					speedup++;
					if (speedup == 20) {
						if (fallspd > 1) {
							fallspd--;
						}
						speedup = 0;
					}
					points = points + (21-fallspd)*stopoints;
				}
				try { 
					Thread.sleep(TIME); 
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
				dispblock.clear();
				frame.getContentPane().removeAll();
				frame.add(spddisp);
				spddisp.setText(""+(21-fallspd));
				pointdisp.setText(""+points);
				frame.add(pointdisp);
				frame.add(staticdisp);
				changenext(nxtdisp,nextsto);
				frame.add(nxtdisp);
				display(game.output(),frame,dispblock,titlekey.trip);
				//print(game.output());
				frame.getContentPane().repaint();
			} //end game loop
			AudioPlayer.player.stop(asd); 
			frame.remove(staticdisp); frame.remove(nxtdisp); frame.remove(spddisp); frame.remove(pointdisp);
			JLabel gameoverdisp = new JLabel();
			gameoverdisp.setText("GAME OVER (SPACE)");
			gameoverdisp.setBounds(65, 355, 225, 50);
			gameoverdisp.setForeground(Color.yellow);
			gameoverdisp.setFont(gamefont);
			frame.add(gameoverdisp);
			frame.getContentPane().repaint();
			titlekey.cont = true;
			frame.addKeyListener(titlekey);
			int count = 0;
			InputStream ends = null;
			AudioStream endsa = null;
			try {
				ends = new FileInputStream("snd\\end.wav");
				endsa = new AudioStream(ends); 
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
			}
				
			AudioPlayer.player.start(endsa); 
			while (titlekey.cont) {
				count++;
				try { 
					Thread.sleep(100); 
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
				if (count == 5) {
					frame.remove(gameoverdisp);
					frame.getContentPane().repaint();
				}
				if (count == 10) {
					frame.add(gameoverdisp);
					frame.getContentPane().repaint();
					count = 0;
				}
			}
			AudioPlayer.player.stop(endsa); 
			frame.removeKeyListener(titlekey);
			highscore = Math.max(highscore, points);
		} //end program loop
	}
	
	public static void changenext(JLabel nxtdisp,int nextsto) {
		String sto = "img\\" + nextsto + ".png";
		nxtdisp.setIcon(new ImageIcon(sto));
	}
	
	//frame.getContentPane().removeAll();
	//frame.getContentPane().repaint();
	public static void display(ArrayList<String> output, JFrame frame, ArrayList<JLabel> dispblock, boolean trip) {
		for (int y = 0; y < output.size(); y++) {
			String sto = output.get(y);
			for (int x = 0; x < sto.length();x++) {
				if (sto.substring(x,x+1).equals("1") || sto.substring(x,x+1).equals("9")) {
					dispblock.add(new JLabel());
					if (trip) {
						dispblock.get(dispblock.size()-1).setIcon(new ImageIcon("img\\block0.gif"));
					} else {
						dispblock.get(dispblock.size()-1).setIcon(new ImageIcon("img\\block0.png"));
					}
					dispblock.get(dispblock.size()-1).setBounds(5+(x*19), 5+(y*19), 18, 18);
					frame.add(dispblock.get(dispblock.size()-1));
				}
			}
		}
	}
	
	//placeholder text-based gui
	//NOTE--gui should not shot y row 0
	public static void print(ArrayList<String> output) {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
		for (int i = 0; i < output.size(); i++) {
			String sto = output.get(i);
			for (int j = 0; j < sto.length();j++) {
				if (sto.substring(j,j+1).equals("0")) {
					System.out.print(" ");
				} else {
					System.out.print("X");
				}
			}
			System.out.print(output.get(i));
			System.out.println();
		}
	}
	
	public static class KeyListener extends KeyAdapter {
		private GameManager game;
		
		public KeyListener(GameManager game, JFrame frame) {
			this.game = game;
		}
		
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode() == 39)game.getPlayerInput("RIGHT"); //moveright
			if (event.getKeyCode() == 37)game.getPlayerInput("LEFT"); //moveleft
			if (event.getKeyCode() == 38)game.getPlayerInput("ROTATE"); //rotate
			if (event.getKeyCode() == 40)game.getPlayerInput("DOWN");
		}
	}
	
	public static class TitleListener extends KeyAdapter {
		public boolean cont;
		public boolean trip;
		public TitleListener(JFrame frame) {
			cont = true;
		}
		
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode() == 32)cont = false;
			if (event.getKeyCode() == 90)trip = true;
		}
	}
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 1000;
	static final int SCREEN_HEIGHT = 1000;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 65;
	final int xCoord[] = new int[GAME_UNITS];
	final int yCoord[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int foodEaten;
	int foodXCoord;
	int foodYCoord;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newFood();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			g.setColor(Color.red);
			g.fillOval(foodXCoord, foodYCoord, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(xCoord[i], yCoord[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(xCoord[i], yCoord[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.white);
			g.setFont(new Font("Times New Roman", Font.BOLD, 35));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + foodEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	public void newFood() {
		foodXCoord = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		foodYCoord = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodyParts; i > 0; i--) {
			xCoord[i] = xCoord[i-1];
			yCoord[i] = yCoord[i-1];
		}
		switch(direction) {
		case 'U':
			yCoord[0] = yCoord[0] - UNIT_SIZE;
			break;
		case 'D':
			yCoord[0] = yCoord[0] + UNIT_SIZE;
			break;
		case 'L':
			xCoord[0] = xCoord[0] - UNIT_SIZE;
			break;
		case 'R':
			xCoord[0] = xCoord[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkFood() {
		if((xCoord[0] == foodXCoord) && (yCoord[0] == foodYCoord)) {
			bodyParts++;
			foodEaten++;
			newFood();
		}
	}
	
	public void checkCollision() {
		//checks if head collides with body
		for(int i = bodyParts; i > 0; i--) {
			if((xCoord[0] == xCoord[i]) && (yCoord[0] == yCoord[i])) {
				running = false;
			}
		}
		//checks if head touches left boarder
		if(xCoord[0] < 0) {
			running = false;
		}
		//checks if head touches right boarder
		if(xCoord[0] > SCREEN_WIDTH) {
			running = false;
		}
		//checks if head touches top boarder
		if(yCoord[0] > SCREEN_HEIGHT) {
			running = false;
		}
		//checks if head touches top boarder
		if(yCoord[0] < 0) {
			running = false;
		}
		if(!running) {
			timer.stop();
		}
		
	}
	
	public void gameOver(Graphics g) {
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Times New Roman", Font.BOLD, 75));
		FontMetrics metricsGO = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metricsGO.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
		g.setColor(Color.white);
		g.setFont(new Font("Times New Roman", Font.BOLD, 35));
		FontMetrics metricsScore = getFontMetrics(g.getFont());
		g.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metricsScore.stringWidth("Score: " + foodEaten))/2, g.getFont().getSize());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkFood();
			checkCollision();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}

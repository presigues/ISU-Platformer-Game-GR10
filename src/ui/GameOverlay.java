package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import gamestates.Gamestates;
import gamestates.Playing;
import main.Game;

public class GameOverlay {
	private Playing playing;
	
	public GameOverlay(Playing playing) {
		this.playing = playing;
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0,  0, Game.GAME_HEIGHT, Game.GAME_HEIGHT);
		
		g.setColor(Color.WHITE);
		g.drawString("GAME OVER", Game.GAME_WIDTH / 2, 150);
		g.drawString("Press ESC to return to menu", Game.GAME_WIDTH / 3, 308);
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			Gamestates.state = Gamestates.MENU;
		}
	}
}

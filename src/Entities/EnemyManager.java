package Entities;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	
	private BufferedImage[][] doggoArr;
	
	private ArrayList<Doggo> dog = new ArrayList<>();
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}
	
	private void addEnemies() {
		dog = LoadSave.GetDoggo();
		
		
	}
	
	public void update(int[][] lvlData) {
		for (Doggo d : dog)
			d.update(lvlData);
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		drawDoggos(g, xLvlOffset);
	}
	
	private void drawDoggos(Graphics g, int xLvlOffset) {
		for(Doggo d : dog) {
		g.drawImage(doggoArr[d.getEnemyState()][d.getAniIndex()], (int) d.getHitbox().x - xLvlOffset - DOGGO_DRAWOFFSET_X, (int) d.getHitbox().y - DOGGO_DRAWOFFSET_Y, ENEMY_WIDTH, ENEMY_HEIGHT, null);
		d.drawHitbox(g, xLvlOffset);
		}
}
	private void loadEnemyImgs() {
		doggoArr = new BufferedImage[4][7];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_SPRITE);
		for(int j = 0; j < doggoArr.length; j++)
			for(int i = 0; i < doggoArr[j].length; i++) {
				doggoArr[j][i] = temp.getSubimage(i * ENEMY_WIDTH_DEFAULT, j * ENEMY_HEIGHT_DEFAULT, ENEMY_WIDTH_DEFAULT, ENEMY_HEIGHT_DEFAULT);
		System.out.print(j);
		System.out.print(i);
			
			}
	}
	
}

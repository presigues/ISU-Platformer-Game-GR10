package Entities;

import static utilz.Constants.EnemyConstants.*;
import main.Game;

public class Doggo extends Enemy  {

	public Doggo(float x, float y) {
		super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY);
		initHitbox(x, y, (int)(22 * Game.SCALE), (int)(19* Game.SCALE));
		
	
	}

}

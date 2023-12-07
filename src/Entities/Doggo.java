package Entities;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static utilz.HelpMethods.IsEntityOnFloor;
import static utilz.HelpMethods.IsFloor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import main.Game;

public class Doggo extends Enemy  {
	
	// Attackbox
	
		private Rectangle2D.Float attackBox;
		private int attackBoxOffsetX;

	public Doggo(float x, float y) {
		super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY);
		initHitbox(x, y, (int)(50 * Game.SCALE), (int)(30* Game.SCALE));
		initAttackBox();
		
	
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y,(int)(60 * Game.SCALE), (int) (25 * Game.SCALE));
		attackBoxOffsetX = (int)(Game.SCALE * 10);
		
	}

	public void update(int[][] lvlData, Player player) {
		updateMove(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
		 

	}
	
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
		
	}

	private void updateMove(int[][] lvlData, Player player) {
		if (firstUpdate) 
			firstUpdateCheck(lvlData);
		

		if (inAir) 
			updateInAir(lvlData);
		 else {
			switch (enemyState) {
			case HIT:
				newState(IDLE);
				break;
			case IDLE:
				
				if(canSeePlayer(lvlData, player))
					turnTowardsPlayer(player);
					if (isPlayerCloseForAttack(player))
						newState(ATTACK);
					
				move(lvlData);
				break;
		}

	}



	}
	
	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.RED);
		g.drawRect((int)(attackBox.x - xLvlOffset),(int) attackBox.y,(int) attackBox.width,(int) attackBox.height);
	}
	
	public int flipX() {
		if(walkDir == RIGHT)
			return width;
		else
			return 0;
	}
	
	public int flipW() {
		if (walkDir == RIGHT) 
			return -1;
		else 
			return 1;
		
			
		
		
	}

}
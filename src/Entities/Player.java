package Entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.HelpMethods.CanMoveHere;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;
import utilz.LoadSave;


public class Player extends Entity {
	
	// VARIABLES 
	private BufferedImage[][] ani;
	private int aniTick, aniIndex, aniSpeed = 40;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down, jump;
	private float playerSpeed = 2.0f * Game.SCALE;
	private int[][] lvlData;
	private float xDrawOffset = 13 * Game.SCALE;
	private float yDrawOffset = 20 * Game.SCALE;

	// Jumping / Gravity
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.75f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	
	// Status bar ui
	
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);
	
	private int healthBarWidth = (int) (119 * Game.SCALE);
	private int healthBarHeight = (int) (15 * Game.SCALE);
	private int healthBarXStart = (int) (52 * Game.SCALE);
	private int healthBaryStart = (int) (21 * Game.SCALE);
	
	private int maxHealth = 100;
	private int currentHealth = 100;
	private int healthWidth = healthBarWidth;
	
	
	Color clr = new Color(172, 24, 24);
	
	// Attackbox
	
	private Rectangle2D.Float attackBox;
	
    private int flipX = 0;
    private int flipW = 1;
	// MAIN
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, (int) (40 * Game.SCALE), (int) (27 * Game.SCALE));
		initAttackbox();

	}

	private void initAttackbox() {
		attackBox = new Rectangle2D.Float(x,y,(int)(20 * Game.SCALE), (int) (20 * Game.SCALE));
		
	}

	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
		
		updateHealth();
		updateAttackBox();
	}

	private void updateAttackBox() {
		if(right) {
			attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
		}else if (left) {
			attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);
		
	}

	private void updateHealth() {
		healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
		
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(ani[playerAction][aniIndex],
				(int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, 
				(int) (hitbox.y - yDrawOffset),
				width * flipW, height, null);
		drawHitbox(g, lvlOffset);
		drawAttackBox(g, lvlOffset);
		drawUI(g);
			}
	
	private void drawAttackBox(Graphics g, int lvlOffsetX) {
		g.setColor(clr);
		g.drawRect((int)attackBox.x - lvlOffsetX, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
		
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(clr);
		g.fillRect(healthBarXStart + statusBarX, healthBaryStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
			}

		}

	}

	private void setAnimation() {
		int startAni = playerAction;

		if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
		
		if (attacking) {
			playerAction = ATTACK;
		}else {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		


		if (startAni != playerAction)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		if (!inAir)
			if ((!left && !right) || (right && left))
				return;

		float xSpeed = 0;

		if (left) {
			xSpeed -= playerSpeed;
		    flipX = width;
		    flipW = -1;
		}    
		if (right) {
			xSpeed += playerSpeed;
		    flipX = 0;
		    flipW = 1;
		}
		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}

		} else
			updateXPos(xSpeed);
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;

	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;

	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}

	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0;
			// gameover();
			
		}else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	private void loadAnimations() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		ani = new BufferedImage[5][4];
		for (int j = 0; j < ani.length; j++)
			for (int i = 0; i < ani[j].length; i++)
				ani[j][i] = img.getSubimage(i * 40, j *38, 40, 40);
		
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

}

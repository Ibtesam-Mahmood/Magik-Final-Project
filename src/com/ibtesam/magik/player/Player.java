package com.ibtesam.magik.player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ibtesam.magik.enumerations.ActionType;
import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.RuneType;
import com.ibtesam.magik.gui.Text;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.puppets.BaseActor;
import com.ibtesam.magik.puppets.SuperStage;
import com.ibtesam.magik.spellsandeffects.Debuff;
import com.ibtesam.magik.spellsandeffects.Rune;
import com.ibtesam.magik.spellsandeffects.Spell;

public abstract class Player extends BaseActor{

	protected BaseActor rune1Actor;
	protected BaseActor rune2Actor;
	protected BaseActor playerCursorArrow;	
	
	protected Texture playerTexture;
	protected Texture playerTextureOpage;
	
	protected String playerImageString;

	protected int speed;

	protected float health;
	protected float mana;
	protected float targetX, targetY;
	protected float previousX, previousY;
	protected float cursorRotation;
	protected float cursorRotationRadius;
	
	protected String playerString;
	
	public static final int PLAYERSPEED = 300;
	public static final int PLAYERHEALTH = 100;
	public static final int PLAYERMANA = 100;
	
	protected int knockBack = 0;
	protected float KBDir;

	protected Game game;

	protected ArrayList<Debuff> deBuffs;
	protected ArrayList<Spell> spells;
	protected Rune rune1;
	protected Rune rune2;

	protected Stage playerHud;

	public boolean spellCollision = true;
	protected boolean dashing = false;
	protected boolean healing = true;
	protected boolean burning = false; //if true debuff exsists within arraylist
	protected boolean stunned = false;
	protected boolean slowed = false;
	protected boolean frozen = false;
	
	protected double dir;
	
	protected float healthBack;
	
	protected short count = 0;
	
	public boolean key = true;
	public boolean canCast = true;
	public boolean canRune = true;
	public boolean canAction = true;
	public boolean canMove = true;

	protected Player(Game game, float x, float y) {
		super(x, y, 44, 64, new Texture(Gdx.files.internal("assets/player/playerSheet.png")), 4, 3, PLAYERSPEED );
		this.game = game;
		health = PLAYERHEALTH;
		healthBack = health;
		mana = PLAYERMANA;
		speed = PLAYERSPEED;
		spellCollision = true;
		velocityX = 0;
		velocityY = 0;
		targetX = x;
		targetY = y;
		cursorRotation = 0;
		cursorRotationRadius = 40;
		rune1 = new Rune();
		rune2 = new Rune();
		spells = new ArrayList<Spell>();
		deBuffs = new ArrayList<Debuff>();
		playerHud = new SuperStage();
		initilizePlayerHUD();
	}

	public abstract void act(float dt);

	public abstract void render();
	
	protected void playerTick(float dt){
		
		handlePlayerPosition(dt);
		
		
		updateActorPos();
		
		
		handleStats(dt);

		
		playerHud.act(dt);
		
		if(count % 25 == 0 && spellCollision == false){
			spellCollision = true;
			setAnimations(playerTexture, speed, 4, 3);
		}
		
		for(int i = 0; i < spells.size(); i++){
			spells.get(i).tick(dt);
		}
		
		handleCounter();
		
		

	}
	
	private void handleCounter() {
		if(count == 1000)
			count = 0;
		count++;
	}
	
	private void handlePlayerPosition(float dt) {
		previousX = getX();
		previousY = getY();
		
		velocityY = 0;
		velocityX = 0;
		
		setDir();
		
		setTargetX( Game.clamp(getTargetX(), 0, Launcher.WIDTH-getWidth()) );
		setTargetY( Game.clamp(getTargetY(), 0, Launcher.HEIGHT-getHeight()) );
		
		if(knockBack != 0){
			performDash(knockBack, KBDir);
			knockBack = 0;
			KBDir = 0;
		}
		
		if(getBoundingPoly().contains(targetX, targetY) && (key || dashing)){
			if(dashing){
				dashing = false;
				speed = PLAYERSPEED;
				setX((float) Math.floor(getX()));
				setY((float) Math.floor(getY()));
			}
		}
		else if(key || dashing){
			velocityX = (float) (speed*Math.cos(dir));
			velocityY = (float) (speed*Math.sin(dir));
			
		}
		

		
		setX( Game.clamp(getX(), 0, Launcher.WIDTH-getWidth()) );
		setY( Game.clamp(getY(), 0, Launcher.HEIGHT-getHeight()) );
	}
	
	protected void handleStats(float dt){
		for (int i = 0; i < deBuffs.size(); i++) {
			deBuffs.get(i).tick(dt);
		}
		
		checkDebuffs();
		
		if(healing)
			health += 0.01f;
		
		health = Game.clamp(health, 0, 100);
		
		if(velocityX == 0 && velocityY == 0)
			mana += 0.1f;
		mana += 0.1f;
		
		if(healthBack != health)
			healthBack -= 0.5f;
		
		healthBack = Game.clamp(healthBack, health, 100);
		
		mana = Game.clamp(mana, 0, 100);
	}

	public void setDir() {
		this.dir = Math.atan2((targetY - getCenterY()), (targetX - getCenterX()));
	}

	protected void initilizePlayerHUD() {
		
		Texture t;
		
		t =  new Texture(Gdx.files.internal("assets/player/nullRune.png"));
		rune1Actor = new BaseActor((int) getX() - getWidth() / 2, (int) getY() + getHeight() / 2, 20, 20, t);
		playerHud.addActor(rune1Actor);
		
		t = new Texture(Gdx.files.internal("assets/player/nullRune.png"));
		rune2Actor = new BaseActor((int) getX() - getWidth() / 2, (int) getY(), 20, 20, t);
		playerHud.addActor(rune2Actor);
		
		t = new Texture(Gdx.files.internal("assets/player/arrow.png"));
		playerCursorArrow = new BaseActor((float)  getCenterX(), (float) getCenterY(), t.getWidth(), t.getHeight(), t, cursorRotation);
		playerHud.addActor(playerCursorArrow);		
		
	}
	

	public void conjure(Rune rune1, Rune rune2, double dir) {

		
		spells.add(new Spell(rune1, rune2, this, getCenterX() + (float) (100*Math.cos(dir)), getCenterY() + (float) (100*Math.sin(dir)) ));
		//System.out.println("x: " + 100*Math.cos(dir) + ", y:" + 100*Math.sin(dir));
		clearRunes();

	}
	
	public void clearRunes(){
		this.rune1 = new Rune();
		this.rune2 = new Rune();
	}

	public void addRune(ElementType element) {
		if (rune1.getType() == RuneType.NULL) {
			rune1 = new Rune(element);
			// System.out.println("rune 1: element - " + element.toString());
		} else if (rune2.getType() == RuneType.NULL) {
			rune2 = new Rune(element);
			// System.out.println("rune 2: element - " + element.toString());
		}
	}

	public void addRune(ActionType action) {
		if (rune1.getType() == RuneType.NULL)
			useAction(action);
		else if (rune2.getType() == RuneType.NULL && rune1.getType() != RuneType.ACTION){
			rune2 = new Rune(action);
			conjure(rune1, rune2, cursorRotation);
		}
	}

	public void updateRuneTexture() {

		updateRuneSprite(rune1, rune1Actor);
		updateRuneSprite(rune2, rune2Actor);

	}

	protected void updateRuneSprite(Rune rune, BaseActor runeActor) {

		if (rune.getType() == RuneType.NULL) {
			runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/nullRune.png")));
		} else if (rune.getType() == RuneType.ELEMENT) {
			if (rune.geteType() == ElementType.FIRE) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/fireRune.png")));
			} else if (rune.geteType() == ElementType.WATER) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/waterRune.png")));
			} else if (rune.geteType() == ElementType.LIGHTNING) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/lightningRune.png")));
			} else if (rune.geteType() == ElementType.ICE) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/iceRune.png")));
			}
		} else if (rune.getType() == RuneType.ACTION) {
			if (rune.getaType() == ActionType.DASH) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/dashRune.png")));
			} else if (rune.getaType() == ActionType.SLASH) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/slashRune.png")));
			} else if (rune.getaType() == ActionType.SHOOT) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/shootRune.png")));
			} else if (rune.getaType() == ActionType.SHIELD) {
				runeActor.setTexture(new Texture(Gdx.files.internal("assets/player/sheildRune.png")));
			}
		}

	}
	
	float k = cursorRotation;

	protected void updateActorPos() {

		if(rune1Actor.getX() != getX() - getWidth()/ 2)
			rune1Actor.setX(getX() - getWidth() / 2);
		if(rune1Actor.getY() != getY() + getHeight() / 2)
			rune1Actor.setY(getY() + getHeight() / 2);
		
		if(rune2Actor.getX() != getX() - getWidth() / 2)
			rune2Actor.setX(getX() - getWidth() / 2);
		if(rune2Actor.getY() != getY())
			rune2Actor.setY(getY());
		
		k++;
		
		playerCursorArrow.setRotation((float) Math.toDegrees(cursorRotation));
		playerCursorArrow.setPosition((float) (getCenterX() + cursorRotationRadius*Math.cos(cursorRotation)), (float) (getCenterY() + (cursorRotationRadius*8/7)*Math.sin(cursorRotation)) - cursorRotationRadius/7 );
		
		
		
	}

	protected void useAction(ActionType action) {
		if (action == ActionType.SHIELD && mana >= 30 && spellCollision) {
			spellCollision = false;
			mana -= 30;
			setAnimations(playerTextureOpage, speed, 4, 3);
		}
		else if (action == ActionType.SHOOT) {
			conjure(new Rune(ActionType.SHOOT), new Rune(), cursorRotation);
		}
		else if (action == ActionType.DASH) {
			performDash(200);
		}
		else if (action == ActionType.SLASH) {
			conjure(new Rune(ActionType.SLASH), new Rune(), cursorRotation);
		}
	}
	
	public void performDash(float distance){
		speed = PLAYERSPEED * 2;
		dashing = true;
		targetX = Gdx.input.getX();
		targetY = (Launcher.HEIGHT - Gdx.input.getY());
		float dir = (float) Math.atan2((targetY - getCenterY()), (targetX -  getCenterX()));
		//System.out.println("heading: " + Math.toDegrees(dir));
		targetX = (float) (getCenterX()  + (Math.cos(dir)*distance));
		targetY = (float) (getCenterY() + (Math.sin(dir)*distance));
	}
	
	public void performDash(float distance, int dashSpeed){
		performDash(distance);
		speed = dashSpeed;
	}
	
	public void performDash(float distance, float dir){
		speed = PLAYERSPEED * 2;
		dashing = true;
		targetX = (float) (getCenterX()  + (Math.cos(dir)*distance));
		targetY = (float) (getCenterY() + (Math.sin(dir)*distance));
	}
	
	private void checkDebuffs() {
		for (int i = 0; i < deBuffs.size(); i++) {
			if(deBuffs.get(i).getDebuff() == Debuffs.BURN){
				burning = true;
			}
			
			if(deBuffs.get(i).getDebuff() == Debuffs.STUN){
				stunned = true;
			}
			
			if(deBuffs.get(i).getDebuff() == Debuffs.SLOW){
				slowed = true;
			}
			
			if(deBuffs.get(i).getDebuff() == Debuffs.FREEZE){
				frozen = true;
			}
		}
	}
	
	public void writeMessage(String message, float x, float y, float duration, Color c){
		if(c == null){
			playerHud.addActor(new Text(message, x, y, duration));
		}
		else{
			playerHud.addActor(new Text(message, x, y, duration, c));

		}
	}
	

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		if(health > this.health)
			writeMessage("+" + (int) (health - this.health), getX() + getWidth(), getY() + getHeight(), 50, Color.GREEN);

		else
			writeMessage("" + (int) (health - this.health), getX() + getWidth(), getY() + getHeight(), 50, Color.RED);
		this.health = health;
	}

	public float getHealthBack() {
		return healthBack;
	}

	public float getMana() {
		return mana;
	}

	public void setMana(float mana) {
		if(mana > this.mana)
			writeMessage("+" + (int) (mana - this.mana), getX() + getWidth(), getY() + getHeight(), 50, Color.BLUE);

		else
			writeMessage("" + (int) (mana - this.mana), getX() + getWidth(), getY() + getHeight(), 50, Color.BLUE);
		this.mana = mana;
		
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		if(this.speed != speed){
			if(speed > this.speed)
				writeMessage("inc", getX() + getWidth(), getY() + getHeight(), 50, Color.ORANGE);
	
			else
				writeMessage("down" , getX() + getWidth(), getY() + getHeight(), 50, Color.LIGHT_GRAY);
			this.speed = speed;
		}
	}


	public Game getGame() {
		return game;
	}



	public double getVelX() {
		return velocityX;
	}

	public void setVelX(int velX) {
		this.velocityX = velX;
	}

	public double getVelY() {
		return velocityY;
	}

	public void setVelY(int velY) {
		this.velocityY = velY;
	}


	public float getTargetX() {
		return targetX;
	}

	public void setTargetX(float targetX) {
		this.targetX = targetX;
	}

	public float getTargetY() {
		return targetY;
	}

	public void setTargetY(float targetY) {
		this.targetY = targetY;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}
	
	public boolean isDebuff(Debuffs debuff){
		if(debuff == Debuffs.BURN)
			return burning;
		else if(debuff == Debuffs.STUN)
			return stunned;
		else if(debuff == Debuffs.SLOW)
			return slowed;
		else if(debuff == Debuffs.FREEZE)
			return frozen;
		else
			return true;
	}
	
	public void setDebuffSwitch(Debuffs debuff, boolean set){
		if(debuff == Debuffs.BURN)
			burning = set;
		else if(debuff == Debuffs.STUN)
			stunned = set;
		else if(debuff == Debuffs.SLOW)
			slowed = set;
		else if(debuff == Debuffs.FREEZE)
			frozen = set;
	}

	public Polygon rightBoundaryRectangle() {
		int x = (int) (getX() + getWidth() * 4 / 5);
		int y = (int) (getY() + getHeight() / 5);
		int w = (int) (getWidth() / 5);
		int h = (int) (getHeight() * 2 / 3);
		return new Polygon(new float[] {
    			x, y,
    			x, y + h,
    			x + w, y + h,
    			x + w, y
            });
	}

	public Polygon leftBoundaryRectangle() {
		int x = (int) (getX());
		int y = (int) (getY() + getHeight() / 5);
		int w = (int) (getWidth() / 5);
		int h = (int) (getHeight() * 2 / 3);
		return new Polygon(new float[] {
    			x, y,
    			x, y + h,
    			x + w, y + h,
    			x + w, y
            });
	}

	public Polygon topBoundaryRectangle() {
		int x = (int) (getX() + getWidth() / 5);
		int y = (int) (getY() + getHeight() / 2);
		int w = (int) (getWidth() * 3 / 5);
		int h = (int) (getHeight() / 2);
		return new Polygon(new float[] {
    			x, y,
    			x, y + h,
    			x + w, y + h,
    			x + w, y
            });
		
	}

	public Polygon bottomBoundaryRectangle() {
		int x = (int) (getX() + getWidth() / 5);
		int y = (int) (getY());
		int w = (int) (getWidth() * 3 / 5);
		int h = (int) (getHeight() / 2);
		return new Polygon(new float[] {
    			x, y,
    			x, y + h,
    			x + w, y + h,
    			x + w, y
            });
	}

	public ArrayList<Debuff> getDeBuffs() {
		return deBuffs;
	}

	public void setHealing(boolean healing) {
		this.healing = healing;
	}

	public Batch getPlayerHudBatch() {
		return playerHud.getBatch();
	}

	public float getPreviousX() {
		return previousX;
	}

	public float getPreviousY() {
		return previousY;
	}
	
	public void setKnockBack(int knockBack, float dir) {
		this.knockBack = knockBack;
		this.KBDir = dir;
	}
	
	
	public String getPlayerString() {
		return playerString;
	}

	public boolean remove() {
		spells = null;
		playerHud = null;
		return super.remove();
		
	}
	
	

}

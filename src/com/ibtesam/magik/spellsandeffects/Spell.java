package com.ibtesam.magik.spellsandeffects;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.blocks.Block;
import com.ibtesam.magik.enumerations.ActionType;
import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.RuneType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.puppets.BaseActor;
import com.ibtesam.magik.states.Play;

public class Spell {

	private float dir = 0;
	
	private BaseActor actor;
	private float targetX, targetY;
	private float x, y;
	private float velX, velY;
	private int w, h;
	private int effectTime;
	private int manaCost;
	private float damage;
	private int speed;
	private boolean configured;
	
	public ArrayList<Debuffs> deBuffs;
	
	private Rune rune1;
	private Rune rune2;
	private Player player;
	private Texture texture;
	
	private int timer = 0;
	
	private boolean iceshoot = false;
	private boolean storm = false;
	private boolean tickEffect = true;
	private boolean disableManaUse = false;
	
	public ArrayList<SpellObject> subSpells;
	public ArrayList<Block> blocks;
	
	private SpellType type = null;
	
	private ElementType ice = ElementType.ICE;
	private ElementType fire = ElementType.FIRE;
	private ElementType water = ElementType.WATER;
	private ElementType lightning = ElementType.LIGHTNING;
	private ElementType eNull = ElementType.NULL;
	

	public Spell(Rune rune1, Rune rune2, Player player, float targetX, float targetY) {
		this.rune1 = rune1;
		this.rune2 = rune2;
		this.player = player;
		this.targetX = targetX;
		this.targetY = targetY;
		actor = null;
		configured = false;
		deBuffs = new ArrayList<Debuffs>();
		subSpells =  new ArrayList<SpellObject>();
		blocks =  new ArrayList<Block>();
		assignSpell(rune1, rune2);
		
	}
	
	public Spell() {}

	public void tick(float dt){
		
		
		
		if(tickEffect)
			effectTime--;
		
		timer++;
		
		if(iceshoot && timer % 4 == 0 && timer < 100){
			
			x = player.getX() + player.getWidth()/2;
			y = player.getY() + player.getHeight()/2;

			//System.out.println("firing");
			Projectile tempProj = new Projectile(velX, velY, damage, w, h, (float) (x + Math.random()*50), (float) (y + Math.random()*50), texture, this, dir, ice);
			tempProj.effectTime = this.effectTime;
			subSpells.add(tempProj);
		}
		
		if(storm && timer % 10 == 0 && timer < 100){
			//System.out.println("firing");
			subSpells.add(new Targeted(damage, w, h, (float) (x + (Math.random()*500) - 250), (float) (y + Math.random()*500) - 250, texture, this, 30, lightning));
		}
		
		if(storm && timer % 20 == 0 && timer < 100){
			//System.out.println("firing");
			subSpells.add(new Targeted(damage, w, h, (float) (x + (Math.random()*100) - 50), (float) (y + Math.random()*100) - 50, texture, this, 30, lightning));
		}
		
		
		if(type == SpellType.WALL){
			if(effectTime <= 0){
				for (int i = 0; i < blocks.size(); i++) {
					blocks.get(i).delete();
				}
				blocks.removeAll(blocks);
			}
			if(blocks.size() == 0){
				delete();
			}
		}
		else if(type != null){
			if(subSpells.size() == 0){
				delete();
			}
			for(int i = 0; i < subSpells.size(); i++){
				subSpells.get(i).tick(dt);
			}
				
		}
		
	}
	
	public void render(){}
	
	private void assignSpell(Rune rune1, Rune rune2){
		if(rune1.geteType() == ElementType.FIRE && rune2.getaType() == ActionType.SHOOT){
			createFireShoot();
		}
		else if(rune1.geteType() == ElementType.WATER && rune2.getaType() == ActionType.SHOOT){
			createWaterShoot();
		}
		else if(rune1.geteType() == ElementType.LIGHTNING && rune2.getaType() == ActionType.SHOOT){
			createLightningShoot();
		}
		else if(rune1.geteType() == ElementType.ICE && rune2.getaType() == ActionType.SHOOT){
			createIceShoot();
		}
		else if(rune1.getaType() == ActionType.SHOOT && rune2.getType() == RuneType.NULL){
			createShoot();
		}
		//////////////////////////////////
		else if(rune1.geteType() == ElementType.FIRE && rune2.getaType() == ActionType.DASH){
			createFireDash();
		}
		else if(rune1.geteType() == ElementType.WATER && rune2.getaType() == ActionType.DASH){
			createWaterDash();
		}
		else if(rune1.geteType() == ElementType.LIGHTNING && rune2.getaType() == ActionType.DASH){
			createLightningDash();
		}
		else if(rune1.geteType() == ElementType.ICE && rune2.getaType() == ActionType.DASH){
			createIceDash();
		}
		/////////////////////////////////////
		else if(rune1.geteType() == ElementType.FIRE && rune2.getaType() == ActionType.SLASH){
			createFireSlash();
		}
		else if(rune1.geteType() == ElementType.WATER && rune2.getaType() == ActionType.SLASH){
			createWaterSlash();
		}
		else if(rune1.geteType() == ElementType.LIGHTNING && rune2.getaType() == ActionType.SLASH){
			createLightningSlash();
		}
		else if(rune1.geteType() == ElementType.ICE && rune2.getaType() == ActionType.SLASH){
			createIceSlash();
		}
		else if(rune1.getaType() == ActionType.SLASH && rune2.getType() == RuneType.NULL){
			createSlash();
		}
		//////////////////////////////////////
		else if(rune1.geteType() == ElementType.FIRE && rune2.getaType() == ActionType.SHIELD){
			createFireShield();
		}
		else if(rune1.geteType() == ElementType.WATER && rune2.getaType() == ActionType.SHIELD){
			createWaterShield();
		}
		else if(rune1.geteType() == ElementType.LIGHTNING && rune2.getaType() == ActionType.SHIELD){
			createLightningShield();
		}
		else if(rune1.geteType() == ElementType.ICE && rune2.getaType() == ActionType.SHIELD){
			createIceShield();
		}
		//////////////////////////////
		else if(rune1.geteType() == ElementType.FIRE && rune2.geteType() == ElementType.FIRE){
			createGrenade();
		}
		else if(rune1.geteType() == ElementType.WATER && rune2.geteType() == ElementType.WATER){
			createSplash();
		}
		else if(rune1.geteType() == ElementType.LIGHTNING && rune2.geteType() == ElementType.LIGHTNING){
			createFiveBolts();
		}
		else if(rune1.geteType() == ElementType.ICE && rune2.geteType() == ElementType.ICE){
			createMine();
		}
		////////////////////////////////////
		else if((rune1.geteType() == ElementType.FIRE && rune2.geteType() == ElementType.WATER) || (rune1.geteType() == ElementType.WATER && rune2.geteType() == ElementType.FIRE)){
			createSteam();
		}
		else if((rune1.geteType() == ElementType.FIRE && rune2.geteType() == ElementType.LIGHTNING) || (rune1.geteType() == ElementType.LIGHTNING && rune2.geteType() == ElementType.FIRE)){
			createShock();
		}
		else if((rune1.geteType() == ElementType.FIRE && rune2.geteType() == ElementType.ICE) || (rune1.geteType() == ElementType.ICE && rune2.geteType() == ElementType.FIRE)){
			createFrostBurn();
		}
		else if((rune1.geteType() == ElementType.WATER && rune2.geteType() == ElementType.LIGHTNING) || (rune1.geteType() == ElementType.LIGHTNING && rune2.geteType() == ElementType.WATER)){
			createRainStorm();
		}
		else if((rune1.geteType() == ElementType.WATER && rune2.geteType() == ElementType.ICE) || (rune1.geteType() == ElementType.ICE && rune2.geteType() == ElementType.WATER)){
			createIceburg();
		}
		else if((rune1.geteType() == ElementType.LIGHTNING && rune2.geteType() == ElementType.ICE) || (rune1.geteType() == ElementType.ICE && rune2.geteType() == ElementType.LIGHTNING)){
			createStuncicle();
		}
	}



	private void setActor(){
		if(configured){
			actor = new BaseActor(x, y, w, h, texture);
		}
		else{
			delete();
		}
		
	}
	
	public void delete(){
		
		if(actor != null)
			actor.remove();
		player.getSpells().remove(this);
		
	}

	public BaseActor getActor() {
		return actor;
	}

	public float getDamage() {
		return damage;
	}

	public Rune getRune1() {
		return rune1;
	}

	public Rune getRune2() {
		return rune2;
	}
	
	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}
	
	public SpellType getType() {
		return type;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	public ArrayList<Debuffs> getDebuffs() {
		return this.deBuffs;
	}

	private boolean subtractMana(int manaCost){
		if(disableManaUse){
			return false;
		}else{
			if(player.getMana() - manaCost >= 0){
				player.setMana(player.getMana() - manaCost);
				return true;
			}
			else{
				player.writeMessage("Insuffienct Mana", player.getX() + player.getWidth(), player.getY() + player.getHeight(), 50, Color.RED);
				return false;
			}
		}
	}
	
	
	private void createFireShoot(){
		damage = 10;
		manaCost = 10;
		w = 20;
		h = 10;
		type = SpellType.PROJECTILE;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		speed = 2000;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		velX = (float) (speed*Math.cos(dir));
		velY = (float) (speed*Math.sin(dir));
		deBuffs.add(Debuffs.BURN);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/fireRune.png") );
			subSpells.add(new Projectile(velX, velY, damage, w, h, x, y, texture, this, dir, fire));
			subSpells.get(0).setKnockBack(100);
		}
	}
	
	
	//MAKE SPELLS HERE
	private void createWaterShoot() {
		damage = 1f;
		manaCost = 10;
		w = 200;
		h = 10;
		effectTime = 100;
		type = SpellType.STREAM;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			subSpells.add( new Stream(effectTime, damage, w, h, x, y, texture, this, dir, water) );
		}
	}
	
	private void createLightningShoot() {
		damage = 5;
		manaCost = 0;
		w = 20;
		h = 20;
		type = SpellType.TARGET;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		//System.out.println("heading: " + Math.toDegrees(dir));
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/lightningRune.png") );
			for (int i = 0; i < 20; i++) {
				x = (float) (player.getCenterX()  + (Math.cos(dir)*(70 + i*20))) - w/2;
				y = (float) (player.getCenterY() + (Math.sin(dir)*(70 + i*20))) - h/2;
				subSpells.add(new Targeted(damage, w, h, x, y, texture, this, 10+(i*10), lightning));
			}
		}
	}
	
	private void createIceShoot(){
		damage = 1;
		manaCost = 30;
		w = 5;
		h = 5;
		effectTime = 50;
		type = SpellType.PROJECTILE;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		speed = 500;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		velX = (float) (speed*Math.cos(dir));
		velY = (float) (speed*Math.sin(dir));
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/fireRune.png") );
			subSpells.add(new Projectile(velX, velY, damage, w, h, (float) (x + Math.random()*20), (float) (y + Math.random()*20), texture, this, dir, ice));
			subSpells.get(0).effectTime = this.effectTime;
			iceshoot = true;
			tickEffect = false;
		}
	}
	
	
	private void createShoot() {
		damage = 1f;
		manaCost = 0;
		w = 7;
		h = 7;
		type = SpellType.PROJECTILE;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		speed = 2000;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		velX = (float) (speed*Math.cos(dir));
		velY = (float) (speed*Math.sin(dir));
		texture = new Texture(Gdx.files.internal("assets/player/nullRune.png") );
		subSpells.add(new Projectile(velX, velY, damage, w, h, x, y, texture, this, dir, eNull));
	}
	///////
	private void createFireDash() {
		player.performDash(200, 1500);
		damage = 0.05f;
		manaCost = 10;
		w = 200;
		h = 20;
		effectTime = 200;
		type = SpellType.TRAIL;
		x = player.getX();
		y = player.getY();
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		if(subtractMana(manaCost)){
			deBuffs.add(Debuffs.BURN);
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png") );
			subSpells.add( new Trail(effectTime, damage, w, h, x, y, texture, this, dir, fire) );
		}
	}
	
	private void createWaterDash() {
		manaCost = 30;
		if(subtractMana(manaCost)){
			player.performDash(400, 1500);
		}
	}
	
	private void createLightningDash() {
		manaCost = 30;
		if(subtractMana(manaCost)){
			player.performDash(300, 3000);
		}
	}
	
	private void createIceDash() {
		player.performDash(200, 1500);
		damage = 0.05f;
		manaCost = 10;
		w = 200;
		h = 20;
		effectTime = 200;
		type = SpellType.TRAIL;
		x = player.getX();
		y = player.getY();
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		if(subtractMana(manaCost)){
			deBuffs.add(Debuffs.SLOW);
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			subSpells.add(new Trail(effectTime, damage, w, h, x, y, texture, this, dir, ice) );
		}
	}
	
	private void createFireSlash() {
		damage = 10/30f;
		manaCost = 30;
		w = 5;
		h = 20;
		type = SpellType.STREAM;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX - player.getCenterX()));
		deBuffs.add(Debuffs.BURN);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			for (int i = 0; i < 30; i++) {
				subSpells.add( new Burst(damage, w, h, x, y, texture, this, dir - (i*0.027f), 50, fire) );
			}
		}
	}
	
	private void createWaterSlash() {
		damage = 15/30f;
		manaCost = 20;
		w = 10;
		h = 20;
		type = SpellType.STREAM;
		manaCost = 30;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			for (int i = 0; i < 30; i++) {
				subSpells.add( new Burst(damage, w, h, x, y, texture, this, dir - (i*0.027f), 50, water) );
			}
		}
	}
	
	private void createLightningSlash() {
		damage = 5/30f;
		manaCost = 20;
		w = 5;
		h = 20;
		type = SpellType.STREAM;
		manaCost = 30;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		deBuffs.add(Debuffs.STUN);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			for (int i = 0; i < 30; i++) {
				subSpells.add( new Burst(damage, w, h, x, y, texture, this, dir - (i*0.027f), 50, lightning) );
			}
		}
	}
	
	private void createIceSlash() {
		damage = 7/30f;
		manaCost = 20;
		w = 5;
		h = 20;
		type = SpellType.STREAM;
		manaCost = 30;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		deBuffs.add(Debuffs.SLOW);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			for (int i = 0; i < 30; i++) {
				subSpells.add( new Burst(damage, w, h, x, y, texture, this, dir - (i*0.027f), 50, ice) );
			}
		}
	}
	
	private void createSlash() {
		damage = 1/30f;
		w = 5;
		h = 20;
		type = SpellType.STREAM;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		texture = new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png") );
		for (int i = 0; i < 30; i++) {
			subSpells.add( new Burst(damage, w, h, x, y, texture, this, dir - (i*0.027f), 40, eNull) );
			//System.out.println(dir - (i*0.027f));
		}
		
	}
	////////////
	private void createFireShield() {
		damage = 0;
		manaCost = 50;
		w = 40;
		h = 40;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		effectTime = 1000;
		type = SpellType.WALL;
		if(subtractMana(manaCost)){
			for (int i = 0; i < 3; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), fire);
				sheild.setRadius(100, dir - (0.4f * i));
				blocks.add(sheild);
			}
			for (int i = 0; i < 3; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), fire);
				sheild.setRadius(100, dir + (0.4f * (i+1) ));
				blocks.add(sheild);
			}
		}
	}
	
	private void createWaterShield() {
		damage = 0;
		manaCost = 50;
		w = 40;
		h = 40;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		effectTime = 1000;
		type = SpellType.WALL;
		if(subtractMana(manaCost)){
			for (int i = 0; i < 16; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), water);
				sheild.setRadius(100, dir - (0.4f * i));
				blocks.add(sheild);
			}

		}
	}
	
	private void createLightningShield() {
		damage = 0;
		manaCost = 50;
		w = 10;
		h = 10;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		effectTime = 1000;
		type = SpellType.WALL;
		if(subtractMana(manaCost)){
			for (int i = 0; i < 8; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), lightning);
				sheild.setRadius(100, dir - (0.1f * i));
				blocks.add(sheild);
			}
			for (int i = 0; i < 8; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), lightning);
				sheild.setRadius(100, dir + (0.1f * (i+1) ));
				blocks.add(sheild);
			}
			for (int i = 0; i < 8; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), lightning);
				sheild.setRadius(110, dir - (0.1f * i));
				blocks.add(sheild);
			}
			for (int i = 0; i < 8; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), lightning);
				sheild.setRadius(110, dir + (0.1f * (i+1) ));
				blocks.add(sheild);
			}
			for (int i = 0; i < 8; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), lightning);
				sheild.setRadius(120, dir - (0.1f * i));
				blocks.add(sheild);
			}
			for (int i = 0; i < 8; i++) {
				Block sheild = new Block(x, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), lightning);
				sheild.setRadius(120, dir + (0.1f * (i+1) ));
				blocks.add(sheild);
			}
		}
	}
	
	private void createIceShield() {
		damage = 0;
		manaCost = 50;
		x = player.getCenterX();
		y = player.getCenterY() - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		effectTime = 1000;
		type = SpellType.WALL;
		if(subtractMana(manaCost)){
			for (int i = 0; i < 6; i++) {
				if(i % 2 == 0){
					w = 5;
					h = 40;
				}
				else{
					w = 40;
					h = 5;
				}
				Block sheild = new Block(x - w/2, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), ice);
				sheild.setRadius(100 - (10*i), dir);
				blocks.add(sheild);
				sheild = new Block(x - w/2, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), ice);
				sheild.setRadius(100 - (10*i), dir - (0.4f));
				blocks.add(sheild);
				sheild = new Block(x - w/2, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), ice);
				sheild.setRadius(100 - (10*i), dir + (0.4f));
				blocks.add(sheild);
				sheild = new Block(x - w/2, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), ice);
				sheild.setRadius(100 - (10*i), dir - (0.4f*2));
				blocks.add(sheild);
				sheild = new Block(x - w/2, y, w, h, new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png")), ice);
				sheild.setRadius(100 - (10*i), dir + (0.4f*2));
				blocks.add(sheild);
			}

		}
	}
	///////
	private void createGrenade() {
		damage = 30;
		manaCost = 30;
		w = 20;
		h = 20;
		type = SpellType.TARGET;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/fireRune.png") );
			subSpells.add(new AnimatedTargeted(damage, targetX, targetY, w, h, x, y, texture, this, 30, fire));
			((Targeted) subSpells.get(0)).setExpantion(150, 150);
		}
	}
	
	private void createSplash() {
		damage = 30;
		manaCost = 30;
		type = SpellType.TARGET;
		w = 100;
		h = 100;
		x = player.getX() - 20;
		y = player.getY() - 20;
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png") );
			//trails.add( new Burst(damage, w, h, x, y, x, y, texture, this, 0, water) );
			subSpells.add( new Targeted(damage, w, h, x, y, texture, this, 0, water) );
		}
	}
	
	private void createFiveBolts() {
		damage = 5;
		manaCost = 10;
		w = 20;
		h = 10;
		type = SpellType.PROJECTILE;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		speed = 2000;
		deBuffs.add(Debuffs.STUN);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/lightningRune.png") );
			for (int i = 0; i < 5; i++) {
				dir = (float) Math.atan2((Math.random()*Launcher.HEIGHT - player.getY() - player.getHeight()/2), (Math.random()*Launcher.WIDTH - player.getX() - player.getWidth()/2));
				velX = (float) (speed*Math.cos(dir));
				velY = (float) (speed*Math.sin(dir));
				subSpells.add(new Projectile(velX, velY, damage, w, h, x, y, texture, this, dir, lightning));
			}
			
		}
	}
	
	private void createMine() {
		damage = 20;
		manaCost = 30;
		w = 20;
		h = 20;
		type = SpellType.PROJECTILE;
		effectTime = 500;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		deBuffs.add(Debuffs.FREEZE);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/iceRune.png") );
			subSpells.add(new Projectile(0, 0, damage, w, h, x, y, texture, this, 0, ice));
			subSpells.get(0).effectTime = effectTime;
		}
	}
	////////
	private void createSteam() {
		damage = 30;
		manaCost = 10;
		w = 100;
		h = 40;
		type = SpellType.STREAM;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2 - h/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			subSpells.add( new Burst(damage, w, h, x, y, texture, this, dir, water) );
		}
	}
	
	private void createShock() {
		damage = 5;
		manaCost = 10;
		w = 500;
		h = 5;
		type = SpellType.STREAM;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		if(player.getMana() - manaCost >= 0){
			texture = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png") );
			subSpells.add( new Burst(damage, w, h, x, y, texture, this, dir, lightning) );
		}
	}
	
	private void createFrostBurn() {
		damage = 10;
		manaCost = 10;
		w = 20;
		h = 10;
		type = SpellType.PROJECTILE;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		speed = 2000;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		velX = (float) (speed*Math.cos(dir));
		velY = (float) (speed*Math.sin(dir));
		deBuffs.add(Debuffs.BURN);
		//deBuffs.add(Debuffs.SLOW);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/iceRune.png") );
			subSpells.add(new Projectile(velX, velY, damage, w, h, x, y, texture, this, dir, ice));
		}
	}
	
	private void createRainStorm() {
		damage = 10;
		manaCost = 40;
		w = 20;
		h = 20;
		type = SpellType.TARGET;
		x = player.getCenterX();
		y = player.getCenterY();
		deBuffs.add(Debuffs.STUN);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/lightningRune.png") );
			subSpells.add(new Targeted(damage, w, h, (float) (x + (Math.random()*100) - 50), (float) (y + Math.random()*100) - 50, texture, this, 30, water));
			storm = true;
			tickEffect = false;
		}
	}
	
	private void createIceburg(){
		damage = 0;
		manaCost = 50;
		w = 200;
		h = 200;
		speed = 0;
		effectTime = 200;
		type = SpellType.WALL;
		if(subtractMana(manaCost)){
			Block iceberg = new Block(targetX, targetY, w, h);
			blocks.add(iceberg);
		}
	}
	
	private void createStuncicle() {
		damage = 10;
		manaCost = 10;
		w = 20;
		h = 10;
		type = SpellType.PROJECTILE;
		x = player.getX() + player.getWidth()/2;
		y = player.getY() + player.getHeight()/2;
		speed = 2000;
		dir = (float) Math.atan2((targetY - player.getCenterY()), (targetX -  player.getCenterX()));
		velX = (float) (speed*Math.cos(dir));
		velY = (float) (speed*Math.sin(dir));
		deBuffs.add(Debuffs.STUN);
		deBuffs.add(Debuffs.SLOW);
		if(subtractMana(manaCost)){
			texture = new Texture(Gdx.files.internal("assets/player/iceRune.png") );
			subSpells.add(new Projectile(velX, velY, damage, w, h, x, y, texture, this, dir, ice));
		}
	}


	
	

}

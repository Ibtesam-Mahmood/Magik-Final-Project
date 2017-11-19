package com.ibtesam.magik.blocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.enumerations.BlockType;
import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.spellsandeffects.Debuff;

public class Drop extends Block{
	


	public enum pickUpType{
		HEAL,
		MANA,
		SPEED,
		POINTER;
		
		private static final List<pickUpType> VALUES = Arrays.asList(values());
		private static final Random RANDOM = new Random();
		
		public static pickUpType randomType()  {
			pickUpType temp = POINTER;
			while(temp == POINTER){
				temp = VALUES.get(RANDOM.nextInt(VALUES.size()));
			}
			return temp;
		}
	}

	private static final float heal = 10;
	private static final float restore = 40;
	private pickUpType effectType; // if false, heal. if true, gain mana.
	
	private static final Texture healthTexture =  new Texture(Gdx.files.internal("assets/blocks/healthPack.png"));
	private static final Texture manaTexture = new Texture(Gdx.files.internal("assets/blocks/manaPack.png"));
	private static final Texture speedTexture = new Texture(Gdx.files.internal("assets/blocks/speedPack.png"));
	private static final Texture pointerTexture = new Texture(Gdx.files.internal("assets/blocks/pointer.png"));
	
	public Drop(float x, float y, pickUpType effectType) {
		super(x - healthTexture.getWidth()/8, y - healthTexture.getHeight()/2, healthTexture.getWidth()/4, healthTexture.getHeight());
		this.effectType = effectType;
		if(effectType == pickUpType.HEAL)
			setAnimations(healthTexture, 200, 4, 1);
		else if(effectType == pickUpType.MANA)
			setAnimations(manaTexture, 200, 4, 1);
		else if(effectType == pickUpType.SPEED)
			setAnimations(speedTexture, 200, 4, 1);
		else if(effectType == pickUpType.POINTER)
			setAnimations(pointerTexture, 200, 4, 1);
		type = BlockType.PICKUP;
	}
	
	public void act(float dt){
		setX(Game.clamp(getX(), 0, Launcher.WIDTH));
		setY(Game.clamp(getY(), 0, Launcher.HEIGHT));
	}
	
	public void render(){}

	public void excecuteEffect(Player player) {
		if(effectType == pickUpType.HEAL)
			player.setHealth(player.getHealth() + heal);
		else if(effectType == pickUpType.MANA)
			player.setMana(player.getMana() + restore);
		else if(effectType == pickUpType.SPEED)
			player.getDeBuffs().add( new Debuff(player, Debuffs.SPEED) );

	}

}

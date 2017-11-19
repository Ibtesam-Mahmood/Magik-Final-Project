package com.ibtesam.magik.spellsandeffects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.blocks.Block;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.states.Play;

public class Targeted extends SpellObject{

	protected int delay;
	protected float baseDamage;
	protected Texture baseTexture;
	protected boolean doesDamage = false;
	protected boolean delaying = true;
	protected boolean resizing = true;
	protected float expandX = 0, expandY = 0;
	protected int range = 50;
	protected int baseDelay;
	protected float baseX, baseY;

	public Targeted(float damage, int w, int h, float x, float y, Texture t,
			Spell spell, int delay, ElementType eType) {
		super(0, w, h, x, y, new Texture(Gdx.files.internal("assets/player/nullRune.png")), spell, eType);
		this.delay = delay;
		this.baseW = w;
		this.baseH = h;
		this.baseDamage = damage;
		this.baseTexture = t;
		type = SpellType.TARGET;
		effectTime = 10;
		baseDelay = delay;
		baseX = x;
		baseY = y;
	}
	

	protected void tick(float dt) {
		
		if(delay == 0){
			damage = baseDamage;
			setX(baseX);
			setY(baseY);
			setWidth(baseW);
			setHeight(baseH);
			setTexture(baseTexture);
			if(expandX != 0){
				setWidth(expandX);
				setX(baseX + baseW/2 - getWidth()/2);
			}
			if(expandY != 0){
				setHeight(expandY);
				setY(baseY + baseH/2 - getHeight()/2);
			}
			resizing = false;
		}
		
		if(effectTime == 0)
			delete();
		else if(effectTime == 7)
			doesDamage = true;
		
		if(delay == -1){
			effectTime--;
			delaying = false;
		}
		else if(delaying){
			delay--;
		}
		
		if(resizing && delaying){
			setWidth(baseW * delay/baseDelay);
			setHeight(baseH * delay/baseDelay);
			setX(baseX + getBaseW()/2 - getWidth()/2);
			setY(baseY + getBaseH()/2 - getHeight()/2);
		}
		
	}

	protected void render() {
		
	}

	public boolean doesDamage() {
		return doesDamage;
	}
	
	public void setExpantion(float expandX, float expandY) {
		this.expandX = expandX;
		this.expandY = expandY;
	}
	
	public void setRange(int range) {
		this.range = range;
	}


	@Override
	public void playerCollision(Player targetPlayer) {
		if(this.doesDamage()){
			targetPlayer.setHealth(targetPlayer.getHealth() - this.getDamage());
			for (int k = 0; k < this.getDebuffs().size(); k++) {
				if(targetPlayer.isDebuff(this.getDebuffs().get(k)) == false){
					targetPlayer.getDeBuffs().add( new Debuff(targetPlayer, this.getDebuffs().get(k)) );
					targetPlayer.setDebuffSwitch(this.getDebuffs().get(k), true);
				}
			}
			this.delete();
		}
		else{
			this.setRange(0);
		}
		
	}


	@Override
	public void blockCollision(Block block, ArrayList<Block> blocks) {
		if(this.doesDamage()){
			if(Play.getOppositeEType(this.geteType()) == block.geteType()){
				block.delete();
				blocks.remove(block);
			}
			this.delete();
		}
		else{
			this.setRange(0);
		}
	}
	

}

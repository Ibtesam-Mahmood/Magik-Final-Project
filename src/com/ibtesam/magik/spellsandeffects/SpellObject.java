package com.ibtesam.magik.spellsandeffects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.blocks.Block;
import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.puppets.BaseActor;

public abstract class SpellObject extends BaseActor{

	

	protected float damage;
	protected float dir;
	protected int knockBack;
	protected float baseW;
	protected float baseH;
	
	protected Player player;
	
	protected int effectTime;
		
	protected ArrayList<Debuffs> debuffs;
	
	public SpellType type;
	protected Spell spell;
	
	private ElementType eType;

	protected SpellObject(int effectTime, float damage, int w, int h, float x, float y,
			Texture texture, Spell spell, float dir, ElementType eType) {
		super(x, y, w, h, texture, dir);
		this.effectTime = effectTime;
		this.damage = damage;
		this.baseH = h;
		this.baseW = w;
		this.player = spell.getPlayer();
		this.debuffs = spell.getDebuffs();
		this.spell = spell;
		this.eType = eType;
	}
	
	protected SpellObject(float velX, float velY, float damage, int w, int h, float x, 
			float y, Texture texture, Spell spell, float dir, ElementType eType) {
		super(x, y, w, h, texture, dir);
		this.velocityX = velX;
		this.velocityY = velY;
		this.damage = damage;
		this.player = spell.getPlayer(); 
		this.debuffs = spell.getDebuffs();
		this.spell = spell;
		this.effectTime = -1;
		this.eType = eType;
	}
	
	protected SpellObject(float damage, int w, int h, float x, float y,
			Texture texture, Spell spell, ElementType eType) {
		super(x, y, w, h, texture);
		this.damage = damage;
		this.player = spell.getPlayer();
		this.debuffs = spell.getDebuffs();
		this.spell = spell;
		this.rotation = 0;
		this.eType = eType;
	}
	
	protected abstract void tick(float dt);
	protected abstract void render();
	public abstract void playerCollision(Player targetPlayer);
	public abstract void blockCollision(Block block, ArrayList<Block> blocks);
	

	public float getDamage() {
		return damage;
	}


	public void delete() {
		
		if(this.type == SpellType.TRAIL || this.type == SpellType.STREAM){
			spell.subSpells.remove(this);
		}
		else if(this.type == SpellType.PROJECTILE)
			spell.subSpells.remove(this);
		else if(this.type == SpellType.TARGET)
			spell.subSpells.remove(this);
		remove();
		
	}

	public float getBaseW() {
		return baseW;
	}

	public float getBaseH() {
		return baseH;
	}

	public SpellType getType() {
		return type;
	}
	

	public ArrayList<Debuffs> getDebuffs() {
		return debuffs;
	}

	public ElementType geteType() {
		return eType;
	}
	
	public int getKnockBack() {
		return this.knockBack;
	}
	
	public float getDir() {
		return dir;
	}
	
	public void setKnockBack(int knockBack) {
		this.knockBack = knockBack;
	}
	
	
	
	

}

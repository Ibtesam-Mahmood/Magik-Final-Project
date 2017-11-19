package com.ibtesam.magik.spellsandeffects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.blocks.Block;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.states.Play;

public class Trail extends SpellObject {
	
	protected boolean setOrigin = true;
	
	public Trail(int effectTime, float damage, int w, int h, float x, float y,
			Texture texture, Spell spell, float dir, ElementType eType) {
		super(effectTime, damage, w, h, x, y, texture, spell, dir, eType);
		type = SpellType.TRAIL;
	}
	
	public void tick(float dt) {
		
		
		if(effectTime == 0)
			delete();
		
		effectTime--;
		
	}
	
	public void render(){
		
	}

	public boolean isSetOrigin() {
		return setOrigin;
	}

	
	public void playerCollision(Player targetPlayer) {
		if(effectTime % 10 == 0){
			targetPlayer.setHealth(targetPlayer.getHealth() - this.getDamage());
			for (int k = 0; k < this.getDebuffs().size(); k++) {
				if(targetPlayer.isDebuff(this.getDebuffs().get(k)) == false){
					targetPlayer.getDeBuffs().add( new Debuff(targetPlayer, this.getDebuffs().get(k)) );
					targetPlayer.setDebuffSwitch(this.getDebuffs().get(k), true);
				}
			}
		}
		if(this.getType() == SpellType.STREAM)
			this.setWidth( (float) Math.hypot( (this.getX() - targetPlayer.getCenterX()), ( this.getCenterY() - targetPlayer.getCenterY() ) ) );
		
		
	}

	@Override
	public void blockCollision(Block block, ArrayList<Block> blocks) {
		if(this.getType() == SpellType.STREAM){
			this.setWidth( (float) Math.hypot( (this.getX() - block.getCenterX()), ( this.getCenterY() - block.getCenterY() ) ) );
			System.out.println(Math.hypot( (this.getX() - block.getCenterX()), ( this.getY() - block.getCenterY() )) );
		}
		if(Play.getOppositeEType(this.geteType()) == block.geteType()){
			block.delete();
			blocks.remove(block);
		}
	}
	
	
	

}

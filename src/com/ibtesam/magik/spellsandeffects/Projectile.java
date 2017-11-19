package com.ibtesam.magik.spellsandeffects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.blocks.Block;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.states.Play;

public class Projectile extends SpellObject{


	public Projectile(float velX, float velY, float damage, int w, int h, float x,
			float y, Texture t, Spell spell, float rotation, ElementType eType) {
		super(velX, velY, damage, w, h, x, y, t, spell, rotation, eType);
		type = SpellType.PROJECTILE;
		setVelocity(velX, velY);
	}
	

	public void tick(float dt) {
	
		if(effectTime != -1){
			effectTime--;
			if(effectTime == 0){
				delete();
			}
			
		}
		
	}
	
	public void render(){
		
	}


	public void playerCollision(Player targetPlayer) {
		//targetPlayer.setKnockBack(tempProj.getKnockBack(), tempProj.getDir());
		targetPlayer.setHealth(targetPlayer.getHealth() - this.getDamage());
		for (int k = 0; k < this.getDebuffs().size(); k++) {
			if(targetPlayer.isDebuff(this.getDebuffs().get(k)) == false){
				targetPlayer.getDeBuffs().add( new Debuff(targetPlayer, this.getDebuffs().get(k)) );
				targetPlayer.setDebuffSwitch(this.getDebuffs().get(k), true);
			}
		}
		this.delete();
	}


	public void blockCollision(Block block, ArrayList<Block> blocks) {
		if(Play.getOppositeEType(this.geteType()) == block.geteType()){
			block.delete();
			blocks.remove(block);
		}
		this.delete();
	}


	
	

}

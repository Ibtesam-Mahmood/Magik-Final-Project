package com.ibtesam.magik.spellsandeffects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.player.Player;

public class Burst extends Stream {
	
	float radius;

	public Burst(float damage, int w, int h, float x, float y, Texture texture,
			Spell spell, float dir, float radius, ElementType eType) {
		super(2, damage, w, h, x, y, texture, spell, dir, eType);
		type = SpellType.STREAM;
		setOrigin = false;
		setRadius(radius, dir);
		this.radius = radius;
	}
	
	public Burst(float damage, int w, int h, float x, float y, Texture texture,
			Spell spell, float dir, ElementType eType) {
		super(5, damage, w, h, x, y, texture, spell, dir, eType);
		type = SpellType.STREAM;
		setOrigin = true;
	}
	
	public void tick(float dt) {
		//this.setWidth(this.getBaseW());
		
		super.tick(dt);
		
		if(setOrigin == false){
			setPosition((float) (player.getCenterX() + radius*Math.cos(rotation)), (float) (player.getCenterY() + (radius*8/7)*Math.sin(rotation))  - radius/7 );
		}
		
		
		//System.out.println("check");
		

	}
	
	public void render() {
		// TODO Auto-generated method stub
		super.render();
	}
	
	public void setWidth(float w) {
		//System.out.print(setOrigin + " : ");
		if(setOrigin == false){
			super.setWidth(w);
			//System.out.println("that");
		}
		if(w != baseW)
			damage = 0;
	}
	
	public void playerCollision(Player targetPlayer) {
			targetPlayer.setHealth(targetPlayer.getHealth() - this.getDamage());
			for (int k = 0; k < this.getDebuffs().size(); k++) {
				if(targetPlayer.isDebuff(this.getDebuffs().get(k)) == false){
					targetPlayer.getDeBuffs().add( new Debuff(targetPlayer, this.getDebuffs().get(k)) );
					targetPlayer.setDebuffSwitch(this.getDebuffs().get(k), true);
				}
			}
		if(this.getType() == SpellType.STREAM){
			float hyp = (float) Math.hypot( (this.getX() - targetPlayer.getCenterX()), ( this.getCenterY() - targetPlayer.getCenterY() ) );
			if(hyp < getBaseW()){
				this.setWidth( hyp );
			}
			else{
				damage = 0;
			}
		}
		
		
	}

}

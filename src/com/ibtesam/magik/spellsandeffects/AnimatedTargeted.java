package com.ibtesam.magik.spellsandeffects;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.player.Player;

public class AnimatedTargeted extends Targeted{

	private float targetX, targetY;
	
	public AnimatedTargeted(float damage, float targetX, float targetY, 
			int w, int h, float x, float y,	Texture t, Spell spell, int delay, ElementType eType) {
		super(damage, w, h, x, y, t, spell, delay, eType);
		this.targetX = targetX;
		this.targetY = targetY;
		short speed = 500;
		this.dir = (float) Math.atan2((targetY - player.getY() - player.getHeight()/2), (targetX - player.getX() - player.getWidth()/2));
		this.velocityX = (float) (speed*Math.cos(dir));
		this.velocityY = (float) (speed*Math.sin(dir));
		setVelocity(velocityX, velocityY);
		delaying = false;
	}
	
	protected void tick(float dt) {
		super.tick(dt);
		
		if(range == 0){
			velocityX = 0;
			velocityY = 0;
			setVelocity(velocityX, velocityY);
			delaying = true;
		}
		else if(delaying == false){
			baseX = getX();
			baseY = getY();
		}
		
		if(range != 0)
			range--;
		
	}
	
	protected void render() {
		
		
	}


	

}

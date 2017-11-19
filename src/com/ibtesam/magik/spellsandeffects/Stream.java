package com.ibtesam.magik.spellsandeffects;

import com.badlogic.gdx.graphics.Texture;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.main.Game;

public class Stream extends Trail {
	


	public Stream(int effectTime, float damage, int w, int h, float x, float y,
			Texture texture, Spell spell, float dir, ElementType eType) {
		super(effectTime, damage, w, h, x, y, texture, spell, dir, eType);
		type = SpellType.STREAM;
	}
	
	public void tick(float dt) {
		
		
		super.tick(dt);
		
		//this.setWidth(this.getBaseW());
		
		if(setOrigin){
			this.setX(player.getX() + player.getWidth()/2);
			this.setY(player.getY() + player.getHeight()/2 - getHeight()/2);
		}
		
		
		setWidth(Game.clamp(getWidth(), 0, baseW));
		
	}
	
	public void render(){
		
	}



	
	

}

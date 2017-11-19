package com.ibtesam.magik.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor{

	protected String message;
	protected Color c;
	protected float duration;
	protected float baseDuration;
	
	private BitmapFont font = new BitmapFont();

	public Text(String message, float x, float y, float duration, Color c) {
		this(message, x, y, duration);
		this.c = c;
	}
	
	public Text(String message, float x, float y, float duration) {
		this.message = message;
		this.duration = (float) Math.floor(duration);
		this.baseDuration = (float) Math.floor(duration);
		this.c = new Color(Color.BLACK);
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, message);
		setPosition(x - glyphLayout.width/2, y - glyphLayout.height/2);
	}
	
	public void act(float dt){
		
		duration--;
		
		c.set(c.r, c.g, c.b, duration/baseDuration);
		setY(getY() + 0.4f);
		
		if(duration == 0)
			remove();
		
	}
	
	public void draw(Batch batch, float parentAlpha){
		
		font.setColor(c.r, c.g, c.b, c.a);
		font.draw(batch, message, getX(), getY());
	}

}

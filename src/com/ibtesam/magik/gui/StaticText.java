package com.ibtesam.magik.gui;

import com.badlogic.gdx.graphics.Color;

public class StaticText extends Text{

	public StaticText(String message, float x, float y) {
		super(message, x, y, 0);
	}
	
	public StaticText(String message, float x, float y, Color c) {
		super(message, x, y, 0, c);
	}
	
	public void act(float dt){}

}

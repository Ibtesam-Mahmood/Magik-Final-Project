package com.ibtesam.magik.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ibtesam.magik.main.Game;

public class Dummy extends Player {
	
	private static final Texture dummyHit = new Texture(Gdx.files.internal("assets/player/dummy_rocking.png"));
	private boolean hit = false;
	private int speed = 500;

	public Dummy(Game game, float x, float y) {
		super(game, x, y);
		playerImageString = "dummy";
		playerTexture = new Texture(Gdx.files.internal("assets/player/" + playerImageString + ".png"));
		setTexture(playerTexture);
	}

	@Override
	public void act(float dt) {
//		if(health < 50)
//			health = PLAYERHEALTH;
		
		playerTick(dt); 
		
		if(hit && count % 110 == 0){
			hit = false;
		}
//		
//		if(hit){
//			animationR.setFrameDuration(animationR.getFrameDuration()*0.002f);
//		}
		
		if(healthBack != health){
			setAnimations(dummyHit, speed, 4, 1);
			hit = true;
		}
		
		if(!hit){
			System.out.println("called");
			setTexture(playerTexture);
		}
		

	}

	@Override
	public void render() {
		
	}

}

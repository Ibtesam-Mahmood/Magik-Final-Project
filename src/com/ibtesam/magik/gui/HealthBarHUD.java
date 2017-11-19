package com.ibtesam.magik.gui;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.puppets.BaseActor;

public class HealthBarHUD extends BaseActor{

	
	private Player player;
	private int playerIndex;
	private ArrayList<Actor> HUDactors;

	public HealthBarHUD(Player player, int playerIndex) {
		this.player = player;
		this.playerIndex = playerIndex;
		HUDactors = initializeHUDActors();
		HUDactors = setHUDPositions(HUDactors);
	}

	public void act(float arg0) {
		
		if(player.getHealth() == 0){
			this.remove();
		}
		
		HUDactors.get(0).setScale((float) player.getMana()/Player.PLAYERMANA, 1);
		HUDactors.get(2).setScale((float) player.getHealthBack()/Player.PLAYERHEALTH, 1);
		HUDactors.get(3).setScale((float) player.getHealth()/Player.PLAYERHEALTH, 1);
		
		
	}
	
	public void draw(Batch batch, float parentAlpha) {
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);
		for (Actor actor : HUDactors) {
			actor.draw(batch, parentAlpha);
		}
		
	}
	
	private ArrayList<Actor> setHUDPositions(ArrayList<Actor> actors) {
		if(playerIndex == 0){
			actors.get(0).setPosition(10, Launcher.HEIGHT - 27);
			actors.get(1).setPosition(10, Launcher.HEIGHT - 27);
			actors.get(2).setPosition(10, Launcher.HEIGHT - 20);
			actors.get(3).setPosition(10, Launcher.HEIGHT - 20);
			actors.get(4).setPosition(10, Launcher.HEIGHT - 20);
		}
		if(playerIndex == 1){
			actors.get(0).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 27);
			actors.get(1).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 27);
			actors.get(2).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 20);
			actors.get(3).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 20);
			actors.get(4).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 20);
		}
		
		return actors;
	}

	private ArrayList<Actor> initializeHUDActors() {
		
		Texture t;
		
		ArrayList<Actor> HUDActors = new ArrayList<Actor>();
		
		t = new Texture(Gdx.files.internal("assets/HUD_bars/manaBar.png"));
		Actor manaBar =  new BaseActor(0, 0, 365, 7, t);
		HUDActors.add(manaBar);
		
		t = new Texture(Gdx.files.internal("assets/HUD_bars/manaBarOutline.png"));
		Actor manaBarOutline =  new BaseActor(0, 0, 365, 7, t);
		HUDActors.add(manaBarOutline);
		
		t = new Texture(Gdx.files.internal("assets/HUD_bars/healthBarBackground.png"));
		Actor healthBarBack = new BaseActor(0, 0, 422, 15, t);
		HUDActors.add(healthBarBack);
		
		t = new Texture(Gdx.files.internal("assets/HUD_bars/healthBar.png"));
		Actor healthBar = new BaseActor(0, 0, 422, 15, t);
		HUDActors.add(healthBar);
		
		t = new Texture(Gdx.files.internal("assets/HUD_bars/healthBarOutline.png"));
		Actor healthBarOutline = new BaseActor(0, 0, 422, 15, t);
		HUDActors.add(healthBarOutline);
		
		return HUDActors;
		
		
	}
}

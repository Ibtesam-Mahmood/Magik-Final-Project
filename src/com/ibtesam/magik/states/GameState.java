package com.ibtesam.magik.states;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.states.handler.GameStateManager;

public abstract class GameState {

	protected GameStateManager manager;
	protected Game game;
	

	
	protected GameState(GameStateManager manager){
		this.manager = manager;
		this.game = manager.getGame();

	}
	

	public abstract void tick(float dt);
	public abstract void render();

	
}

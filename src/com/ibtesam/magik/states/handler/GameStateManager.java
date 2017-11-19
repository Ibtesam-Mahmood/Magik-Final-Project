package com.ibtesam.magik.states.handler;

import java.util.Stack;

import com.ibtesam.magik.enumerations.State;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.states.*;

public class GameStateManager {

	private Game game;
	
	private Stack<GameState> gameStates;
	
	private static State currentState;
	
	private int count = 0;
	
	public GameStateManager(Game game){
		currentState = State.MAINMENU;
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(currentState);
		
	}
	
	public void tick(float dt){
		if(count == 0)
			gameStates.peek().tick(dt);
		else
			count--;
	}
	
	@SuppressWarnings("unchecked")
	public void render(){
		Stack<GameState> tempStates = (Stack<GameState>) gameStates.clone();
		Stack<GameState> tempStatesFlip = new Stack<GameState>();
		
		//System.out.println(tempStates.size());
		while(!(tempStates.size() == 0)){
			tempStatesFlip.push(tempStates.pop());
		}
		int size = tempStatesFlip.size();
		for (int i = 0; i < size; i++) {
			tempStatesFlip.peek().render();
			tempStatesFlip.pop();
		}
	}

	public Game getGame() {
		return game;
	}
	
	private GameState getGameState(State state){
		switch(state){
		case MAINMENU: 
			return new MainMenu(this);
		case PLAY:
			return new Play(this);
		case TRAINING:
			return new Training(this);
		case TUTORIAL:
			return new Tutorial(this);
		case TUTORIAL_DICLAIMER:
			return new FirstTime(this);
		case PAUSE:
			return new Pause(this);
		default:
			return null;
			
		}
			
	}
	
	public void setState(State state){
		
		int size = gameStates.size();
		for (int i = 0; i < size; i++) {
			popState();
		}
		pushState(state);
		
	}

	public void popState() {
		gameStates.pop();
		count = 10;
	}

	public void pushState(State state) {
		gameStates.push(getGameState(state));
		count = 10;
	}

	public GameState getCurrentState() {
		return gameStates.peek();
	}
	
	
		
	
	
	
	
}

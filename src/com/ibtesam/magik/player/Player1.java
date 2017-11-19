package com.ibtesam.magik.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ibtesam.magik.enumerations.ActionType;
import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.RuneType;
import com.ibtesam.magik.enumerations.State;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.puppets.BaseActor;
import com.ibtesam.magik.spellsandeffects.Debuff;
import com.ibtesam.magik.spellsandeffects.Rune;

public class Player1 extends Player {

	
	public Player1(Game game, int x, int y) {
		super(game, x, y);
		playerImageString = "playerSheet";
		playerTexture = new Texture(Gdx.files.internal("assets/player/" + playerImageString + ".png"));
		playerTextureOpage = new Texture(Gdx.files.internal("assets/player/" + playerImageString + "_transparent.png"));
		setAnimations(playerTexture, speed, 4, 3);
		playerString = "player 1";
	}

	public void act(float dt) {
		if(!dashing && canMove)
			moveTo();
		if(canRune || canAction)
			pressKey();
		if(canCast)
			cast();
		
		playerTick(dt);
		
		cursorRotation = (float) (Math.atan2((Launcher.HEIGHT - Gdx.input.getY() - getCenterY()), (Gdx.input.getX() - getCenterX())));
		
		moveBy(velocityX*dt, velocityY*dt);
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			game.getGSM().pushState(State.PAUSE);
		}
		
	}
	
	public void render() {
		
		playerHud.draw();
		for(int i = 0; i < spells.size(); i++){
			spells.get(i).render();
		}
		
	}
	public void cast(){
		if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
			if(rune1.getType() != RuneType.NULL && rune2.getType() != RuneType.NULL){
				conjure(rune1, rune2, cursorRotation);
				updateRuneTexture();
				//System.out.println("conjured");
			}
		}
	}
	
	private void moveTo(){
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			targetX = (int) Gdx.input.getX();
			targetY = (int) Launcher.HEIGHT - Gdx.input.getY();
		}
		

	}
	
	
	
	private void pressKey() {
		if(Gdx.input.isKeyJustPressed(Keys.Q) && canRune){
			addRune(ElementType.FIRE);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.W) && canRune){
			addRune(ElementType.WATER);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.E) && canRune){
			addRune(ElementType.LIGHTNING);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.R) && canRune){
			addRune(ElementType.ICE);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.A) && canAction){
			addRune(ActionType.DASH);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.S) && canAction){
			addRune(ActionType.SLASH);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.D) && canAction){
			addRune(ActionType.SHOOT);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.F) && canAction){
			addRune(ActionType.SHIELD);
			updateRuneTexture();
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			rune1 = new Rune();
			rune2 = new Rune();
			updateRuneTexture();
		}
	}
	

	

}

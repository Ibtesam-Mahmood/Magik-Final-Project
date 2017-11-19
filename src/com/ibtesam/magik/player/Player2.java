package com.ibtesam.magik.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.ibtesam.magik.enumerations.ActionType;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.RuneType;
import com.ibtesam.magik.enumerations.State;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.spellsandeffects.Rune;
import com.ibtesam.magik.spellsandeffects.Spell;

public class Player2 extends Player{

	Controller controller;
	boolean a = false;
	boolean b = false;
	boolean x = false;
	boolean y = false;
	boolean lb = false;
	boolean rb = false;
	boolean ljb = false;
	boolean rt = false;
	boolean lt = false;
	
	ControllerListener controllerListener = new ControllerListener() {
		public int indexOf (Controller controller) {
			return Controllers.getControllers().indexOf(controller, true);
		}
		public void connected (Controller controller) {}

		@Override
		public void disconnected (Controller controller) {
			//print("disconnected " + controller.getName());
			int i = 0;
			for (Controller c : Controllers.getControllers()) {
				print("#" + i++ + ": " + c.getName());
			}
			if (Controllers.getControllers().size == 0) print("No controllers attached");
		}

		@Override
		public boolean buttonDown (Controller controller, int buttonIndex) {
		//	print("#" + indexOf(controller) + ", button " + buttonIndex + " down");
			if(buttonIndex == 0)
				a = true;
			if(buttonIndex == 1)
				b = true;
			if(buttonIndex == 2)
				x = true;
			if(buttonIndex == 3)
				y = true;
			if(buttonIndex == 4)
				lb = true;
			if(buttonIndex == 5)
				rb = true;
			if(buttonIndex == 8)
				ljb = true;
			return false;
		}

		@Override
		public boolean buttonUp (Controller controller, int buttonIndex) {
			//print("#" + indexOf(controller) + ", button " + buttonIndex + " up");
			if(buttonIndex == 0)
				a = false;
			if(buttonIndex == 1)
				b = false;
			if(buttonIndex == 2)
				x = false;
			if(buttonIndex == 3)
				y = false;
			if(buttonIndex == 4)
				lb = false;
			if(buttonIndex == 5)
				rb = false;
			if(buttonIndex == 8)
				ljb = false;
			return false;
		}

		@Override
		public boolean axisMoved (Controller controller, int axisIndex, float value) {
			//print("#" + indexOf(controller) + ", axis " + axisIndex + ": " + value);
			if(controller.getAxis(4) > 0.995)
				lt = true;
			else if(controller.getAxis(4) < -0.995)
				rt = true;
			else{
				lt = false;
				rt = false;
			}
			return false;
		}

		@Override
		public boolean povMoved (Controller controller, int povIndex, PovDirection value) {
			//print("#" + indexOf(controller) + ", pov " + povIndex + ": " + value);
			return false;
		}

		@Override
		public boolean xSliderMoved (Controller controller, int sliderIndex, boolean value) {
		//	print("#" + indexOf(controller) + ", x slider " + sliderIndex + ": " + value);
			return false;
		}

		@Override
		public boolean ySliderMoved (Controller controller, int sliderIndex, boolean value) {
		//	print("#" + indexOf(controller) + ", y slider " + sliderIndex + ": " + value);
			return false;
		}

		@Override
		public boolean accelerometerMoved (Controller controller, int accelerometerIndex, Vector3 value) {
			// not printing this as we get to many values
			return false;
		}
	};
	
	public Player2(Game game, int x, int y) {
		super(game, x, y);
		playerImageString = "player2Sheet";
		playerTexture = new Texture(Gdx.files.internal("assets/player/" + playerImageString + ".png"));
		playerTextureOpage = new Texture(Gdx.files.internal("assets/player/" + playerImageString + "_transparent.png"));
		setAnimations(playerTexture, speed, 4, 3);
		count = 470;
		boolean configured  = false;
//		while(!configured){
//			try{
//				//System.out.println(Controllers.getControllers().first().getName());
//				if(Controllers.getControllers().first().getName() != null){
//					System.out.println("working");
//					configured = true;
//				}
//			}catch(IllegalStateException ex){
//				//configured = false;
//				System.out.println("controller not connected");
//			}
//		}
		controller = Controllers.getControllers().first();
		Controllers.addListener(controllerListener);
		key = false;
		playerString = "player 2";
	}
	
	void print (String message) {
		System.out.println(message);
	}

	public void act(float dt) {
	
		playerTick(dt);
		
		controllerInput();
		
		if(canCast){
			pressKey();
			cast();
		}
		
		moveBy(velocityX*dt, velocityY*dt);
		
		if(controller.getButton(7)){
			game.getGSM().pushState(State.PAUSE);
		}
		
//		if(count % 300 == 0)
//			conjure(new Rune(ElementType.WATER), new Rune(ActionType.SHIELD), cursorRotation);
		
		//System.out.println(velocityY);
		

		
		//System.out.println(velocityY);
		

		
		
	}
	
	private void controllerInput() {
		PovDirection pov = controller.getPov(0);
		float px = 0, py = 0;
		
		if(!dashing){
			if(controller.getAxis(0) > 0.1 || controller.getAxis(0) < -0.1){
				py = -controller.getAxis(0);
			}
			else{
				py = 0;
			}
			if(controller.getAxis(1) > 0.1 || controller.getAxis(1) < -0.1){
				px = controller.getAxis(1);
			}
			else{
				px = 0;
			}
			

			
			dir = (float) (Math.atan2(py, px));
			//System.out.println(dir);
			
			if(py != 0 || px != 0){
				velocityX = (float) (speed*Math.cos(dir));
				velocityY = (float) (speed*Math.sin(dir));
			}
			
		}

		
		float ax = 0;
		float ay = 0;
		if(controller.getAxis(2) > 0.1 || controller.getAxis(2) < -0.1)
			ay = -controller.getAxis(2);
		if(controller.getAxis(3) > 0.1 || controller.getAxis(3) < -0.1)
			ax = controller.getAxis(3);
		

		
		if(ax != 0 || ay != 0)
			cursorRotation = (float) (Math.atan2((-controller.getAxis(2)), (controller.getAxis(3))));
		else if(px != 0 || py != 0)
			cursorRotation = (float) dir;
	}


	public void render() {
		
		playerHud.draw();
		for(int i = 0; i < spells.size(); i++){
			spells.get(i).render();
		}
		
	}
	

	private void pressKey() {
		if(lt){
			addRune(ElementType.FIRE);
			updateRuneTexture();
			lt = false;
		}
		if(lb){
			addRune(ElementType.WATER);
			updateRuneTexture();
			lb = false;
		}
		if(rt){
			addRune(ElementType.LIGHTNING);
			updateRuneTexture();
			rt = false;
		}
		if(rb){
			addRune(ElementType.ICE);
			updateRuneTexture();
			rb = false;
		}
		if(x){
			addRune(ActionType.DASH);
			updateRuneTexture();
			x = false;
		}
		if(y){
			addRune(ActionType.SLASH);
			updateRuneTexture();
			y= false;
		}
		if(a){
			addRune(ActionType.SHOOT);
			updateRuneTexture();
			a = false;
		}
		if(ljb){
			addRune(ActionType.SHIELD);
			updateRuneTexture();
			ljb = false;
		}
		if(b){
			rune1 = new Rune();
			rune2 = new Rune();
			updateRuneTexture();
		}
	}
	
	public void cast(){
		if(controller.getButton(9)){
			if(rune1.getType() != RuneType.NULL && rune2.getType() != RuneType.NULL){
				System.out.println(cursorRotation);
				conjure(rune1, rune2, cursorRotation);
				updateRuneTexture();
				x=false;
				//System.out.println("conjured");
			}
		}
	}
	
	public void performDash(float distance){
		speed = PLAYERSPEED * 2;
		dashing = true;
		float dir = cursorRotation;
		//System.out.println("heading: " + Math.toDegrees(dir));
		targetX = (float) (getCenterX()  + (Math.cos(dir)*distance));
		targetY = (float) (getCenterY() + (Math.sin(dir)*distance));
	}


	
	
	

}

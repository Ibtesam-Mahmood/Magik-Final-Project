package com.ibtesam.magik.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ibtesam.magik.enumerations.State;
import com.ibtesam.magik.gui.Button;
import com.ibtesam.magik.gui.Button.Action;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.puppets.BaseActor;
import com.ibtesam.magik.puppets.SuperStage;
import com.ibtesam.magik.states.handler.GameStateManager;

public class FirstTime extends GameState{
	
	private Stage disclaimer;
	

	public FirstTime(final GameStateManager gsm) {
		super(gsm);
		disclaimer = new SuperStage();
		disclaimer.addActor( new BaseActor(0, 0, Launcher.WIDTH, Launcher.HEIGHT, 
				new Texture(Gdx.files.internal("assets/background_opage.png")) ) );
		Button tutorial = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 50, "Play Tutorial",new Action() {
			public void actionEvent() {
				gsm.setState(State.TUTORIAL);
			}
		});
		disclaimer.addActor(tutorial);
		Button menu = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 80, "Menu",new Action() {
			public void actionEvent() {
				Game.firstTime = false;
				gsm.popState();
			}
		});
		disclaimer.addActor(menu);
	}


	public void tick(float dt) {
				
		disclaimer.act(dt);
	}

	public void render() {
		disclaimer.draw();
	}


}

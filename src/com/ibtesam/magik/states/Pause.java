package com.ibtesam.magik.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ibtesam.magik.enumerations.State;
import com.ibtesam.magik.gui.Button;
import com.ibtesam.magik.gui.Button.Action;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.puppets.BaseActor;
import com.ibtesam.magik.states.handler.GameStateManager;

public class Pause extends GameState {
	
	private Stage pause;

	public Pause(final GameStateManager manager) {
		super(manager);
		pause = new Stage();
		pause.addActor( new BaseActor(0, 0, Launcher.WIDTH, Launcher.HEIGHT, 
				new Texture(Gdx.files.internal("assets/background_opage.png")) ) );
		Button play = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 50, "Continue",new Action() {
			public void actionEvent() {
				manager.popState();
			}
		});
		pause.addActor(play);
		Button menu = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 80, "Menu",new Action() {
			public void actionEvent() {
				manager.setState(State.MAINMENU);
			}
		});
		pause.addActor(menu);
	}

	@Override
	public void tick(float dt) {
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			manager.popState();
		}
		
		
		pause.act(dt);
	}

	@Override
	public void render() {
		pause.draw();
	}

}

package com.ibtesam.magik.states;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ibtesam.magik.enumerations.State;
import com.ibtesam.magik.gui.Button;
import com.ibtesam.magik.gui.Button.Action;
import com.ibtesam.magik.gui.Text;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.puppets.BaseActor;
import com.ibtesam.magik.puppets.SuperStage;
import com.ibtesam.magik.states.handler.GameStateManager;

public class MainMenu extends GameState{
	
	private Stage mainMenu;
	

	public MainMenu(final GameStateManager gameStateManager) {
		super(gameStateManager);
		mainMenu = new SuperStage();

	}
	
	private void initMainMenu(){
		mainMenu.addActor( new BaseActor(0, 0, Launcher.WIDTH, Launcher.HEIGHT, 
				new Texture(Gdx.files.internal("assets/background_main.png")) ) );
		Texture t = new Texture(Gdx.files.internal("assets/title.png"));
		mainMenu.addActor( new BaseActor(Launcher.WIDTH/2 - t.getWidth()/2, 300, t.getWidth(), t.getHeight(), t) );
		Button play = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 30, "Play", new Action() {
			public void actionEvent() {
				if(Controllers.getControllers().size >= 1)
					manager.setState(State.PLAY);
				else
					mainMenu.addActor( new Text("Controller Not Connected. Please Connect Controller and Reboot", Launcher.WIDTH/2 - 125, Launcher.HEIGHT/2, 10000, Color.RED) );
			}
		});
		mainMenu.addActor(play);
		Button training = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 60, "Training",new Action() {
			public void actionEvent() {
				manager.setState(State.TRAINING);
			}
		});
		mainMenu.addActor(training);
		Button tutorial = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 90, "Tutorial", new Action() {
			public void actionEvent() {
				manager.setState(State.TUTORIAL);
			}
		});
		mainMenu.addActor(tutorial);
		Button exit = new Button(Launcher.WIDTH/2, Launcher.HEIGHT/2 - 120, "Exit", new Action() {
			public void actionEvent() {
				Gdx.app.exit();
			}
		});

		mainMenu.addActor(exit);
	}


	public void tick(float dt) {
		
		if(Game.firstTime)
			manager.pushState(State.TUTORIAL_DICLAIMER);
		else{
			initMainMenu();
		}
		
		mainMenu.act(dt);
	}

	public void render() {
		mainMenu.draw();
	}


}

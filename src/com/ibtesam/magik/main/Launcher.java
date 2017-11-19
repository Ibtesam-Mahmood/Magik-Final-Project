package com.ibtesam.magik.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Launcher{

//	public static final int WIDTH = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
//	public static final int HEIGHT = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 576;

	public static void main(String[] args) {
		Game game = new Game();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		
		//config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		config.height = Launcher.HEIGHT;
		config.width = Launcher.WIDTH;
		config.resizable = false;

        new LwjglApplication( game , config);
	}
	
	//public static float getLauncherWidth

}

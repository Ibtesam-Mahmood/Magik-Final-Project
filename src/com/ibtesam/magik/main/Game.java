package com.ibtesam.magik.main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ibtesam.magik.states.handler.GameStateManager;


public class Game implements ApplicationListener {
	
	public static final float STEP = 1/60f;
	private float accum;
	
	private SpriteBatch batch;
    //private Texture texture;

	public static boolean firstTime;
    
    private GameStateManager gsm;
    
//    Controller controller;
    
    public void create() {
    	
    	batch = new SpriteBatch();
    	
    	//e.addMouseListener(player1);
    	
    	gsm = new GameStateManager(this);
    	
    	try(FileInputStream in = new FileInputStream("first_time.txt")) { 
    		Scanner c = new Scanner(in);
    		firstTime = Boolean.parseBoolean((c.nextLine()));
    	} catch (Exception e) {
			e.printStackTrace();
    	}
        
//    	controller = Controllers.getControllers().first();
        
        //FileHandle worldFile = Gdx.files.internal("world.png");
        //texture = new Texture(worldFile);
    }

    public void render() {
    	
    	Gdx.gl.glClearColor(1, 1, 1, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//    	for (int i = 0; i <= 17; i++) {
//			if(controller.getButton(i))
//				System.out.println(i);
//		}
    	
    	//System.out.println(Controllers.getControllers().size);
    		
    	tick(Gdx.graphics.getDeltaTime());
    		
    	gsm.render();
    	
    	
    	
    }
    
    public void tick(float dt){
    	
    	gsm.tick(dt);
    	
    	
    }
    
    

	public SpriteBatch getBatch() {
		return batch;
	}
	
	public static float clamp(float x, float min, float max){
		
		if(x > max)
			return max;
		else if(x < min)
			return min;
		else
			return x;
		
	}
	
	public static boolean randomBoolean(){
		double rand = Math.random();
		return rand >= 0.5? true:
			false;
	}


	public GameStateManager getGSM() {
		return gsm;
	}

	@Override
	public void dispose() {}

	@Override
	public void pause() {}

	@Override
	public void resize(int arg0, int arg1) {}

	@Override
	public void resume() {}
}

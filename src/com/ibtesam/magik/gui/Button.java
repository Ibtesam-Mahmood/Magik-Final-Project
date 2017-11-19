package com.ibtesam.magik.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.puppets.BaseActor;

public class Button extends BaseActor{
	
	//Omar ashqar
	
	public interface Action {
		public void actionEvent();
	}
	
	private Action job;
	
	private BitmapFont font = new BitmapFont();
	private GlyphLayout glyphLayout = new GlyphLayout();
	
	private String text;
	
	private boolean sides = false;
	
	private static final Texture leftSide = new Texture(Gdx.files.internal("assets/HUD_bars/leftButtonSide.png"));
	private static final Texture rightSide = new Texture(Gdx.files.internal("assets/HUD_bars/rightButtonSide.png"));
	
	public Button(float x, float y, int w, int h, String text,Action job){
		super(x, y, w, h,  null);
		//new Texture(Gdx.files.internal("assets/blocks/block.png"))
		this.job = job;
		this.text = text;
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.getData().setScale(2);
		glyphLayout.setText(font, text);
		setPosition(x - w/2, y - h/2);
	}
	
	public Button(float x, float y, String text,Action job){
		super(x, y, 0, 0, null);
		this.text = text;
		this.job = job;
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.getData().setScale(2);
		glyphLayout.setText(font, text);
		setWidth(glyphLayout.width);
		setHeight(glyphLayout.height);
		setPosition(x - getWidth()/2, y - getHeight()/2);
	}
	

	public void act(float dt) {
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			float targetX = Gdx.input.getX();
			float targetY = Launcher.HEIGHT - Gdx.input.getY();
			
			if( getBoundingPoly().contains(targetX, targetY) ){
				fireAction();
			}
		}
		if(getBoundingPoly().contains(Gdx.input.getX(), Launcher.HEIGHT - Gdx.input.getY())){
			sides = true;
		}
		else{
			sides = false;
		}
		
	}
	
	public void draw(Batch batch, float parentAlpha) {
        if ( isVisible() ){
        	font.setColor(new Color(Color.BLACK));
            font.draw(batch, text, getCenterX() - (glyphLayout.width)/2, getCenterY() + (glyphLayout.height)/2);
            if(sides){
            	batch.draw(leftSide, getX() - 25, getCenterY() - rightSide.getHeight()/2);
            	batch.draw(rightSide, getX() + getWidth() + 5, getCenterY() - rightSide.getHeight()/2);
            }
        }
        
		
	}
		
	
	private void fireAction() {
		job.actionEvent();
	}

}

package com.ibtesam.magik.blocks;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ibtesam.magik.enumerations.BlockType;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.puppets.BaseActor;

public class Block extends BaseActor{

	private int health;
	private SpriteBatch batch = new SpriteBatch();
	private BitmapFont font = new BitmapFont();
	
	protected BlockType type;
	
	private static Texture blockTexture = new Texture(Gdx.files.internal("assets/blocks/block.png"));
	
	private ElementType eType;

	public Block(float x, float y, int w, int h) {
		super(x, y, w, h, blockTexture);
		health = 100;
		eType = ElementType.NULL;
		type = BlockType.COLLISION;
	}
	
	public Block(float x, float y, int w, int h,  Texture texture, ElementType eType) {
		super(x, y, w, h, texture);
		health = 100;
		this.eType = eType;
		type = BlockType.COLLISION;
	}
	
	public void act(float dt){}
	
	public void render(){
		
		batch.begin();
		font.setColor(new Color(Color.BLACK));
		font.getData().setScale(1, 1);
		font.draw(batch, health + "", getX(), getY() + getHeight()*3/2);
		batch.end();
		
	}
	
	public void delete(){
		
		remove();
		
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}



	public ElementType geteType() {
		return eType;
	}

	public BlockType getBlockType() {
		return type;
	}
	
	
	
	
	
	

}

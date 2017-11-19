package com.ibtesam.magik.puppets;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class BaseActor extends Actor
{
    protected TextureRegion region;
	private TextureRegion[][] allFrames;
	private TextureRegion[] downFrames;
	private TextureRegion[] rightFrames;
	private TextureRegion[] leftFrames;
	
    private float elapsedTime = 0;
    private float animationSpeed = 0;
	
	protected Animation<TextureRegion> animation;
	protected Animation<TextureRegion> animationL;
	protected Animation<TextureRegion> animationR;
	
    protected Rectangle boundary;
    protected float velocityX;
    protected float velocityY;
    protected float rotation = 0;
	private int frameCol = 0;
	private int frameRow = 0;
	
	private boolean animated = false;
	private boolean canFaceFront = false;

    public BaseActor()
    {
        super();
        region = new TextureRegion();
        boundary = new Rectangle();
        velocityX = 0;
        velocityY = 0;
    }
    
    public BaseActor(float x, float y, int boundsX, int boundsY, Texture t)
    {
        super();
        region = new TextureRegion();
        boundary = new Rectangle();
        velocityX = 0;
        velocityY = 0;
        setPosition(x, y);
        setBounds(boundsX, boundsY);
        setTexture(t);
    }
    
    public BaseActor(float x, float y, int boundsX, int boundsY, Texture t, int frameCol, int frameRow, int speed)
    {
        super();
		
        region = new TextureRegion();
        boundary = new Rectangle();
        velocityX = 0; 
        velocityY = 0;
        setPosition(x, y);
        setBounds(boundsX, boundsY);
        setAnimations(t, speed, frameCol, frameRow);
    }
    
    public BaseActor(float x, float y, int boundsX, int boundsY, Texture t, float rotation)
    {
        super(); 
		this.rotation = rotation;
        region = new TextureRegion();
        boundary = new Rectangle();
        velocityX = 0;
        velocityY = 0;
        setPosition(x, y);
        setBounds(boundsX, boundsY);
        setTexture(t);
        setOrigin(0, getHeight()/2);
        setRotation((float) Math.toDegrees(rotation));
    }
    
    protected void setAnimations(Texture t, int speed, int frameCol, int frameRow){
    	
		animated = true;
    	
		this.frameCol = frameCol;
		this.frameRow = frameRow;
    	
		if(frameRow > 2)
			canFaceFront = true;
		allFrames = TextureRegion.split(t, t.getWidth() / frameCol,
				t.getHeight() / frameRow);
		

		rightFrames = allFrames[0];
		if(frameRow > 1)
			leftFrames = allFrames[1];
		if(frameRow > 2)
			downFrames = allFrames[2];
		
		animationSpeed = ((float) frameCol)/((float) speed) * 10;

		animationR = new Animation<TextureRegion>( animationSpeed, rightFrames);
		if(frameRow > 1)
			animationL = new Animation<TextureRegion>( animationSpeed, leftFrames);
		if(frameRow > 2)
			animation = new Animation<TextureRegion>( animationSpeed, downFrames);
		
		if(frameRow < 2)
			region = rightFrames[0];
		else
			region = downFrames[0];
    }
    
    
    public void setRadius(float radius, float rotation) {
          setPosition((float) (getX() + radius*Math.cos(rotation)), (float) (getY() + (radius*8/7)*Math.sin(rotation)) - radius/7 );
	}
    
    
    
    public void setTexture(Texture t){
    	this.animated = false;
    	this.frameRow = 0;
    	if(t != null)
    		region.setRegion( t );
    }
    
    public void setBounds(int w, int h){
    	setWidth(w);
    	setHeight(h);
    }
    
    public Polygon getBoundingPoly()
    {
        
    	boundary.set( (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight() );
    	Polygon poly = new Polygon(new float[] {
    			boundary.x, boundary.y,
    			boundary.x, boundary.y + boundary.height,
    			boundary.x + boundary.width, boundary.y + boundary.height,
    			boundary.x + boundary.width, boundary.y
            });
    	poly.setOrigin(getX(), getY() + getHeight()/2);
    	poly.setRotation((float) Math.toDegrees(rotation));
    	return poly;
    }

    public void act(float dt)
    {
        super.act( dt );
        moveBy( velocityX * dt, velocityY * dt );
    }
    
    public void draw(Batch batch, float parentAlpha) 
    {
        //super.draw( batch, parentAlpha ); // but.... this is empty, so can be deleted here...
    	elapsedTime += Gdx.graphics.getDeltaTime();
    	
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if ( isVisible() ){
        	if(frameRow == 1){
        		batch.draw( animationR.getKeyFrame(elapsedTime, true), getX(), getY(), getOriginX(), getOriginY(),
        				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
        	}
        	else if(!animated || (velocityX == 0 && velocityY == 0) ){
        		batch.draw( region, getX(), getY(), getOriginX(), getOriginY(),
        				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
        	}
        	else{
        		//System.out.println(velocityX);
        		if(velocityX > 0){
            		batch.draw( animationR.getKeyFrame(elapsedTime, true), getX(), getY(), getOriginX(), getOriginY(),
            				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
                   // region = rightFrames[0];
        		}
        		else if(velocityX < 0){
            		batch.draw( animationL.getKeyFrame(elapsedTime, true), getX(), getY(), getOriginX(), getOriginY(),
            				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
                  //  region = leftFrames[0];
        		}
        		else if(canFaceFront){
            		batch.draw( animation.getKeyFrame(elapsedTime, true), getX(), getY(), getOriginX(), getOriginY(),
            				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
                   // region = downFrames[0];
        		}
        		else{
            		batch.draw( animationL.getKeyFrame(elapsedTime, true), getX(), getY(), getOriginX(), getOriginY(),
            				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
                   // region = leftFrames[0];
        		}
        		

        	}
        }
        
        
    }
    
    public float getVelocityX() {
		return velocityX;
	}

	public void setVelocity(float velocityX, float velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	public float getVelocityY() {
		return velocityY;
	}
	
	public float getCenterX(){
		return getX() + getWidth()/2;
	}
	
	public float getCenterY(){
		return this.getY() + getHeight()/2;
	}

}
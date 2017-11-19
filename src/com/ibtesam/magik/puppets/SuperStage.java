package com.ibtesam.magik.puppets;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SuperStage extends Stage {
	
	
	
	public SuperStage() {
		// TODO Auto-generated constructor stub
	}

	public SuperStage(Viewport viewport) {
		super(viewport);
		// TODO Auto-generated constructor stub
	}

	public SuperStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
		// TODO Auto-generated constructor stub
	}
	
	public void addActors(Collection<Actor> collection) {
		
		for (Actor actor : collection) {
			addActor(actor);
		}
		
	}

}

package com.ibtesam.magik.states;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ibtesam.magik.blocks.Block;
import com.ibtesam.magik.blocks.Drop;
import com.ibtesam.magik.blocks.Drop.pickUpType;
import com.ibtesam.magik.enumerations.BlockType;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.gui.HealthBarHUD;
import com.ibtesam.magik.gui.Text;
import com.ibtesam.magik.main.Game;
import com.ibtesam.magik.main.Launcher;
import com.ibtesam.magik.player.Dummy;
import com.ibtesam.magik.player.Player;
import com.ibtesam.magik.player.Player1;
import com.ibtesam.magik.puppets.BaseActor;
import com.ibtesam.magik.puppets.SuperStage;
import com.ibtesam.magik.spellsandeffects.Spell;
import com.ibtesam.magik.spellsandeffects.SpellObject;
import com.ibtesam.magik.states.handler.GameStateManager;

public class Training extends GameState{
	
	private ArrayList<Player> players;
	private ArrayList<HealthBarHUD> healthBarHUDs;
	
	public SuperStage field;
	
	private Camera cam;
	
	private int count = 0;
	
	public ArrayList<Block> blocks;
	private int dropCounter = 0;

	public Training(GameStateManager gameStateManager) {
		super(gameStateManager);
		
		players = new ArrayList<Player>();
		players.add( new Player1(game, 100, 300) );
		players.add( new Dummy(game, 700, 300) );
		
		healthBarHUDs = new ArrayList<HealthBarHUD>(players.size());
		
		blocks = new ArrayList<Block>();
		field = new SuperStage();
		field.addActor( new BaseActor(0, 0, Launcher.WIDTH, Launcher.HEIGHT, 
				new Texture(Gdx.files.internal("assets/background.png")) ) );
		cam = field.getCamera();
		field.getBatch().setProjectionMatrix(cam.combined);
		for (Player player : players) {
			healthBarHUDs.add( new HealthBarHUD(player, players.indexOf(player)) );
			field.addActor(player);
			field.addActor(healthBarHUDs.get(players.indexOf(player)));
		}
		
		
	}
	
	
	public void tick(float dt) {
				
		
		for (Player player : players) {
			if(player.getHealth() == 0){
				player.remove();
				players.remove(player);
				System.out.println("deleting");
				count = 1;
				return;
			}
		}
		
		if(players.size() == 1 && count % 101 == 0){
			System.out.println("working");
			Player dummy = new Dummy(game, 700, 300);
			HealthBarHUD dummyBar =  new HealthBarHUD(dummy, 1);
			players.add( dummy );
			healthBarHUDs.add(dummyBar);
			field.addActor(dummy);
			field.addActor(dummyBar);
			field.addActor( new Text("new Dummy", 700, 370, 25, Color.RED) );
		}


		if(count % 3 == 0 && count != 0){
			if(dropCounter  < 3){
				blocks.add(new Drop( (float) (Math.random()*Launcher.WIDTH), (float) (Math.random()*(Launcher.HEIGHT - 100)), pickUpType.randomType()) );
				dropCounter++;
			}
		}
		
		if(players.size() > 1){
			for (Player player : players) {
				addSpells(player);
			}
			
			
			for(int i = 0; i < blocks.size(); i++){
				if(!(field.getActors().contains(blocks.get(i), true))){
					field.addActor(blocks.get(i));
					//System.out.println("added to feild");
				}
			}
			
			for (Player player1 : players) {
				for (Player player2 : players) {
					if(player1 != player2)
						spellCollision(player1, player2);
				}
			}
	
			for (Player player : players) {
				blockCollision(blocks, player);
			}
		}

		field.act(dt);
		
		if(count <= 1000)
			count++;
		else
			count = 0;
	}

	public void render() {
		for(int i = 0; i < blocks.size(); i++)
			blocks.get(i).render();
		field.draw();
		for (Player player : players) {
			player.render();
		}
	}
	
	//WARNING CANCER CODE AHEAD
	
	private void spellCollision(Player currentPlayer, Player targetPlayer){
		
		Polygon targetPoly = targetPlayer.getBoundingPoly();
		
		for(int j = 0; j < currentPlayer.getSpells().size(); j++){
			
				
				Spell tempSpell = currentPlayer.getSpells().get(j);
				
				if(tempSpell.getType() == SpellType.WALL){
					blockCollision(currentPlayer.getSpells().get(j).blocks, targetPlayer);
				}
				else{
					if(targetPlayer.spellCollision)
						for(int l = 0; l < tempSpell.subSpells.size(); l++){
							
							SpellObject subSpell = tempSpell.subSpells.get(l);
							
							Polygon poly = subSpell.getBoundingPoly();
							
							if( Intersector.overlapConvexPolygons(poly, targetPoly) ){
								subSpell.playerCollision(targetPlayer);
							}
							
						}
					
				}
					
			}
		playerCollision(currentPlayer, targetPlayer);
		
	}
		

		

	
	private void blockCollision(ArrayList<Block> blocks, Player playerOne){
		
		//System.out.println(player1.getHealth());
		
		for(int i = blocks.size() - 1; i >= 0; i--){
			
			Block tempBlock = blocks.get(i);
			
			if(tempBlock.getBlockType() == BlockType.COLLISION){
				for(int j = 0; j < playerOne.getSpells().size(); j++){
					
					Spell tempSpell = playerOne.getSpells().get(j);
					
					for(int l = 0; l < tempSpell.subSpells.size(); l++){
						
						SpellObject subSpell = tempSpell.subSpells.get(l);
						
						Polygon poly = subSpell.getBoundingPoly();
						
						if( Intersector.overlapConvexPolygons(poly, tempBlock.getBoundingPoly()) ){
							subSpell.blockCollision(tempBlock, blocks);
						}
					}
				}
	
				playerCollision(playerOne, tempBlock);
				
			}
			else if(tempBlock.getBlockType() == BlockType.PICKUP){
				if( Intersector.overlapConvexPolygons(playerOne.getBoundingPoly(), tempBlock.getBoundingPoly()) ){
					((Drop) tempBlock).excecuteEffect(playerOne);
					tempBlock.delete();
					blocks.remove(tempBlock);
					dropCounter--;
				}			
			}
			
		}

	}
	
	private void playerCollision(Player target, BaseActor block){
		
		Polygon blockPoly = block.getBoundingPoly();
		
		if( Intersector.overlapConvexPolygons(target.rightBoundaryRectangle(), blockPoly) ){
			target.setPosition(target.getPreviousX() - 1, target.getPreviousY());
			target.setTargetX(target.getCenterX());
			target.setTargetY(target.getCenterY());
			
		}
		else if( Intersector.overlapConvexPolygons(target.leftBoundaryRectangle(), blockPoly) ){
			target.setPosition(target.getPreviousX() + 1, target.getPreviousY());
			target.setTargetX(target.getCenterX());
			target.setTargetY(target.getCenterY());
		}
		else if( Intersector.overlapConvexPolygons(target.bottomBoundaryRectangle(), blockPoly) ){
			target.setPosition(target.getPreviousX(), target.getPreviousY() + 1);
			target.setTargetX(target.getCenterX());
			target.setTargetY(target.getCenterY());
		}
		else if( Intersector.overlapConvexPolygons(target.topBoundaryRectangle(), blockPoly) ){
			target.setPosition(target.getPreviousX(), target.getPreviousY() - 1);
			target.setTargetX(target.getCenterX());
			target.setTargetY(target.getCenterY());
		}
		
	}
	
	private void addSpells(Player player){
		
		for(int i = 0; i < player.getSpells().size(); i++){
			if(player.getSpells().get(i).getType() == SpellType.WALL){
				for(int j = 0 ; j < player.getSpells().get(i).blocks.size(); j++){
					if(!(field.getActors().contains(player.getSpells().get(i).blocks.get(j), true))){
						field.addActor(player.getSpells().get(i).blocks.get(j));
						//System.out.println("added to feild");
					}
				}
			}
			else{
				for(int j = 0 ; j < player.getSpells().get(i).subSpells.size(); j++){
					if(!(field.getActors().contains(player.getSpells().get(i).subSpells.get(j), true))){
						field.addActor(player.getSpells().get(i).subSpells.get(j));
						//System.out.println("added to feild");
					}
				}
			}
			
		}
		
	}
	
	public static ElementType getOppositeEType(ElementType eType){
		if(eType == ElementType.FIRE)
			return ElementType.WATER;
		else if(eType == ElementType.WATER)
			return ElementType.FIRE;
		else if(eType == ElementType.LIGHTNING)
			return ElementType.ICE;
		else if(eType == ElementType.ICE)
			return ElementType.LIGHTNING;
		else
			return null;
	}
	
	private void addHUDBars(ArrayList<Actor> HUDactors, int playerIndex){
		if(playerIndex == 0){
			HUDactors.get(0).setPosition(10, Launcher.HEIGHT - 27);
			HUDactors.get(1).setPosition(10, Launcher.HEIGHT - 27);
			HUDactors.get(2).setPosition(10, Launcher.HEIGHT - 20);
			HUDactors.get(3).setPosition(10, Launcher.HEIGHT - 20);
			HUDactors.get(4).setPosition(10, Launcher.HEIGHT - 20);
		}
		else if(playerIndex == 1){
			HUDactors.get(0).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 27);
			HUDactors.get(1).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 27);
			HUDactors.get(2).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 20);
			HUDactors.get(3).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 20);
			HUDactors.get(4).setPosition(Launcher.WIDTH - 432, Launcher.HEIGHT - 20);
		}
		
		field.addActors(HUDactors);
	}

}

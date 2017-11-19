package com.ibtesam.magik.states;

import java.io.PrintStream;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.ibtesam.magik.blocks.Block;
import com.ibtesam.magik.blocks.Drop;
import com.ibtesam.magik.blocks.Drop.pickUpType;
import com.ibtesam.magik.enumerations.ActionType;
import com.ibtesam.magik.enumerations.BlockType;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.SpellType;
import com.ibtesam.magik.enumerations.State;
import com.ibtesam.magik.gui.HealthBarHUD;
import com.ibtesam.magik.gui.StaticText;
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

public class Tutorial extends GameState{
	
	private ArrayList<Player> players;
	private ArrayList<HealthBarHUD> healthBarHUDs;
	
	private StaticText tutorialText;
	
	boolean winDisplayed = true;
	
	private float moveX = 0, moveY = 0;
	
	private int[] dontPrint = {5, 25, 41, 3, 4, 17, 18, 37, 39, 24, 40, 46};	
	private int objectiveNum = 0;
	
	public SuperStage field;
	
	private Camera cam;
	
	private int count = 0;
	
	public ArrayList<Block> blocks;
	
	BitmapFont font;

	public Tutorial(GameStateManager gameStateManager) {
		super(gameStateManager);
		
		font = new BitmapFont();
		
		players = new ArrayList<Player>();
		players.add( new Player1(game, 100, 300) );
		players.get(0).canCast = false;
		players.get(0).canMove = false;
		players.get(0).canRune = false;
		players.get(0).canAction = false;
		
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
		AssignObjective(objectiveNum);
		
	}
	
	
	public void tick(float dt) {
				
		//cam.position.set(player1.getCenterX(), player1.getCenterY(), 0);
		
		
		for (Player player : players) {
			if(player.getHealth() == 0){
				player.remove();
				players.remove(player);
				System.out.println("deleting");
				return;
			}
		}
		
		 
		if(objectiveCompleted(objectiveNum)){
			objectiveNum++;
			AssignObjective(objectiveNum);
		}
		
			for (Player player : players) {
				addSpells(player);
			}
			

			for(int i = 0; i < blocks.size(); i++){
				if(!(field.getActors().contains(blocks.get(i), true))){
					field.addActor(blocks.get(i));
					//System.out.println("added to feild");
				}
			}
		if(players.size() > 1){
			for (Player player1 : players) {
				for (Player player2 : players) {
					if(player1 != player2)
						spellCollision(player1, player2);
				}
			}
			
			for(Block block : blocks)
				block.act(dt);
			
		}
	
		for (Player player : players) {
			blockCollision(blocks, player);
		}

		field.act(dt);
		
		if(count <= 1000)
			count++;
		else
			count = 0;
	}

	public void render() {
		for(Block block : blocks)
			block.render();
		field.draw();
		for (Player player : players) {
			player.render();
		}
	}
	
	private void AssignObjective(int ob) {
		boolean printReminder = true;
		for (int i = 0; i < dontPrint.length; i++) {
			if(dontPrint[i] == ob){
				printReminder = false;
				break;
			}
		}
		if(printReminder)
			field.addActor( new Text("Press Space to Continue", Launcher.WIDTH/2, 110, 50, Color.RED) );
		switch(ob){
			case 0:
				tutorialText = new StaticText("Welcome to Magik", 500, 100);
				field.addActor( tutorialText );
				return;
			case 1:
				tutorialText = new StaticText("Magik is a game where players fight eachother through the use of spells", 500, 100);
				field.addActor( tutorialText );
				return;
			case 2:
				tutorialText = new StaticText("So lets get to basics", 500, 100);
				field.addActor( tutorialText );
				return;
			case 3:
				tutorialText = new StaticText("LMB is to move, try moving to the pointer", 500, 100);
				blocks.add(new Drop( 500, 300, pickUpType.POINTER) );
				players.get(0).canMove = true;
				field.addActor( tutorialText );
				return;
			case 4:
				tutorialText = new StaticText("Good, now a bit more", 500, 100);
				blocks.add(new Drop( 100, 100, pickUpType.POINTER) );
				blocks.add(new Drop( 700, 500, pickUpType.POINTER) );
				blocks.add(new Drop( 900, 200, pickUpType.POINTER) );
				field.addActor( tutorialText );
				return;
			case 5:
				tutorialText = new StaticText("Ok Ok now come back here", 500, 100);
				players.get(0).canMove = false;
				moveX = 500;
				moveY = 300;
				players.get(0).setTargetX(moveX);
				players.get(0).setTargetY(moveY);
				field.addActor( tutorialText );
				return;
			case 6:
				tutorialText = new StaticText("You are a mage should know what spells are? right", 500, 100);
				field.addActor( tutorialText );
				return;
			case 7:
				tutorialText = new StaticText("What kind of mage are you... ", 500, 100);
				field.addActor( tutorialText );
				return;
			case 8:
				tutorialText = new StaticText("Oh well, spells are formed by combining 2 elements", 500, 100);
				field.addActor( tutorialText );
				return;
			case 9:
				tutorialText = new StaticText("FIRE", 500, 100);
				players.get(0).addRune(ElementType.FIRE);
				players.get(0).addRune(ElementType.FIRE);
				players.get(0).updateRuneTexture();
				field.addActor( tutorialText );
				return;
			case 10:
				tutorialText = new StaticText("WATER", 500, 100);
				players.get(0).clearRunes();
				players.get(0).addRune(ElementType.WATER);
				players.get(0).addRune(ElementType.WATER);
				players.get(0).updateRuneTexture();
				field.addActor( tutorialText );
				return;
			case 11:
				tutorialText = new StaticText("LIGHTNING", 500, 100);
				players.get(0).clearRunes();
				players.get(0).addRune(ElementType.LIGHTNING);
				players.get(0).addRune(ElementType.LIGHTNING);
				players.get(0).updateRuneTexture();
				field.addActor( tutorialText );
				return;
			case 12:
				tutorialText = new StaticText("ICE", 500, 100);
				players.get(0).clearRunes();
				players.get(0).addRune(ElementType.ICE);
				players.get(0).addRune(ElementType.ICE);
				players.get(0).updateRuneTexture();
				field.addActor( tutorialText );
				return;
			case 13:
				tutorialText = new StaticText("Those things floating around you are runes", 500, 100);
				players.get(0).clearRunes();
				players.get(0).updateRuneTexture();
				field.addActor( tutorialText );
				return;
			case 14:
				tutorialText = new StaticText("Right now you have no elements, so your runes are black", 500, 100);
				field.addActor( tutorialText );
				return;
			case 15:
				tutorialText = new StaticText("Runes light up when you add elements to your spell", 500, 100);
				field.addActor( tutorialText );
				return;
			case 16:
				tutorialText = new StaticText("Runes light up when you add elements to your spell, you can only hold 2 though", 500, 100);
				field.addActor( tutorialText );
				return;
			case 17:
				players.get(0).canRune = true;
				tutorialText = new StaticText("Try it out; Q: Fire, W: Water, E: Lightning, R: Ice, Space: Clear", 500, 100);
				count = 1;
				field.addActor( tutorialText );
				return;
			case 18:
				players.get(0).canCast = true;
				tutorialText = new StaticText("Once you have 2 elements you can cast a spell, Click RMB to cast", 500, 100);
				field.addActor( tutorialText );
				return;
			case 19:
				tutorialText = new StaticText("If you noticed after casting your mana bar decreases", 500, 100);
				field.addActor( tutorialText );
				return;
			case 20:
				tutorialText = new StaticText("Mana gets consumed when you cast spells, but dont worry it comes back", 500, 100);
				field.addActor( tutorialText );
				return;
			case 21:
				tutorialText = new StaticText("Mana regenerates faster when you stand still", 500, 100);
				field.addActor( tutorialText );
				return;
			case 22:
				tutorialText = new StaticText("The red bar above mana is health", 500, 100);
				field.addActor( tutorialText );
				return;
			case 23:
				tutorialText = new StaticText("Health is a measure of vitality, if your health reaches zero you loose", 500, 100);
				field.addActor( tutorialText );
				return;
			case 24:
				createDummy(700, 300);
				tutorialText = new StaticText("Practice your spells and destroy the dummy", 500, 100);
				field.addActor( tutorialText );
				return;
			case 25:
				tutorialText = new StaticText("Ok Ok that's enough, poor dummy", 500, 100);
				moveX = 500;
				moveY = 300;
				players.get(0).setTargetX(moveX);
				players.get(0).setTargetY(moveY);
				players.get(0).canCast = false;
				players.get(0).canRune = false;
				players.get(0).canMove = false;
				field.addActor( tutorialText );
				return;
			case 26:
				tutorialText = new StaticText("I don't know if you caught that", 500, 100);
				field.addActor( tutorialText );
				return;
			case 27:
				tutorialText = new StaticText("Some spells can apply debuffs to the target", 500, 100);
				field.addActor( tutorialText );
				return;
			case 28:
				tutorialText = new StaticText("Burn, Slow, Stun, and Freeze", 500, 100);
				field.addActor( tutorialText );
				return;
			case 29:
				tutorialText = new StaticText("I'll let you figure out which spells apply debuffs", 500, 100);
				field.addActor( tutorialText );
				return;
			case 30:
				tutorialText = new StaticText("Along with elements you can also use actions", 500, 100);
				field.addActor( tutorialText );
				return;
			case 31:
				//players.get(0).canMove = true;
				tutorialText = new StaticText("DASH", 500, 100);
				players.get(0).performDash(200);
				field.addActor( tutorialText );
				return;
			case 32:
				moveX = 500;
				moveY = 300;
				players.get(0).setTargetX(moveX);
				players.get(0).setTargetY(moveY);
				tutorialText = new StaticText("SLASH", 500, 100);
				players.get(0).addRune(ActionType.SLASH);
				field.addActor( tutorialText );
				return;
			case 33:
				tutorialText = new StaticText("SHOOT", 500, 100);
				players.get(0).addRune(ActionType.SHOOT);
				field.addActor( tutorialText );
				return;
			case 34:
				tutorialText = new StaticText("SHIELD", 500, 100);
				players.get(0).addRune(ActionType.SHIELD);
				field.addActor( tutorialText );
				return;
			case 35:
				tutorialText = new StaticText("Shield uses mana to make you invuenerable for a while", 500, 100);
				field.addActor( tutorialText );
				return;
			case 36:
				tutorialText = new StaticText("Unlike spells, you only require to press actions to use them", 500, 100);
				field.addActor( tutorialText );
				return;
			case 37:
				players.get(0).canAction = true;
				players.get(0).canMove = true;
				tutorialText = new StaticText("Try them out; A: DASH, S: SLASH, D: SHOOT, E: SHIELD", 500, 100);
				field.addActor( tutorialText );
				return;
			case 38:
				moveX = 500;
				moveY = 300;
				players.get(0).setTargetX(moveX);
				players.get(0).setTargetY(moveY);
				players.get(0).canMove = false;
				players.get(0).canAction = false;
				tutorialText = new StaticText("Actions can be combined with spells to make an elemental action", 500, 100);
				field.addActor( tutorialText );
				return;
			case 39:
				players.get(0).canAction = true;
				players.get(0).canMove = true;
				players.get(0).canRune = true;
				tutorialText = new StaticText("Try it out by adding an element first then an action, an elemental sheild can be broken by an opposite element", 500, 100);
				field.addActor( tutorialText );
				return;
			case 40:
				createDummy(500, 450);
				createDummy(700, 300);
				createDummy(500, 150);
				players.get(0).canCast = true;
				tutorialText = new StaticText("Destroy the dummies and test your spells", 500, 100);
				field.addActor( tutorialText );
				return;
			case 41:
				players.get(0).canAction = false;
				players.get(0).canMove = false;
				players.get(0).canRune = false;
				players.get(0).canCast = false;
				moveX = 500;
				moveY = 300;
				players.get(0).setTargetX(moveX);
				players.get(0).setTargetY(moveY);
				tutorialText = new StaticText("Wow well that conclu..", 500, 100);
				field.addActor( tutorialText );
				return;
			case 42:
				tutorialText = new StaticText("Almost forgot", 500, 100);
				field.addActor( tutorialText );
				return;
			case 43:
				tutorialText = new StaticText("These will spawn randomly, picking them will help you out", 500, 100);
				blocks.add(new Drop( 450, 200, pickUpType.HEAL) );
				blocks.add(new Drop( 500, 200, pickUpType.MANA) );
				blocks.add(new Drop( 550, 200, pickUpType.SPEED) );
				field.addActor( tutorialText );
				return;
			case 44:
				tutorialText = new StaticText("Green: heals you, Blue: rejuvinates mana, Orange: provides a speed boost", 500, 100);
				field.addActor( tutorialText );
				return;
			case 45:
				tutorialText = new StaticText("That Conludes our Tutorial, you will now be returned to the Main Menu", 500, 100);
				field.addActor( tutorialText );
				return;
			case 46:
				if(Game.firstTime){
					Game.firstTime = false;
					try(PrintStream printStream = new PrintStream( "first_time.txt" );) {				
						printStream.print(false + "");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				game.getGSM().setState(State.MAINMENU);
				return;
				
		}

	}
	
	private boolean objectiveCompleted(int ob) {
		int objective = 0;
		
		if(ob == 5 || ob == 25 || ob == 41)
			objective = 2;
		else if(ob == 3 || ob == 4)
			objective = 1;
		else if(ob == 17 || ob == 18 || ob == 37 || ob  == 39)
			objective = 3;
		else if(ob == 24 || ob == 40)
			objective = 4;
		else if(ob != 46)
			objective = 0;

		//System.out.println(ob);
		switch(objective){
		case 0:
			if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
				field.getActors().removeValue(tutorialText, true);
				tutorialText.remove();
				return true;
			}
			break;
		case 1:
			if(blocks.size() == 0){
				field.getActors().removeValue(tutorialText, true);
				tutorialText.remove();
				return true;
			}
			break;
		case 2:
			if(players.get(0).getBoundingPoly().contains(moveX, moveY)){
				field.getActors().removeValue(tutorialText, true);
				tutorialText.remove();
				return true;
			}
			break;
		case 3:
			if(count % 1000 == 0){
				field.getActors().removeValue(tutorialText, true);
				tutorialText.remove();
				return true;
			}
			break;
		case 4:
			if(players.size() == 1){
				field.getActors().removeValue(tutorialText, true);
				tutorialText.remove();
				return true;
			}
			break;
			
			
		}
		return false;
	}
	
	private void createDummy(float x, float y){
		Player dummy = new Dummy(game, x, y); 
		HealthBarHUD dummyBar =  new HealthBarHUD(dummy, 1);
		players.add( dummy );
		healthBarHUDs.add(dummyBar);
		field.addActor(dummy);
		field.addActor(dummyBar);
		field.addActor( new Text("new Dummy", dummy.getX(), dummy.getY() + 70, 25, Color.RED) );
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
	

}

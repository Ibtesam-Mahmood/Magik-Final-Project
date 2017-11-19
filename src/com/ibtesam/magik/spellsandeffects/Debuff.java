package com.ibtesam.magik.spellsandeffects;

import com.ibtesam.magik.enumerations.Debuffs;
import com.ibtesam.magik.player.Player;


public class Debuff {

	
	
	private Player player;
	private Debuffs debuff;
	private int timer;
	private float effect;

	public Debuff(Player player, Debuffs debuff) {
		this.player = player;
		this.debuff = debuff;
		setDebuff(debuff);
	}
	
	private void setDebuff(Debuffs debuff){
		if(debuff == Debuffs.BURN){
			timer = 5*60;
			effect = 0.005f;
			player.setHealing(false);
		}
		else if(debuff == Debuffs.FREEZE){
			timer = 2*60;
			effect = 1f;
		}
		else if(debuff == Debuffs.SLOW){
			timer = 7*60;
			effect = Player.PLAYERSPEED/1.5f;
		}
		else if(debuff == Debuffs.STUN){
			timer = 60;
			effect = 0;
		}
		else if(debuff == Debuffs.SPEED){
			timer = 2*60;
			effect = Player.PLAYERSPEED*1.33f;
		}
			
		
	}
	
	public void tick(float dt){
		
		timer--;
		
		if(timer > 0){
			inflict(debuff);
		}
		else{
			restore(debuff);
		}
		
	}
	
	public void render(){
		
		
	}
	
	private void inflict(Debuffs debuff){
		if(debuff == Debuffs.BURN){
			if(timer % 20 == 0)
				player.setHealth((int) (player.getHealth() - effect));
		}
		else if(debuff == Debuffs.STUN){
			player.setSpeed((int) effect);
		}
		else if(debuff == Debuffs.SLOW){
			player.setSpeed((int) effect);
		}
		else if(debuff == Debuffs.FREEZE){
			player.setSpeed((int) effect);
		}
		else if(debuff == Debuffs.SPEED){
			player.setSpeed((int) effect);
		}
		
		
	}
	
	private void restore(Debuffs debuff){
		if(debuff == Debuffs.BURN){
			player.setHealing(true);
		}
		else if(debuff == Debuffs.STUN){
			player.setTargetX(player.getX());
			player.setTargetY(player.getY());
			player.setSpeed(Player.PLAYERSPEED);
		}
		else if(debuff == Debuffs.SLOW){
			player.setSpeed(Player.PLAYERSPEED);
		}
		else if(debuff == Debuffs.FREEZE){
			player.setSpeed(Player.PLAYERSPEED);
		}
		else if(debuff == Debuffs.SPEED){
			player.setSpeed(Player.PLAYERSPEED);
		}
		player.setDebuffSwitch(debuff, false);
		player.getDeBuffs().remove(this);
		
	}

	public Debuffs getDebuff() {
		return debuff;
	}
	
	
}

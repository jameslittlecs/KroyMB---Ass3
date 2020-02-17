package com.mozarellabytes.kroy.Minigame;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Alien extends Unit{

	//Basic attack, does unit damage to target
	private Attack beamRay = new Attack("Beam Ray", 50, this, this.getDamage()*-1, 0, 0, 0, false);
	//Basic heal, heals self for damage variable
	private Attack dryOff = new Attack("Dry Off", 3, this, 0, this.getDamage()*2, 0, 0, false);
	//Increases self damage variable
	private Attack chargeUp = new Attack("Charge Up", 5, this, 0, 0, 0, 10, false);
	//Deals some damage to target, heals damage dealt
	private Attack probe = new Attack("Probe", 5, this, this.getDamage()*-1, this.getDamage(), 0, 0, false);
	
	//Constructor
	public Alien(int HP, int damage, Vector2 startPosition, int maxHP) {
		super(new Texture(Gdx.files.internal("alien.png")),HP, damage, startPosition, maxHP);
		List<Attack> moveList = Arrays.asList(beamRay,dryOff,chargeUp,probe);
		super.setMoveList(moveList);
	}
	
	//Sets the attacks to do the correct damage based on the current damage
	public void updateMoves() {
		beamRay.setTargetHealthChange(this.getDamage()*-1);
		dryOff.setSelfHealthChange(this.getDamage()*2);
		probe.setSelfHealthChange(this.getDamage());
		probe.setTargetHealthChange(this.getDamage()*-1);
	}
	
	//AI for choosing attacks, based on current health it will choose a different random weighted attack to perform and returns that attack
	public Attack chooseAttack() {
		Random rand = new Random();
		int randomNum = rand.nextInt(100);
		if(this.getHP()<=this.getMaxHP()/2) {
			if(randomNum < 20 && this.chargeUp.hasPP()) {
				return this.chargeUp;
			}
			else if (randomNum < 70 && randomNum > 20 && this.probe.hasPP()){
				return this.probe;
			}
			else {
				return this.beamRay;
			}
		}
		else if(this.getHP() < 20) {
			if(this.dryOff.hasPP()) {
				return this.dryOff;
			}
			else if(this.probe.hasPP()){
				return this.probe;
			}
			else {
				return this.beamRay;
			}
		}
		else {
			if(randomNum < 50 && this.chargeUp.hasPP()) {
				return this.chargeUp;
			}
			else {
				return this.beamRay;
			}
		}
	}

}

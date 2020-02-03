package com.mozarellabytes.kroy.Minigame;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Alien extends Unit{

	//Basic attack, does unit damage to target
	private Attack beamRay = new Attack("Beam Ray", 10, this, this.getDamage()*-1, 0, 0, 0);
	//Basic heal, heals self for damage variable
	private Attack dryOff = new Attack("Dry Off", 3, this, 0, this.getDamage(), 0, 0);
	//Increases self damage variable
	private Attack chargeUp = new Attack("Charge Up", 5, this, 0, 0, 0, 10);
	//Deals some damage to target, heals damage dealt
	private Attack probe = new Attack("Probe", 5, this, this.getDamage()*-1, this.getDamage(), 0, 0);
	
	public Alien(int HP, int damage, Vector2 position) {
		super(HP, damage, position);
		List<Attack> moveList = Arrays.asList(beamRay,dryOff,chargeUp,probe);
		super.setMoveList(moveList);
	}

}

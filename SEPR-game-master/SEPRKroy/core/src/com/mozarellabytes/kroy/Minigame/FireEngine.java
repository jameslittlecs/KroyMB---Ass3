package com.mozarellabytes.kroy.Minigame;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class FireEngine extends Unit{

	//Basic attack, does unit damage to target
	private Attack waterSpray = new Attack("Water Spray", 10, this, this.getDamage()*-1, 0, 0, 0);
	//Basic heal, heals self for damage variable
	private Attack quickRepair = new Attack("Quick Repair", 3, this, 0, this.getDamage(), 0, 0);
	//Increases self damage variable
	private Attack pressurePump = new Attack("Pressure Pump", 5, this, 0, 0, 0, 10);
	//Deals some damage to self, but deals double damage to target
	private Attack waterBlast = new Attack("Water Blast", 5, this, this.getDamage()*-2, this.getDamage()*-1, 0, 0);
	
	public FireEngine(int HP, int damage, Vector2 position, int maxHP) {
		super(HP, damage, position, maxHP);
		List<Attack> moveList = Arrays.asList(waterSpray, quickRepair, pressurePump, waterBlast);
		super.setMoveList(moveList);
	}

}

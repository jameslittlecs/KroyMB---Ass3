package com.mozarellabytes.kroy.Minigame;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class FireEngine extends Unit{

	//Basic attack, does unit damage to target
	private Attack waterSpray = new Attack("Water Spray", 10, this, this.getDamage()*-1, 0, 0, 0, true);
	//Basic heal, heals self for damage variable
	private Attack quickRepair = new Attack("Quick Repair", 3, this, 0, this.getMaxHP()-this.getHP(), 0, 0, false);
	//Increases self damage variable
	private Attack pressurePump = new Attack("Pressure Pump", 5, this, 0, 0, 0, 10, false);
	//Deals some damage to self, but deals double damage to target
	private Attack waterBlast = new Attack("Water Blast", 5, this, this.getDamage()*-2, this.getDamage()*-1, 0, 0, false);
	
	public FireEngine(int HP, int damage, Vector2 startPosition, int maxHP) {
		super(new Texture(Gdx.files.internal("minigameFireEngine.png")),HP, damage, startPosition, maxHP);
		List<Attack> moveList = Arrays.asList(waterSpray, quickRepair, pressurePump, waterBlast);
		super.setMoveList(moveList);
	}

	public void updateMoves() {
		waterSpray.setTargetHealthChange(this.getDamage()*-1);
		quickRepair.setSelfHealthChange(this.getMaxHP()-this.getHP());
		waterBlast.setSelfHealthChange(this.getDamage()*-1);
		waterBlast.setTargetHealthChange(this.getDamage()*-2);
		}
}

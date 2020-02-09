package com.mozarellabytes.kroy.Minigame;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Unit extends Sprite{
	
	private int HP, damage;
	private Vector2 startPosition;
	private Vector2 currentPosition;
	private boolean isAlive;
	private List<Attack> moveList;
	private int maxHP;
	
	public Unit(Texture texture, int HP, int damage, Vector2 startPosition, int maxHP) {
		super(texture);
		this.HP = HP;
		this.damage = damage;
		this.startPosition = startPosition;
		this.currentPosition = startPosition;
		this.isAlive = true;
		this.maxHP = maxHP;
	}
	
	//Methods
	public Attack getAttack(int i) {
		return (Attack)this.getMoveList().get(i);
	}
	
	public void attackAnimation(Vector2 otherUnitPosition) {
		float velocity = 1000;
		Vector2 delta = otherUnitPosition.sub(this.getCurrentPosition()).nor();
		Vector2 newPos = new Vector2(this.getCurrentPosition());
		newPos.add(delta.scl(velocity * Gdx.graphics.getDeltaTime()));
		this.setCurrentPosition(newPos);
	}
	
    public void drawSprite(Batch batch) {
        batch.draw(this, this.currentPosition.x, this.currentPosition.y, 400, 400);
    }
	
	//Getters and Setters
	public int getMaxHP() {
		return this.maxHP;
	}
	
	public void setMaxHP(int hp) {
		this.maxHP = hp;
	}
	
	public int getHP() {
		return this.HP;
	}
	
	public void setHP(int HP) {
		if(HP<=this.maxHP && HP>=0) {
			this.HP = HP;
		}else if(HP>this.maxHP) {
			this.HP = this.maxHP;
		}else if(HP<0) {
			this.HP = 0;
		}
		
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public Vector2 getStartPosition() {
		return this.startPosition;
	}
	
	public void setStartPosition(Vector2 position) {
		this.startPosition = position;
	}
	
	public Vector2 getCurrentPosition() {
		return this.currentPosition;
	}
	
	public void setCurrentPosition(Vector2 position) {
		this.currentPosition = position;
	}
	
	public boolean isAlive() {
		return this.isAlive;
	}
	
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	public List<Attack> getMoveList() {
		return this.moveList;
	}
	
	public void setMoveList(List<Attack> moveList) {
		this.moveList = moveList;
	}
	
	public String getMoveName(int index) {
		return this.getAttack(index).getName();
	}
	
	public int getSelectedIndex() {
		for(int i=0; i<this.getMoveList().size();i++) {
			if(this.getAttack(i).getSelected()) {
				return i;
			}
		}
		return -1;
	}
}

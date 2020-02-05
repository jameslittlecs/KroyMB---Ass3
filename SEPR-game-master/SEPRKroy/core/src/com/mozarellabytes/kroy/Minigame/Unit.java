package com.mozarellabytes.kroy.Minigame;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Unit {
	
	private int HP, damage;
	private Vector2 position;
	private boolean isAlive;
	private List<Attack> moveList;
	private int maxHP;
	
	public Unit(int HP, int damage, Vector2 position, int maxHP) {
		this.HP = HP;
		this.damage = damage;
		this.position = position;
		this.isAlive = true;
		this.maxHP = maxHP;
	}
	
	//Methods
	public Attack getAttack(int i) {
		return (Attack)this.getMoveList().get(i);
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
		this.HP = HP;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
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
}

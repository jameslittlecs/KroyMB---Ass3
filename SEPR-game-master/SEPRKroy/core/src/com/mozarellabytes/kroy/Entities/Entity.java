package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.SoundFX;

public abstract class Entity extends Sprite{
	private Vector2 position;
	private float HP;
	private float maxHP;
	private GameScreen gameScreen;

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public float getHP() {
		return HP;
	}

	public void setHP(float hP) {
		HP = hP;
	}

	public float getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(float maxHP) {
		this.maxHP = maxHP;
	}

    public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void repair(float HP) {
        this.HP += HP;
    }
    public void damage(float dmg) {
    	this.HP -= Math.min(dmg, this.HP);
    }
}

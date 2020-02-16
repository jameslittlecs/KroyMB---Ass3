package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Utilities.AStar;

public class Bomber extends Patrol {
	private float viewDistance;

	public Bomber(GameScreen gameScreen, Vector2 position,Array<Vector2> nodes) {
		this.setGameScreen(gameScreen);
		this.setPosition(position);
		this.setLookLeft(new Texture(Gdx.files.internal("sprites/patrol/ufoleft.png")));
        this.setLookRight(new Texture(Gdx.files.internal("sprites/patrol/uforight.png")));
        this.setLookUp(new Texture(Gdx.files.internal("sprites/patrol/ufoup.png")));
        this.setLookDown(new Texture(Gdx.files.internal("sprites/patrol/ufodown.png")));
		this.setRegion(getLookDown());
		this.setPath(new Queue<Vector2>());
		this.setPoints(new Queue<Vector2>());
		this.setSpeed(1f);
		this.setNodes(nodes);
		this.setRange(4);
		this.viewDistance = 6;
		this.setAd(50);
		this.setHP(25);
		this.setMaxHP(25);
	}
	public void move() {
		if (getTarget() != null) {
			if (Math.abs(Math.floor(getTarget().getPosition().x) - Math.floor(this.getPosition().x)) > this.viewDistance || 
					Math.abs(Math.floor(getTarget().getPosition().y) - Math.floor(this.getPosition().y)) > this.viewDistance) {
				this.setTarget(null);
			}
		}
		if (getTarget() == null) {
			this.setTarget(this.getGameScreen().getStation().nearestTruck(this.getPosition(), this.getRange()));
		}
		if (getTarget() != null) {
			AStar pathfinder = new AStar(this.getGameScreen().getObstacleGrid(), this.getPosition(), new Vector2((float) Math.floor(getTarget().getPosition().x),
					(float) Math.floor(getTarget().getPosition().y)));
			this.setPath(pathfinder.findPath());
			System.out.println(this.getPath().toString());
			System.out.println(this.getPoints().toString());
		}
		super.move();
		//checks if target is within range
		//if not removes target
		//if no target, look for engines
		//if an engine is spotted path towards it
		//if within 1 tile of target engine
	}
	private	boolean nextToTarget() {
		if (this.getTarget().getPosition().x <= this.getPosition().x + 1 &&
				this.getTarget().getPosition().x >= this.getPosition().x - 1 &&
				this.getTarget().getPosition().y <= this.getPosition().y + 1 &&
				this.getTarget().getPosition().y >= this.getPosition().y - 1) {
			return true;
		}
		return false;
	}
	public void attack() {
		if (getTarget() != null) {
			if (nextToTarget()) {
				getTarget().damage(getAd());
				this.getGameScreen().getPatrols().remove(this);
				return;
			}
		}
	}
}

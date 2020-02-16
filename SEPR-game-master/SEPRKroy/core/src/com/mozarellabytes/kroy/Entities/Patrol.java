package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mozarellabytes.kroy.Utilities.AStar;

public abstract class Patrol extends Unit {
	
	private Array<Vector2> nodes;
	private int targetNode = 0;
	private int range;
	private FireTruck target;
	
	public void move() {
		if (this.getPath().isEmpty() && this.getPoints().isEmpty()) {
			if (this.getPosition().equals(getNodes().get(targetNode))) {
				this.targetNode = this.targetNode + 1;
				this.targetNode = this.targetNode % this.getNodes().size;
			}
			AStar pathfinder = new AStar(this.getGameScreen().getObstacleGrid(), this.getPosition(), this.getNodes().get(targetNode));
			this.setPath(pathfinder.findPath());
		}
		super.move();
	}

	public abstract void attack();
	
	public void drawStats(ShapeRenderer shapeMapRenderer) {
		shapeMapRenderer.rect(this.getPosition().x + 0.2f, this.getPosition().y + 1.3f, 0.7f, 0.3f, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        shapeMapRenderer.rect(this.getPosition().x + 0.25f, this.getPosition().y + 1.35f, 0.6f, 0.2f, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK, Color.FIREBRICK);
        shapeMapRenderer.rect(this.getPosition().x + 0.25f, this.getPosition().y + 1.35f, this.getHP() / this.getMaxHP() * 0.6f, 0.2f, Color.RED, Color.RED, Color.RED, Color.RED);
        }

	public Array<Vector2> getNodes() {
		return nodes;
	}

	public void setNodes(Array<Vector2> nodes) {
		this.nodes = nodes;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public FireTruck getTarget() {
		return target;
	}

	public void setTarget(FireTruck target) {
		this.target = target;
	}

}
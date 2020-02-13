package com.mozarellabytes.kroy.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

public abstract class Unit extends Entity {
	
    private Vector2 prevPosition;
    
    private float speed;

    private Queue<Vector2> path;
    private Queue<Vector2> points;
    
	private float ad;
    
    private Texture lookLeft;
    private Texture lookRight;
    private Texture lookUp;
    private Texture lookDown;

    public void move() {
    	if (!this.path.isEmpty() && this.points.isEmpty()) {
        	generatePoints(path.removeFirst());
    	}
    	if (!this.points.isEmpty()) {
    		changeSprite(this.points.first());
    		this.prevPosition = this.getPosition();
    		this.setPosition(this.points.removeFirst());
    	}
    }
    
    public void draw(Batch batch) {
    	batch.draw(this, this.getPosition().x, this.getPosition().y, 1, 1);
    }
    
	private void generatePoints(Vector2 end) {
		int count = (int) (20/this.speed);
        for (int i=1; i<count; i++) {
            this.points.addLast(new Vector2(((end.x - this.getPosition().x)/count) * i + this.getPosition().x, ((end.y - this.getPosition().y)/count) * i + this.getPosition().y));
        }
        this.points.addLast(new Vector2(((int) end.x), ((int) end.y)));
	}
	private void changeSprite(Vector2 nextPos) {
            if (nextPos.x > this.getPosition().x) {
                setTexture(lookRight);
            } else if (nextPos.x < this.getPosition().x) {
                setTexture(lookLeft);
            } else if (nextPos.y > this.getPosition().y) {
                setTexture(lookUp);
            } else if (nextPos.y < this.getPosition().y) {
                setTexture(lookDown);
            }
    }

	public Vector2 getPrevPosition() {
		return prevPosition;
	}
	public void setPrevPosition(Vector2 prevPosition) {
		this.prevPosition = prevPosition;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public Queue<Vector2> getPath() {
		return path;
	}
	public void setPath(Queue<Vector2> path) {
		this.path = path;
	}
	public Queue<Vector2> getPoints() {
		return points;
	}
	public void setPoints(Queue<Vector2> points) {
		this.points = points;
	}
	public Texture getLookLeft() {
		return lookLeft;
	}
	public void setLookLeft(Texture lookLeft) {
		this.lookLeft = lookLeft;
	}
	public Texture getLookRight() {
		return lookRight;
	}
	public void setLookRight(Texture lookRight) {
		this.lookRight = lookRight;
	}
	public Texture getLookUp() {
		return lookUp;
	}
	public void setLookUp(Texture lookUp) {
		this.lookUp = lookUp;
	}
	public Texture getLookDown() {
		return lookDown;
	}
	public void setLookDown(Texture lookDown) {
		this.lookDown = lookDown;
	}

	public float getAd() {
		return ad;
	}

	public void setAd(float ad) {
		this.ad = ad;
	}
}

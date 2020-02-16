package com.mozarellabytes.kroy.Entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.mozarellabytes.kroy.Screens.GameScreen;

public class Gunner extends Patrol {
	
    private float reserve;
    private final ArrayList<Particle> spray;
    
    public Gunner(GameScreen gameScreen, Vector2 position,Array<Vector2> nodes) {
		this.setGameScreen(gameScreen);
		this.setPosition(position);
		this.setLookLeft(new Texture(Gdx.files.internal("sprites/patrol/tankleft.png")));
        this.setLookRight(new Texture(Gdx.files.internal("sprites/patrol/tankright.png")));
        this.setLookUp(new Texture(Gdx.files.internal("sprites/patrol/tankup.png")));
        this.setLookDown(new Texture(Gdx.files.internal("sprites/patrol/tankdown.png")));
        this.setRegion(getLookDown());
		this.setPath(new Queue<Vector2>());
		this.setPoints(new Queue<Vector2>());
		this.setHP(50);
		this.setMaxHP(50);
		this.setSpeed(1f);
		this.setNodes(nodes);
		this.setAd(0.08f);
    	this.spray = new ArrayList<Particle>();
    	this.reserve = 100;
    	this.setRange(4);
    }
    
	@Override
	public void attack() {
		this.setTarget(this.getGameScreen().getStation().nearestTruck(this.getPosition(), this.getRange()));
		if (this.reserve > 0 && this.getTarget() != null) {
            this.spray.add(new Particle(this, this.getTarget(), 1));
            this.reserve -= Math.min(this.reserve, this.getAd());
        }
		this.updateSpray();
	}
	public void updateSpray() {
        if (this.spray != null) {
            for (int i=0; i < this.spray.size(); i++) {
                Particle particle = this.spray.get(i);
                particle.updatePosition();
                if (particle.isHit()) {
                    this.damage(particle);
                    this.removeParticle(particle);
                }
            }
        }
    }
	 private void damage(Particle particle) {
	        particle.getTarget().damage(Math.min(this.getAd(), particle.getTarget().getHP()));
	    }
	 private void removeParticle(Particle particle) {
	        this.spray.remove(particle);
	    }

	@Override
	public void drawStats(ShapeRenderer shapeMapRenderer) {
		super.drawStats(shapeMapRenderer);
		for (Particle particle : this.spray) {
            shapeMapRenderer.rect(particle.getPosition().x, particle.getPosition().y, particle.getSize(), particle.getSize(), particle.getColour(), particle.getColour(), particle.getColour(), particle.getColour());
        }
	}
}

package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Minigame.Alien;
import com.mozarellabytes.kroy.Minigame.FireEngine;
import com.mozarellabytes.kroy.Minigame.Unit;

public class MiniGameScreen implements Screen{

	private final Kroy game;
	
	private Alien alien = new Alien(100, 10, new Vector2(600, 800), 100);
	private FireEngine fireEngine = new FireEngine(100, 10, new Vector2(600, 800), 100);
	
	public MiniGameScreen(Kroy game) {
		// TODO Auto-generated constructor stub
		this.game = game;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		//alien.getAttack(0).performAttack(fireEngine);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		renderHPBar(fireEngine.getHP(), fireEngine.getMaxHP(), Color.RED, Color.FIREBRICK, 1, 1000, 1000, 100, 800);
		game.shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	 private void renderHPBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position, int X, int Y, int H, int W) {
	        
		 	//game.shapeRenderer.rect(X + W - positionSpacer - outerSpacing - barSpacer, Y + outerSpacing, whiteW, H - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
	        //The max health bit
	        game.shapeRenderer.rect(X, Y, W, H, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
	        //The bit that moves and changes with value
	        game.shapeRenderer.rect(X, Y, (value/maxValue)*W, H, progressColour, progressColour, progressColour, progressColour);
	    }

}

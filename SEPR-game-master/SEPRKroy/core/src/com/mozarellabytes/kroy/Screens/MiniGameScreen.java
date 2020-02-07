package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mozarellabytes.kroy.Minigame.Attack;
import com.mozarellabytes.kroy.Minigame.MinigameInputHandler;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Minigame.Alien;
import com.mozarellabytes.kroy.Minigame.FireEngine;
import com.mozarellabytes.kroy.Minigame.Unit;

public class MiniGameScreen implements Screen{
	
	BitmapFont font = new BitmapFont();
	SpriteBatch batch = new SpriteBatch();

	private final Kroy game;
	
	public static Alien alien = new Alien(100, 10, new Vector2(600, 800), 100);
	public static FireEngine fireEngine = new FireEngine(100, 10, new Vector2(600, 800), 100);
	public static Unit unitTurn;
	public static boolean paused = false;
	private boolean playerDead = false;
	private boolean alienDead = false;
	public static boolean gameEnd = false;
	public static Attack chosenAttack;
	
	public MiniGameScreen(Kroy game) {
		// TODO Auto-generated constructor stub
		Gdx.input.setInputProcessor(new MinigameInputHandler(this));
		this.game = game;
		unitTurn = fireEngine;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		//alien.getAttack(0).performAttack(fireEngine);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor( 0, 0, 0, 0 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		//Draw health bar
		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		renderHPBar(fireEngine.getHP(), fireEngine.getMaxHP(), Color.RED, Color.FIREBRICK, 1, 200, 1000, 100, 800);
		renderHPBar(alien.getHP(), alien.getMaxHP(), Color.RED, Color.FIREBRICK, 1, 1400, 1000 , 100, 800);
		game.shapeRenderer.end();
		
		//When its the players turn
		if(!gameEnd) {
			if(this.unitTurn == fireEngine) {
				batch.begin();
		        int yChange = 0;
		        for(int i = 0 ; i<fireEngine.getMoveList().size(); i++) {
		        	if(fireEngine.getMoveList().get(i).getSelected()) {
		        		font.setColor(Color.RED);
		        	}else {
		        		font.setColor(Color.WHITE);
		        	}
		        	font.draw(batch, fireEngine.getMoveName(i) + " (" + fireEngine.getAttack(i).getPP() + "/" + fireEngine.getAttack(i).getMaxPP() + ")", 200, 300 - yChange);
		        	yChange += 20;
		        }
		        batch.end();
			}
			//When its the AI's turn
			else {
				if(!paused) {
					chosenAttack = alien.chooseAttack();
					paused = true;
				}
				batch.begin();
				font.draw(batch, "Alien uses " + chosenAttack.getName() + "!", 500, 300);
				batch.end();
			}
		}
		
		if(fireEngine.getHP()<=0) {
			this.playerDead = true;
			this.gameEnd = true;
		}
		if(alien.getHP()<=0) {
			this.alienDead = true;
			this.gameEnd = true;
		}
		if(gameEnd) {
			//end the game
		}
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
		batch.dispose();
        font.dispose();
		
	}
	
	 private void renderHPBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position, int X, int Y, int H, int W) {
	        
		 	//game.shapeRenderer.rect(X + W - positionSpacer - outerSpacing - barSpacer, Y + outerSpacing, whiteW, H - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
	        //The max health bit
	        game.shapeRenderer.rect(X, Y, W, H, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
	        //The bit that moves and changes with value
	        game.shapeRenderer.rect(X, Y, (value/maxValue)*W, H, progressColour, progressColour, progressColour, progressColour);
	    }

}

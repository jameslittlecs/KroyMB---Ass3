package com.mozarellabytes.kroy.Screens;

import java.awt.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mozarellabytes.kroy.Minigame.Attack;
import com.mozarellabytes.kroy.Minigame.MinigameInputHandler;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Minigame.Alien;
import com.mozarellabytes.kroy.Minigame.FireEngine;
import com.mozarellabytes.kroy.Minigame.Unit;
import com.mozarellabytes.kroy.Utilities.Constants;
import com.mozarellabytes.kroy.Utilities.GUI;
import com.mozarellabytes.kroy.Utilities.GameInputHandler;

public class MiniGameScreen implements Screen{
	
	BitmapFont font = new BitmapFont();
	SpriteBatch batch = new SpriteBatch();
	
	private final Screen parent;

	private final Kroy game;
	
	public static Alien alien;
	public static FireEngine fireEngine;
	public static Unit unitTurn;
	public static boolean paused = false;
	private boolean playerDead = false;
	private boolean alienDead = false;
	public static boolean gameEnd = false;
	public static Attack chosenAttack;
	public static boolean fireEngineMoving = false;
	public static boolean alienMoving = false;
	private boolean goBack = false;
	public static long startMovingTime;
	public static long currentMovingTime;
	
	private final OrthographicCamera camera;
	private Texture backgroundImage,grassImage;
	
	public MiniGameScreen(Kroy game, Screen parent) {
		this.game = game;
		
		this.parent = parent;
		
		Gdx.input.setInputProcessor(new MinigameInputHandler(this));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		backgroundImage = new Texture("minigameBackground.png");
		grassImage = new Texture("grass.png");
		fireEngine = new FireEngine(100, 10, new Vector2(camera.viewportWidth/5f, camera.viewportHeight/4f), 100);
		alien = new Alien(100, 10, new Vector2(camera.viewportWidth/1.45f, camera.viewportHeight/1.6f), 100);
		unitTurn = fireEngine;
		font.getData().setScale(6);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		//Draw background
		batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
		//Draw Grass circles
		batch.draw(grassImage, camera.viewportWidth/7f, camera.viewportHeight/5.5f, camera.viewportWidth/4f, camera.viewportHeight/4f);
		batch.draw(grassImage, camera.viewportWidth/1.6f, camera.viewportHeight/1.9f, camera.viewportWidth/4f, camera.viewportHeight/4f);
		fireEngine.drawSprite(batch);
		alien.drawSprite(batch);
		//Draw FireEngine
		if(fireEngineMoving) {
			if(!goBack) {
				if(fireEngine.getSelectedIndex() == 0 || fireEngine.getSelectedIndex() == 3 ) {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x, alien.getStartPosition().y);
					fireEngine.attackAnimation(thisVector, 1000);
				}else {
					Vector2 thisVector = new Vector2 (fireEngine.getStartPosition().x, fireEngine.getStartPosition().y+20);
					fireEngine.attackAnimation(thisVector, 90);
				}
			}
			else {
				if(fireEngine.getSelectedIndex() == 0 || fireEngine.getSelectedIndex() == 3 ) {
					Vector2 thisVector = new Vector2(fireEngine.getStartPosition().x, fireEngine.getStartPosition().y);
					fireEngine.attackAnimation(thisVector, 1000);
				}else {
					Vector2 thisVector = new Vector2 (fireEngine.getStartPosition().x, fireEngine.getStartPosition().y);
					fireEngine.attackAnimation(thisVector, 90);
				}
			}
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.3 && !goBack) {
				goBack = true;
				startMovingTime = System.currentTimeMillis();
			}
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.3 && goBack) {
				fireEngineMoving = false;
				unitTurn = alien;
				goBack = false;
			}
		}
		if(alienMoving) {
			if(!goBack) {
				if(chosenAttack.getName() == "Beam Ray" || chosenAttack.getName() == "Probe") {
					Vector2 thisVector = new Vector2(fireEngine.getStartPosition().x, fireEngine.getStartPosition().y);
					alien.attackAnimation(thisVector,1000);
				}else {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x, alien.getStartPosition().y+20);
					alien.attackAnimation(thisVector, 90);
				}
			}
			else {
				if(chosenAttack.getName() == "Beam Ray" || chosenAttack.getName() == "Probe") {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x, alien.getStartPosition().y);
					alien.attackAnimation(thisVector,1000);
				}else {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x, alien.getStartPosition().y);
					alien.attackAnimation(thisVector, 90);
				}
			}
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.3 && !goBack) {
				goBack = true;
				startMovingTime = System.currentTimeMillis();
			}
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.3 && goBack) {
				alienMoving = false;
				unitTurn = fireEngine;
				goBack = false;
			}
		}
		batch.end();
		
		//Draw health bar
		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		renderHPBar(fireEngine.getHP(), fireEngine.getMaxHP(), Color.RED, Color.FIREBRICK, 1, camera.viewportWidth/10f, camera.viewportHeight/2f, camera.viewportWidth/6f, camera.viewportHeight/40f);
		renderHPBar(alien.getHP(), alien.getMaxHP(), Color.RED, Color.FIREBRICK, 1, camera.viewportWidth/1.3f, camera.viewportHeight/1.1f, camera.viewportWidth/6f, camera.viewportHeight/40f);
		game.shapeRenderer.end();
		
		//When its the players turn
		
		if(!gameEnd && !fireEngineMoving && !alienMoving) {
			if(this.unitTurn == fireEngine) {
				batch.begin();
		        int yChange = 0;
		        for(int i = 0 ; i<fireEngine.getMoveList().size(); i++) {
		        	if(fireEngine.getMoveList().get(i).getSelected()) {
		        		font.setColor(Color.RED);
		        	}else {
		        		font.setColor(Color.WHITE);
		        	}
		        	font.draw(batch, fireEngine.getMoveName(i) + " (" + fireEngine.getAttack(i).getPP() + "/" + fireEngine.getAttack(i).getMaxPP() + ")", camera.viewportWidth/2f, (camera.viewportHeight/4f)-yChange);
		        	yChange += 80;
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
				font.draw(batch, "Alien uses " + chosenAttack.getName() + "! (Press Enter to continue)", camera.viewportWidth/10f, camera.viewportHeight/5f);
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
			if(playerDead) {
				GUI gui = new GUI(game, (GameScreen) parent);
	            Gdx.input.setInputProcessor(new GameInputHandler((GameScreen) parent, gui));
	            gui.idleInfoButton();
				this.game.setScreen(parent);
			}else if(alienDead) {
				GameState.endGame(true, game);
			}
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
	
	 private void renderHPBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position, float viewportWidth, float viewportHeight, float W, float H) {
	        
		 	//game.shapeRenderer.rect(X + W - positionSpacer - outerSpacing - barSpacer, Y + outerSpacing, whiteW, H - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
	        //The max health bit
	        game.shapeRenderer.rect(viewportWidth, viewportHeight, W, H, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
	        //The bit that moves and changes with value
	        game.shapeRenderer.rect(viewportWidth, viewportHeight, (value/maxValue)*W, H, progressColour, progressColour, progressColour, progressColour);
	    }

}

package com.mozarellabytes.kroy.Screens;

import java.awt.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
	private PlayState state;
	public static long startMovingTime;
	public static long currentMovingTime;
	
	public enum PlayState {
        PLAY, INSTRUCTION
    }
	
	private final OrthographicCamera camera;
	private Texture backgroundImage,grassImage;
	
	/**
	 * Constructor for minigamescreen. This is where the minigame is run
	 * @param game to return to
	 * @param parent
	 */
	public MiniGameScreen(Kroy game, Screen parent) {
		this.game = game;
		
		this.parent = parent;

		state = PlayState.INSTRUCTION;
		gameEnd = false;
		goBack = false;
		alienMoving = false;
		fireEngineMoving = false;
		gameEnd = false;
		alienDead = false;
		playerDead = false;
		paused = false;
		
		Gdx.input.setInputProcessor(new MinigameInputHandler(this));
		
		//Sets the camera in scene filling the users display ratio
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		
		//Initializes background image and fireEngine and Alien objects
		backgroundImage = new Texture("minigameBackground.png");
		fireEngine = new FireEngine(100, 10, new Vector2(camera.viewportWidth/8f, camera.viewportHeight * 3/20f), 100);
		alien = new Alien(100, 10, new Vector2(camera.viewportWidth * 10/16f, camera.viewportHeight * 19/40f), 100);

		unitTurn = fireEngine;
		font.getData().setScale(3);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		//Clears screen at beginning of render
		Gdx.gl.glClearColor(51/255f, 34/255f, 99/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
		
        //Sprite drawing begins here
		batch.begin();
		//Draw background
		batch.draw(backgroundImage, 0, 0, camera.viewportWidth, camera.viewportHeight);
		//Draws fireEngine and alien sprites
		fireEngine.drawSprite(batch);	
		alien.drawSprite(batch);
		
		ShapeRenderer shapeMapRenderer = new ShapeRenderer();
        shapeMapRenderer.setProjectionMatrix(camera.combined);
		
		//FireEngine attack animations
		if(fireEngineMoving) {
			if(!goBack) {
				//Moves the fireEngine forward for the animation
				//For damage dealing attacks
				if(fireEngine.getSelectedIndex() == 0 || fireEngine.getSelectedIndex() == 3 ) {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x - camera.viewportWidth/7f, alien.getStartPosition().y - camera.viewportHeight/7f);
					fireEngine.attackAnimation(thisVector, 1000);
				}
				//For self-value changing attacks
				else {
					Vector2 thisVector = new Vector2 (fireEngine.getStartPosition().x, fireEngine.getStartPosition().y+20);
					fireEngine.attackAnimation(thisVector, 50);
				}
			}
			//Moves the fireEngine backwards for the animation
			else {
				//For damage dealing attacks
				if(fireEngine.getSelectedIndex() == 0 || fireEngine.getSelectedIndex() == 3 ) {
					Vector2 thisVector = new Vector2(fireEngine.getStartPosition().x, fireEngine.getStartPosition().y);
					fireEngine.attackAnimation(thisVector, 1000);
				}
				//For self-value changing attacks
				else {
					Vector2 thisVector = new Vector2 (fireEngine.getStartPosition().x, fireEngine.getStartPosition().y);
					fireEngine.attackAnimation(thisVector, 50);
				}
			}
			//If a certain amount of time passes make the fireEngine go back
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.25 && !goBack) {
				goBack = true;
				startMovingTime = System.currentTimeMillis();
			}
			//If a certain amount of time passes while the fireEngine is going back stop the engine from moving and make it the aliens turn
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.25 && goBack) {
				fireEngineMoving = false;
				unitTurn = alien;
				goBack = false;
			}
		}
		//Handles alien attack animations
		if(alienMoving) {
			if(!goBack) {
				//Moves the alien forward for the animation
				//For damage dealing attacks
				if(chosenAttack.getName() == "Beam Ray" || chosenAttack.getName() == "Probe") {
					Vector2 thisVector = new Vector2(fireEngine.getStartPosition().x + camera.viewportWidth/7f, fireEngine.getStartPosition().y + camera.viewportHeight/7f);
					alien.attackAnimation(thisVector,1000);
				}
				//For self-value changing attacks
				else {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x, alien.getStartPosition().y+20);
					alien.attackAnimation(thisVector, 90);
				}
			}
			//Moves the alien back for the animation
			else {
				//For damage dealing attacks
				if(chosenAttack.getName() == "Beam Ray" || chosenAttack.getName() == "Probe") {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x, alien.getStartPosition().y);
					alien.attackAnimation(thisVector,1000);
				}
				//For self-value changing attacks
				else {
					Vector2 thisVector = new Vector2(alien.getStartPosition().x, alien.getStartPosition().y);
					alien.attackAnimation(thisVector, 90);
				}
			}
			//If a certain amount of time passes make the alien go back
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.25 && !goBack) {
				goBack = true;
				startMovingTime = System.currentTimeMillis();
			}
			//If a certain amount of time passes while the alien is going back stop the engine from moving and make it the fireEngines turn
			if(((System.currentTimeMillis()-startMovingTime)/1000)>.25 && goBack) {
				alienMoving = false;
				unitTurn = fireEngine;
				goBack = false;
			}
		}
		batch.end();
		
		//Draw health bar
		game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		renderHPBar(fireEngine.getHP(), fireEngine.getMaxHP(), Color.RED, Color.FIREBRICK, 1, camera.viewportWidth/6f, camera.viewportHeight * 11/20f, camera.viewportWidth/6f, camera.viewportHeight/40f);
		renderHPBar(alien.getHP(), alien.getMaxHP(), Color.RED, Color.FIREBRICK, 1, camera.viewportWidth * 10/16f, camera.viewportHeight * 7/8f, camera.viewportWidth/6f, camera.viewportHeight/40f);
		game.shapeRenderer.end();
		
		//When its the players turn
		
		if(!gameEnd && !fireEngineMoving && !alienMoving) {
			//For the fireEngine's turn
			if(this.unitTurn == fireEngine) {
				batch.begin();
				//The displacement value for the attack text being displayed to screen (will be incremented so all attacks are not displayed on one anther)
		        int yChange = 0;
		        //For all moves in the fireEngines move list, they are drawn onto the screen. The currently selected move is drawn red
		        for(int i = 0 ; i<fireEngine.getMoveList().size(); i++) {
		        	if(fireEngine.getMoveList().get(i).getSelected()) {
		        		font.setColor(Color.RED);
		        	}else {
		        		font.setColor(Color.WHITE);
		        	}
		        	font.draw(batch, fireEngine.getMoveName(i) + " (" + fireEngine.getAttack(i).getPP() + "/" + fireEngine.getAttack(i).getMaxPP() + ")", camera.viewportWidth * 2/3f, (camera.viewportHeight * 1/3f)-yChange);
		        	yChange += 80;
		        }
		        batch.end();
			}
			//When its the AI's turn
			else {
				if(!paused) {
					//Chooses the attack and waits for space to be pressed
					chosenAttack = alien.chooseAttack();
					paused = true;
				}
				batch.begin();
				//Displays the alien's attack they are using
				font.draw(batch, "Alien uses " + chosenAttack.getName() + "! (Press Enter to continue)", camera.viewportWidth/10f, camera.viewportHeight/7f);
				batch.end();
			}
		}
		
		//Checks if the fireEngine is dead. If so the player looses
		if(fireEngine.getHP()<=0) {
			this.playerDead = true;
			this.gameEnd = true;
		}
		//Checks if the alien is dead. If so the alien looses
		if(alien.getHP()<=0) {
			this.alienDead = true;
			this.gameEnd = true;
		}
		//If the game ends, check to see who won. If alien bring player back to main game. If player bring player to win game screen.
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
		
		switch (state) {
        case PLAY:
            break;
        case INSTRUCTION:
            // render dark background
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeMapRenderer.setColor(0, 0, 0, 0.9f);
            shapeMapRenderer.rect(this.camera.viewportWidth/6f, this.camera.viewportHeight/2f, this.camera.viewportWidth * 2/3f, this.camera.viewportHeight * 1/3f);
            shapeMapRenderer.end();
            GlyphLayout layout = new GlyphLayout();
            String instructionText1 = "Battle Instructions - ";
            String instructionText2 = "Water Spray - Basic Attack Move";
            String instructionText3 = "Quick Repair - Repair Fire Engine";
            String instructionText4 = "Pressure Pump - Skip Turn, Perminated Dmg Increase";
            String instructionText5 = "Water Blast - Strong Attack, But Fire Engine Damaged" + "\n" + "\n" + "Press - Space - To Continue";
            
            
            layout.setText(game.font26, instructionText1);
            layout.setText(game.font26, instructionText2);
            layout.setText(game.font26, instructionText3);
            layout.setText(game.font26, instructionText4);
            layout.setText(game.font26, instructionText5);
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            game.font50.draw(game.batch, instructionText1, camera.viewportWidth * 7/36f, camera.viewportHeight* 28/36f);
            game.font33.draw(game.batch, instructionText2, camera.viewportWidth * 7/36f, camera.viewportHeight* 28/36f - 60f);
            game.font33.draw(game.batch, instructionText3, camera.viewportWidth * 7/36f, camera.viewportHeight* 28/36f - 100f);
            game.font33.draw(game.batch, instructionText4, camera.viewportWidth * 7/36f, camera.viewportHeight* 28/36f - 140f);
            game.font33.draw(game.batch, instructionText5, camera.viewportWidth * 7/36f, camera.viewportHeight* 28/36f - 180f);

            game.batch.end();
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
	
	//public void reset

	@Override
	public void dispose() {
		batch.dispose();
        font.dispose();
		
	}
	
	public void storyPlay() {
		this.state = PlayState.PLAY;
	}
	
	//Renders the health bars 
	 private void renderHPBar(float value, float maxValue, Color progressColour, Color backgroundColour, int position, float viewportWidth, float viewportHeight, float W, float H) {
	        
		 	//game.shapeRenderer.rect(X + W - positionSpacer - outerSpacing - barSpacer, Y + outerSpacing, whiteW, H - outerSpacing*2 - spaceForText, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
	        //The max health bit
	        game.shapeRenderer.rect(viewportWidth, viewportHeight, W, H, backgroundColour, backgroundColour, backgroundColour, backgroundColour);
	        //The bit that moves and changes with value
	        game.shapeRenderer.rect(viewportWidth, viewportHeight, (value/maxValue)*W, H, progressColour, progressColour, progressColour, progressColour);
	    }

}

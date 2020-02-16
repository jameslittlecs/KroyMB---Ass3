package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mozarellabytes.kroy.Entities.*;
import com.mozarellabytes.kroy.GameState;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Utilities.*;
import java.util.ArrayList;


/**
 * The Screen that our game is played in.
 * Accessed from MenuScreen when the user
 * clicks the Start button, and exits when
 * the player wins or loses the game
 */
public class GameScreen implements Screen {
	
	private ArrayList<Patrol> patrols;
	
	private int[][] obstacleGrid;

	private int storyCounter;
	
    /** Instance of our game that allows us the change screens */
    private final Kroy game;

    /** Renders our tiled map */
    private final OrthogonalTiledMapRenderer mapRenderer;

    /** Camera to set the projection for the screen */
    private final OrthographicCamera camera;

    /** Renders shapes such as the health/reserve
     * stat bars above entities */
    private final ShapeRenderer shapeMapRenderer;

    /** Stores the layers of our tiled map */
    private final MapLayers mapLayers;

    /** Stores the structures layers, stores the background layer */
    private final int[] structureLayersIndices, backgroundLayerIndex;

    /** Batch that has dimensions in line with the 40x25 map */
    private final Batch mapBatch;

    /** Used for shaking the camera when a bomb hits a truck */
    private final CameraShake camShake;

    /** Stores whether the game is running or is paused */
    private PlayState state;
    private StoryState storyState;

    /**
     * Deals with all the user interface on the screen
     * that does not want to be inline with the map
     * coordinates, e.g. big stat bars, buttons, pause
     * screen
     */
    private final GUI gui;

    /**
     * Stores the progress through the game. It keeps
     * track of trucks/fortresses and will end the game
     * once an end game condition has been met
     */
    public final GameState gameState;

    /** List of Fortresses currently active on the map */
    private final ArrayList<Fortress> fortresses;

    private int maxFortress;
    
    private static int numberofFortressAlive;
    
    private String finalFortress;
    
    /** Where the FireEngines' spawn, refill and repair */
    private final FireStation station;

    /** The FireTruck that the user is currently drawing a path for */
    public FireTruck selectedTruck;

    /** The entity that the user has clicked on to show
     * the large stats in the top left corner */
    public Object selectedEntity;

    /** Play when the game is being played
     * Pause when the pause button is clicked */
    public enum PlayState {
        PLAY, PAUSE
    }
    
    /** Used to switch between story state within the game
     * Non is the normal state and the other states are called when the story is prompted */
    public enum StoryState {
    	NON, INTRO, FORTRESS, BOSS, UPDATE, MSG
    }
    
    long startTime;

	long currentTime;
	
	long timeDifference;
    
    int upgradeCounter;
    
    int timer, timerS;
    
    int upgradeTimes, upgradeTimer, stationTimer, stationTimes;
    
    boolean stationDestoryed;

    /**
     * Constructor which has the game passed in
     *
     * @param game LibGdx game
     */
    
    public GameScreen(Kroy game) {
        this.game = game;
        
        upgradeTimes = 60;
        upgradeTimer = upgradeTimes;
        stationTimes = 5;
        stationTimer = stationTimes;
        
        startTime = System.currentTimeMillis();
//        System.out.println(startTime);
        
        upgradeCounter = 0;

        state = PlayState.PLAY;
        storyState = StoryState.INTRO;
        storyCounter = 0;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        TiledMap map = new TmxMapLoader().load("maps/YorkMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.TILE_WxH);
        mapRenderer.setView(camera);

        shapeMapRenderer = new ShapeRenderer();
        shapeMapRenderer.setProjectionMatrix(camera.combined);

        gui = new GUI(game, this);

        Gdx.input.setInputProcessor(new GameInputHandler(this, gui));

        gameState = new GameState();
        camShake = new CameraShake();

        //Orders renderer to start rendering the background, then the player layer, then structures
        mapLayers = map.getLayers();
        backgroundLayerIndex = new int[]{mapLayers.getIndex("background")};

        structureLayersIndices = new int[]{mapLayers.getIndex("structures"),
                mapLayers.getIndex("structures2"),
                mapLayers.getIndex("transparentStructures")};

        station = new FireStation(this, 3, 2);

        spawn(FireTruckType.Ocean);
        spawn(FireTruckType.Speed);
        spawn(FireTruckType.Cannon);
        spawn(FireTruckType.Juggernaut);
        spawn(FireTruckType.Standard);
        
        generateGrid((TiledMapTileLayer) mapLayers.get("collisions"));
        
        this.patrols = new ArrayList<Patrol>();
        Array<Vector2> patrolPath1 = new Array<Vector2>();
        patrolPath1.add(new Vector2(10, 5));
        patrolPath1.add(new Vector2(10, 10));
        patrolPath1.add(new Vector2(20, 8));
        Array<Vector2> patrolPath2 = new Array<Vector2>();
        patrolPath2.add(new Vector2(15, 15));
        patrolPath2.add(new Vector2(23, 15));
        Gunner patrol1 = new Gunner(this, new Vector2(2, 5), patrolPath1);
        Bomber patrol2 = new Bomber(this, new Vector2(15, 13), patrolPath2);
        this.patrols.add(patrol1);
        this.patrols.add(patrol2);

        fortresses = new ArrayList<Fortress>();
        fortresses.add(new Fortress(this, new Vector2(12, 18.5f), FortressType.Revs));
        fortresses.add(new Fortress(this, new Vector2(30.5f, 17.5f), FortressType.Walmgate));
        fortresses.add(new Fortress(this, new Vector2(16, 3.5f), FortressType.Clifford));
        fortresses.add(new Fortress(this, new Vector2(35f, 5f), FortressType.CentralHall));
        fortresses.add(new Fortress(this, new Vector2(2.5f, 14f), FortressType.Minister));
        fortresses.add(new Fortress(this, new Vector2(30f, 6f), FortressType.TrainStation));
        
        maxFortress = fortresses.size();
        numberofFortressAlive = maxFortress;      
        
        //finalFortress = fortresses.get(0).getFortressType().getName();
        
        // sets the origin point to which all of the polygon's local vertices are relative to.
        for (FireTruck truck : station.getTrucks()) {
            truck.setOrigin(Constants.TILE_WxH / 2, Constants.TILE_WxH / 2);
        }

        mapBatch = mapRenderer.getBatch();

        if (SoundFX.music_enabled) {
            SoundFX.sfx_soundtrack.setVolume(.5f);
            SoundFX.sfx_soundtrack.play();
        }
       

    }

	@Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render(backgroundLayerIndex);

        mapBatch.begin();
        
        for (FireTruck truck : station.getTrucks()) {
            truck.drawPath(mapBatch);
            truck.drawSprite(mapBatch);
        }
        for (Patrol p : this.patrols) {
            p.draw(mapBatch);
        }

        station.draw(mapBatch);

        for (Fortress fortress : this.fortresses) {
            fortress.draw(mapBatch);
        }

        mapBatch.end();

        mapRenderer.render(structureLayersIndices);

        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (FireTruck truck : station.getTrucks()) {
            truck.drawStats(shapeMapRenderer);
        }

        for (Fortress fortress : fortresses) {
            fortress.drawStats(shapeMapRenderer);
            for (Bomb bomb : fortress.getBombs()) {
                bomb.drawBomb(shapeMapRenderer);
            }
        }
        for (Patrol p : this.patrols) {
            p.drawStats(shapeMapRenderer);
        }

        shapeMapRenderer.end();

        gui.renderSelectedEntity(selectedEntity);

        switch (state) {
            case PLAY:
                this.update(delta);
                break;
            case PAUSE:
                // render dark background
                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.5f);
                shapeMapRenderer.rect(0, 0, this.camera.viewportWidth, this.camera.viewportHeight);
                shapeMapRenderer.end();
                gui.renderPauseScreenText();
                
        }
        
        switch (storyState) {
        	case NON:
//        		this.update(delta); i think this is making update happen twice
        		break;
        	case INTRO:
            	System.out.println(storyCounter);
        		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.8f);
                shapeMapRenderer.rect(this.camera.viewportWidth/4f, this.camera.viewportHeight/4f, this.camera.viewportWidth/2f, this.camera.viewportHeight/2f);
                shapeMapRenderer.end();
        		gui.renderIntroText();
        		break;
        	case FORTRESS:
        		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.8f);
                shapeMapRenderer.rect(this.camera.viewportWidth/4f, this.camera.viewportHeight/4f, this.camera.viewportWidth/2f, this.camera.viewportHeight/2f);
                shapeMapRenderer.end();
        		gui.renderFortressText();
        		break;
        	case BOSS:
        		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.8f);
                shapeMapRenderer.rect(this.camera.viewportWidth/4f, this.camera.viewportHeight/4f, this.camera.viewportWidth/2f, this.camera.viewportHeight/2f);
                shapeMapRenderer.end();
        		gui.renderBossText();
        		break;
        	case UPDATE:
        		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.8f);
                shapeMapRenderer.rect(this.camera.viewportWidth/4f, this.camera.viewportHeight/4f, this.camera.viewportWidth/2f, this.camera.viewportHeight/2f);
                shapeMapRenderer.end();
        		gui.renderUpdateText();
        		break;
        	case MSG:
        		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeMapRenderer.setColor(0, 0, 0, 0.8f);
                shapeMapRenderer.rect(this.camera.viewportWidth/3f, this.camera.viewportHeight * 53/64f, this.camera.viewportWidth/3f, this.camera.viewportHeight * 10/64f);
                shapeMapRenderer.end();
        		gui.renderStoryUpdate(upgradeCounter);       
        }
        
        gui.renderButtons();
        
        
    }

    /**
     * Manages all of the updates/checks during the game
     *
     * @param delta The time in seconds since the last render
     */
    private void update(float delta) {
        gameState.hasGameEnded(game);
        
        //If the minigame is lost the game is restored to its previous state with a low health fortress
        if(gameState.getMinigameEntered()) {
        	this.toMiniGameScreen();
        	gameState.setMinigameEntered(false);
        	if(finalFortress == "Revolution") {
        		fortresses.add(new Fortress(this, new Vector2(12, 18.5f), FortressType.Revs));
        	}else if(finalFortress == "Clifford's Tower") {
        		fortresses.add(new Fortress(this, new Vector2(16, 3.5f), FortressType.Clifford));
        	}else if(finalFortress == "Walmgate Bar"){
        		 fortresses.add(new Fortress(this, new Vector2(30.5f, 17.5f), FortressType.Walmgate));
        	}
        	fortresses.get(0).setHP(20);
        	this.updateFortressAlive();
        	gameState.setMinigameEntered(false);
        }
        this.updateFortressAlive();
        CameraShake.update(delta, camera, new Vector2(camera.viewportWidth / 2f, camera.viewportHeight / 2f));

        station.restoreTrucks();
        station.checkForCollisions();
        gameState.setTrucksInAttackRange(0);

        currentTime = System.currentTimeMillis();
        
		timeDifference = (currentTime - startTime)/1000;
		int time = (int) timeDifference;
		timer = upgradeTimer - time;
		stationTimes = stationTimer - time;
		
		if (timer == 0) {
			upgradeTimer = upgradeTimes + upgradeTimer*2;
		} 
		
		if (stationTimes <= 0) {
			stationTimes = 0;
		}
		
		if (timeDifference >= stationTimer && stationDestoryed == false) {
			station.destroyStation();
			stationDestoryed = true;
		}
			
		if (upgradeCounter == 0 && timeDifference >= upgradeTimes) {
			upgradeFortresses();
			upgradeCounter++;
			this.storyState = StoryState.MSG;
		} else if (upgradeCounter == 1 && timeDifference >= upgradeTimes*3){
			upgradeFortresses();
			upgradeCounter++;
			this.storyState = StoryState.MSG;
		} else if (upgradeCounter == 2 && timeDifference >= upgradeTimes*7){
			upgradeFortresses();
			upgradeCounter++;
			this.storyState = StoryState.MSG;
		} 
		
		if (upgradeCounter == 3) {
			timer = 0;
		}

		timeDifference = currentTime - startTime;

//		System.out.println(timeDifference);  

		
		if (upgradeCounter == 0 && timeDifference >= 40000 || upgradeCounter == 1 && timeDifference >= 80000 || upgradeCounter == 2 && timeDifference >= 120000) {
			upgradeFortresses();
			upgradeCounter++;
		}
        
        if (maxFortress - fortresses.size() == 1 && storyCounter == 0) {
        	storyCounter++;
        	this.storyState = StoryState.FORTRESS;
        	System.out.println(storyCounter);
        } else if (maxFortress - fortresses.size() == 2 && storyCounter == 1) {
        	storyCounter++;
        	this.storyState = StoryState.UPDATE;
        } else if (maxFortress - fortresses.size() == 3 && storyCounter == 2){
        	storyCounter++;
        	this.storyState = StoryState.BOSS;
        } else {}
        
        ArrayList<Patrol> patrolList = new ArrayList<Patrol>(this.patrols);
        for (Patrol p : patrolList) {
        	if (p.getHP() <= 0) {
        		this.patrols.remove(p);
        		continue;
        	}
            this.patrols.get(this.patrols.indexOf(p)).move();
            this.patrols.get(this.patrols.indexOf(p)).attack();
        }
        
        for (int i = 0; i < station.getTrucks().size(); i++) {
            FireTruck truck = station.getTruck(i);

            truck.move();
            truck.updateSpray();

            // manages attacks between trucks and fortresses
            for (Fortress fortress : this.fortresses) {
                if (fortress.withinRange(truck.getVisualPosition())) {
                    fortress.attack(truck, true);
                }
                if (truck.enemyInRange(fortress.getPosition())) {
                    gameState.incrementTrucksInAttackRange();
                    truck.attack(fortress);
                    break;
                }
            }
            for (Patrol p : this.patrols) {
            	 if (truck.enemyInRange(p.getPosition())) {
                     gameState.incrementTrucksInAttackRange();
                     truck.attack(p);
                     break;
                 }
            }

            // check if truck is destroyed
            if (truck.getHP() <= 0) {
                gameState.removeFireTruck();
                station.destroyTruck(truck);
                if (truck.equals(this.selectedTruck)) {
                    this.selectedTruck = null;
                }
            }
        }

        for (int i = 0; i < this.fortresses.size(); i++) {
            Fortress fortress = this.fortresses.get(i);

            boolean hitTruck = fortress.updateBombs();
            if (hitTruck) {
                camShake.shakeIt(.2f);
            }

            // check if fortress is destroyed
            if (fortress.getHP() <= 0) {
            	//destroys the attacking fire engine to start the minigame
            	if(this.fortresses.size() == 1) {
            		for (int j = 0; j < station.getTrucks().size(); j++) {
                        FireTruck truck = station.getTruck(j);
                        if (truck.enemyInRange(fortress.getPosition())) {
                        	gameState.removeFireTruck();
                        	station.destroyTruck(truck);
                            if (truck.equals(this.selectedTruck)) {
                                this.selectedTruck = null;
                            }
                            break;
                        }
            		}
            	}
                this.fortresses.remove(fortress);
                if(this.fortresses.size() == 1) {
                	finalFortress = fortresses.get(0).getFortressType().getName();
                }
                if (SoundFX.music_enabled) {
                    SoundFX.sfx_fortress_destroyed.play();
                }
            }

        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (gameState.getTrucksInAttackRange() > 0) {
                SoundFX.playTruckAttack();
            }
            else {
                SoundFX.stopTruckAttack();
            }
        }


        shapeMapRenderer.end();
        shapeMapRenderer.setColor(Color.WHITE);

        gui.renderSelectedEntity(selectedEntity);
        
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        shapeMapRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeMapRenderer.setColor(0, 0, 0, 0.8f);
        shapeMapRenderer.rect(this.camera.viewportWidth * 95/128f, this.camera.viewportHeight * 1/200f, this.camera.viewportWidth/4f, this.camera.viewportHeight * 1/25f);
        shapeMapRenderer.rect(this.camera.viewportWidth * 57/128f, this.camera.viewportHeight * 1/200f, this.camera.viewportWidth* 36/128, this.camera.viewportHeight * 1/25f);
        shapeMapRenderer.end();
        
        gui.renderTimer(timer,stationTimes);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        shapeMapRenderer.dispose();
        mapBatch.dispose();
        SoundFX.sfx_soundtrack.stop();
    }

    public void upgradeFortresses() {
    	for (Fortress fortress : this.fortresses) {
    		fortress.upgradeStat();
    	}
    }
    
    
    /**
     * Checks whether the player has clicked on a truck and sets that
     * truck to selected truck and entity
     *
     * @param position  coordinates of where the user clicked
     * @return          <code>true</code> if player clicks on a truck
     *                  <code>false</code> otherwise
     */
    public boolean checkClick(Vector2 position) {
        for (int i = this.station.getTrucks().size() - 1; i >= 0; i--) {
            FireTruck selectedTruck = this.station.getTruck(i);
            Vector2 truckTile = getTile(selectedTruck.getPosition());
            if (position.equals(truckTile) &&!selectedTruck.getMoving()) {
                this.selectedTruck = this.station.getTruck(i);
                this.selectedEntity = this.station.getTruck(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the coordinates of the tile that the truck is closest to
     *
     * @param position  coordinates of truck
     * @return          coordinates of closest tile
     */
    private Vector2 getTile(Vector2 position) {
        return new Vector2((float) Math.round((position.x)), (float) Math.round(position.y));
    }

    /**
     * Checks whether the user has clicked on a the last tile in a
     * truck's trail path and selects the truck as active truck and
     * entity
     *
     * @param position  the coordinates where the user clicked
     * @return          <code>true</code> if player clicks on the
     *                  last tile in a truck's path
     *                  <code>false</code> otherwise
     */
    public boolean checkTrailClick(Vector2 position) {
        for (int i=this.station.getTrucks().size()-1; i>=0; i--) {
            if (!this.station.getTruck(i).path.isEmpty()) {
                if (position.equals(this.station.getTruck(i).path.last())) {
                    this.selectedTruck = this.station.getTruck(i);
                    this.selectedEntity = this.station.getTruck(i);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Checks whether the tile that the user is trying to add to the
     *  truck's path is on the road. This uses the custom "road"
     * boolean property in the collisions layer within the tiled map
     *
     * @param x x coordinate of tile
     * @param y y coordinate of tile
     * @return  <code>true</code> if the tile is a road
     *          <code>false</code> otherwise
     */
    public boolean isRoad(int x, int y) {
        return ((TiledMapTileLayer) mapLayers.get("collisions")).getCell(x, y).getTile().getProperties().get("road").equals(true);
    }

    /**
     * Changes from GameScreen to Control screen, passing "game" so that when
     * the player exits the control screen, it knows to return to the Game
     */
    public void toControlScreen() {
        game.setScreen(new ControlsScreen(game, this, "game"));
    }
    
    public void toMiniGameScreen() {
    	game.setScreen(new MiniGameScreen(game, this));
    }

    /** Exits the main game screen and goes to the menu, called when the home
     * button is clicked */
    public void toHomeScreen() {
        game.setScreen(new MenuScreen(game));
        SoundFX.sfx_soundtrack.dispose();
    }

    /**
     * Creates a new FireEngine, plays a sound and adds it gameState to track
     * @param type Type of truck to be spawned (Ocean, Speed)
     */
    private void spawn(FireTruckType type) {
        SoundFX.sfx_truck_spawn.play();
        station.spawn(new FireTruck(this, new Vector2(6,2), type));
        gameState.addFireTruck();
    }

    /** Toggles between Play and Pause state when the Pause button is clicked */
    public void changeState() {
        if (this.state.equals(PlayState.PLAY)) {
            this.state = PlayState.PAUSE;
        } else {
            this.state = PlayState.PLAY;
        }
    }
    
    public void storyNon() {
    	this.storyState = StoryState.NON;
    }

    public FireStation getStation() {
        return this.station;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public ArrayList<Fortress> getFortresses() {
        return this.fortresses;
    }
    
    public void updateFortressAlive() {
    	numberofFortressAlive = this.getFortresses().size();
    }
    
    public static int getFortressesAlive() {
    	return numberofFortressAlive;
    }

    public PlayState getState() {
        return this.state;
    }
    private void generateGrid(TiledMapTileLayer layer) {
		this.obstacleGrid = new int[layer.getWidth()][layer.getHeight()];
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				if (layer.getCell(x, y).getTile().getProperties().get("road").equals(true)) {
					this.obstacleGrid[x][y] = 1;
				}
				else {
					this.obstacleGrid[x][y] = 0;
				}
			}
		}
	}
    public int[][] getObstacleGrid(){
    	return this.obstacleGrid;
    }

	public ArrayList<Patrol> getPatrols() {
		return patrols;
	}

	public CameraShake getCamShake() {
		return camShake;
	}
}


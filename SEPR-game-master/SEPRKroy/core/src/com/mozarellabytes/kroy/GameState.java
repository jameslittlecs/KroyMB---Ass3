package com.mozarellabytes.kroy;

import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Entities.FortressType;
import com.mozarellabytes.kroy.Screens.GameOverScreen;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Screens.MiniGameScreen;

/** This class is used to keep track of the player's progress within
 * the game. It keeps track of how many active fire trucks the user
 * has and how many fortresses have been destroyed and causes the game
 * to end declaring the player has having won or lost
 */

public class GameState {

    /** Number of fire trucks there are on screen */
    private int activeFireTrucks;

    /** The number of fortresses the player has destroyed */

    /** The number of trucks that have a fortress within their attack range */
    private int trucksInAttackRange;
    
    private boolean miniGameEntered = false;

    /** Constructor for GameState */
    public GameState() {
        this.activeFireTrucks = 0;
    }

    /** Adds one to activeFireTrucks, called when a firetruck is spawned */
    public void addFireTruck() {
        this.activeFireTrucks++;
    }

    /** Removes one from activeFireTrucks, called when a firetruck
     * is destroyed */
    public void removeFireTruck() {
        this.activeFireTrucks--;
    }
    
    public void setMinigameEntered(boolean value) {
    	miniGameEntered = value;
    }
    
    public boolean getMinigameEntered() {
    	return miniGameEntered;
    }


    /** Determines whether the game has ended either when a certain
     * number of fortresses have been destroyed or when there are no
     * fire trucks left
     * @param game LibGDX game
     */
    public void hasGameEnded(Kroy game) {
        if (GameScreen.getFortressesAlive() == 0 && !(miniGameEntered)) {
        	miniGameEntered = true;
            //endGame(true, game);
        } else if (this.activeFireTrucks == 0) {
            endGame(false, game);
        }
    }

    /** Triggers the appropriate game over screen depending
     * on if the user has won or lost
     * @param playerWon <code> true </code> if player has won
     *                  <code> false </code> if player has lost
     * @param game LibGDX game
     */
    public static void endGame(Boolean playerWon, Kroy game) {
        if (playerWon) {      	
            game.setScreen(new GameOverScreen(game, true));
        } else {
            game.setScreen(new GameOverScreen(game, false));
            
        }
    }

    public void setTrucksInAttackRange(int number){
        trucksInAttackRange = number;
    }

    public void incrementTrucksInAttackRange(){
        trucksInAttackRange++;
    }

    public int getTrucksInAttackRange(){
        return trucksInAttackRange;
    }


}
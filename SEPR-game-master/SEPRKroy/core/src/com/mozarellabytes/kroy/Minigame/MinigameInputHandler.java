package com.mozarellabytes.kroy.Minigame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.mozarellabytes.kroy.Screens.GameScreen;
import com.mozarellabytes.kroy.Screens.MiniGameScreen;
import com.mozarellabytes.kroy.Utilities.GUI;

public class MinigameInputHandler implements InputProcessor{
	
	private MiniGameScreen gameScreen;

	public MinigameInputHandler(MiniGameScreen miniGameScreen) {
        this.gameScreen = miniGameScreen;
    }
	
	@Override
	public boolean keyDown(int keyCode) {
		switch(keyCode){
		case Input.Keys.UP:
			int currentSelected = MiniGameScreen.fireEngine.getSelectedIndex();
			if(MiniGameScreen.fireEngine.getSelectedIndex() != 0) {
				MiniGameScreen.fireEngine.getAttack(currentSelected-1).setSelected(true);
				MiniGameScreen.fireEngine.getAttack(currentSelected).setSelected(false);
			}
			else {
				MiniGameScreen.fireEngine.getAttack(MiniGameScreen.fireEngine.getMoveList().size()-1).setSelected(true);
				MiniGameScreen.fireEngine.getAttack(currentSelected).setSelected(false);
			}
			break;
		case Input.Keys.DOWN:
			int currentlySelected = MiniGameScreen.fireEngine.getSelectedIndex();
			if(MiniGameScreen.fireEngine.getSelectedIndex() != MiniGameScreen.fireEngine.getMoveList().size()-1) {
				MiniGameScreen.fireEngine.getAttack(currentlySelected+1).setSelected(true);
				MiniGameScreen.fireEngine.getAttack(currentlySelected).setSelected(false);
			}
			else {
				MiniGameScreen.fireEngine.getAttack(0).setSelected(true);
				MiniGameScreen.fireEngine.getAttack(currentlySelected).setSelected(false);
			}
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
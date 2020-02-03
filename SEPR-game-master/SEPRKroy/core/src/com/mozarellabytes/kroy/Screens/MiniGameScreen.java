package com.mozarellabytes.kroy.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.mozarellabytes.kroy.Kroy;
import com.mozarellabytes.kroy.Minigame.Alien;
import com.mozarellabytes.kroy.Minigame.FireEngine;

public class MiniGameScreen implements Screen{

	private Alien alien = new Alien(100, 10, new Vector2(600, 800));
	private FireEngine fireEngine = new FireEngine(100, 10, new Vector2(600, 800));
	
	public MiniGameScreen(Kroy kroy) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println(fireEngine.getHP());
		alien.getAttack(0).performAttack(fireEngine);
		System.out.println(fireEngine.getHP());
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
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

}

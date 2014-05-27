package com.nickan.tankbattle.game;

import com.badlogic.gdx.utils.Array;
import com.nickan.tankbattle.model.Tank;

/**
 * Interface for both server and client, as they should have almost have same interface, only different implementation
 * @author Nickan
 *
 */
public abstract class Updater {
	private GameScreen screen;
	protected Array<Tank> tanks;
	
	
	public Updater(GameScreen screen) {
		this.screen = screen;
		tanks = new Array<Tank>();
	}
	
	public abstract void update(float delta);
	
	
	public abstract void playerMoveLeft();
	
	public abstract void playerMoveRight();
	
	public abstract void playerMoveUp();
	
	public abstract void playerMoveDown();
	
	public abstract void playerStop();
	
	public abstract void fire();
	
	
	public void resize(int width, int height) {
		
	}
	
	public void show() {
		
	}
	
	public void hide() {
		
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void dispose() {
		
	}
	
	public Array<Tank> getTanks() {
		return tanks;
	}
	
	public abstract int[][] getTilesInfo();
	
	public float getCellWidth() {
		return screen.game.getCellWidth();
	}
	
	public float getCellHeight() {
		return screen.game.getCellHeight();
	}
	
}

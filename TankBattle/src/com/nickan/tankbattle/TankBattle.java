package com.nickan.tankbattle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class TankBattle extends Game {
	// Designed for 800 x 480 screen
	// Stands for the division of the screen, making cells
	public static final int WIDTH_DIVISION = 19;
	public static final int HEIGHT_DIVISION = 15;

	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}

	public float getCellWidth() { return Gdx.graphics.getWidth() / WIDTH_DIVISION; }

	public float getCellHeight() { return Gdx.graphics.getHeight() / HEIGHT_DIVISION; }
	
}

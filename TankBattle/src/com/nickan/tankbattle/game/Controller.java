package com.nickan.tankbattle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Responsible for all the controls, same for both client and server
 * @author Nickan
 *
 */
public class Controller {
	Updater updater; // Always being called, so make it a field
	Renderer renderer;
	
	public Controller(Updater updater, Renderer renderer) {
		this.updater = updater;
		this.renderer = renderer;
	}
	
	public void resize(int width, int height) {
		float cellWidth = updater.getCellWidth();
		float cellHeight = updater.getCellHeight();
		
		renderer.moveUp.setPosition(cellWidth * 2, cellHeight * 4);
		renderer.moveUp.setSize(cellWidth * 2, cellHeight * 2);
		
		renderer.moveDown.setPosition(cellWidth * 2, cellHeight * 0);
		renderer.moveDown.setSize(cellWidth * 2, cellHeight * 2);
		
		renderer.moveLeft.setPosition(cellWidth * 0, cellHeight * 2);
		renderer.moveLeft.setSize(cellWidth * 2, cellHeight * 2);
		
		renderer.moveRight.setPosition(cellWidth * 4, cellHeight * 2);
		renderer.moveRight.setSize(cellWidth * 2, cellHeight * 2);
		
		renderer.attack.setPosition(cellWidth * 15, cellHeight);
		renderer.attack.setSize(cellWidth * 4, cellWidth * 4);
		
		initializeMovementButtonListener();
		activateMovementButton();
		
		renderer.attack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				updater.fire();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
			}
		} );
		
		renderer.getStage().addActor(renderer.attack);
		Gdx.input.setInputProcessor(renderer.getStage());
	}
	
	private void initializeMovementButtonListener() {
		renderer.moveUp.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
				updater.playerMoveUp();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
			}
		} );
		
		renderer.moveDown.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
				updater.playerMoveDown();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
			}
		} );
		
		renderer.moveLeft.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
				updater.playerMoveLeft();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
			}
		} );
		
		renderer.moveRight.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
				updater.playerMoveRight();
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				updater.playerStop();
			}
		} );
	}
	
	private void activateMovementButton() {
		Stage stage = renderer.getStage();
		
		stage.addActor(renderer.moveUp);
		stage.addActor(renderer.moveDown);
		stage.addActor(renderer.moveLeft);
		stage.addActor(renderer.moveRight);
	}

}

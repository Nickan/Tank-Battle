package com.nickan.tankbattle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nickan.framework.Animation;
import com.nickan.framework.Animation.PlayMode;
import com.nickan.tankbattle.TankBattle;
import com.nickan.tankbattle.model.Bullet;
import com.nickan.tankbattle.model.Tank;

/**
 * Interface for both server and client, as they should have almost have same interface, only different implementation
 * @author Nickan
 *
 */
public class Renderer {
	private Updater updater;
	
	private Animation aniTank;
	Button attack;
	Button moveUp;
	Button moveDown;
	Button moveLeft;
	Button moveRight;
	
	private Skin skin;
	private TextureAtlas atlas;
	private Stage stage;
	
	private OrthographicCamera orthoCam;
	
	public Renderer(Updater updater) {
		this.updater = updater;
	}
	
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();
		batch.setProjectionMatrix(orthoCam.combined);
		batch.begin();
		
		drawStageBattle(batch);
		
		for (Tank tank: updater.getTanks()) {
			Vector2 pos = tank.getPosition();
			Vector2 size = tank.getSize();
			Vector2 view = tank.getView();
			
			batch.draw(aniTank.getKeyFrame(tank.getAnimationTime()), pos.x, pos.y, 
					size.x / 2, size.x / 2, size.x, size.y, 1.0f, 1.0f, (float) Math.toDegrees(Math.atan2(view.y, view.x)) );
			
			drawBullets(batch, tank.getBullets());
		}
		
		batch.end();
		
		stage.act(delta);
		stage.draw();
	}
	
	private void drawBullets(SpriteBatch batch, Bullet[] bullets) {
		for (int index = 0; index < bullets.length; ++index) {
			Bullet tempBullet = bullets[index];
			
			if (tempBullet.isFired()) {
				Vector2 pos = tempBullet.getPosition();
				Vector2 size = tempBullet.getSize();
				Vector2 view = tempBullet.getView();
				
				batch.draw(skin.getRegion("bullet"), pos.x - size.x / 2, pos.y - size.y / 2, 
						size.x / 2, size.x / 2, size.x, size.y, 1.0f, 1.0f, (float) Math.toDegrees(Math.atan2(view.y, view.x)) );
			}
		}
	}
	
	private void drawStageBattle(SpriteBatch batch) {
		int[][] tileInfo = updater.getTilesInfo();
		if (tileInfo != null) {
			
			for (int row = 0; row < tileInfo.length; ++row) {
				for (int col = 0; col < tileInfo[row].length; ++col) {
					int tileNumber = tileInfo[row][col];

					switch (tileNumber) {
					case 0:
						
						break;
					case 1:
						batch.draw(skin.getRegion("wood"), col / 4f, row / 4f, 0, 0, 0.25f,
								0.25f, 1, 1, 0);
						break;
					case 2:
						batch.draw(skin.getRegion("metal"), col / 4f, row / 4f, 0, 0, 0.5f,
								0.5f, 1, 1, 0);
							++col;	// Skip the next tile
						break;
					case 4: 
						batch.draw(skin.getRegion("forest"), col / 4f, row / 4f, 0, 0, 0.5f,
								0.5f, 1, 1, 0);
						++col; // Skip the next tile
						break;
					case 6: 
						batch.draw(skin.getRegion("water"), col / 4f, row / 4f, 0, 0, 1,
								1, 1, 1, 0);
						col += 3; // Skip the next three tiles
						break;
						default: 
							break;
					}

					
				}
			}
		}
	}

	
	public void resize(int width, int height) {
		if (stage == null) {
			stage = new Stage(width, height, true);
		}
		stage.setViewport(width, height);
		stage.clear();
		
		orthoCam = new OrthographicCamera(width / updater.getCellWidth(), height / updater.getCellHeight());
		
		// Centering the position of the camera
		orthoCam.position.set( (TankBattle.WIDTH_DIVISION / 2) - 2f, (TankBattle.HEIGHT_DIVISION / 2), 0);
		orthoCam.update();
		
		stage.getSpriteBatch().setProjectionMatrix(orthoCam.combined);
	}

	
	public void show() {
		atlas = new TextureAtlas("gamegraphics.pack");
		skin = new Skin(atlas);
		
		aniTank = new Animation(new Texture(Gdx.files.internal("tanksheet.png"), true), 0, 0, 32, 32, 8, 8, 1.0f);
		aniTank.setPlayMode(PlayMode.LOOP);
		
		attack = new Button(skin.getDrawable("attack"));
		moveUp = new Button(skin.getDrawable("forwardnormal"), skin.getDrawable("forwardpressed"));
		moveDown = new Button(skin.getDrawable("backwardnormal"), skin.getDrawable("backwardpressed"));
		moveLeft = new Button(skin.getDrawable("leftnormal"), skin.getDrawable("leftpressed"));
		moveRight = new Button(skin.getDrawable("rightnormal"), skin.getDrawable("rightpressed"));
	}

	
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	
	public void dispose() {
		skin.dispose();
		stage.dispose();
	}
	
	
	public final Stage getStage() {
		return stage;
	}
	
	
}

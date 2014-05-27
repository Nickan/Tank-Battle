package com.nickan.tankbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MenuScreen implements Screen {
	private TankBattle game;

	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private BitmapFont arial;
	
	private Button create;
	private Button join;
	private ShaderProgram shaderProgram;
	
	public MenuScreen(TankBattle game) {
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		
		stage.act();
		stage.draw();
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();
		batch.begin();
		
		// FIXME Test later if the distance font is not having error on Android, still don't have modified arial.fnt
//		batch.setShader(shaderProgram);
		arial.setScale(2.5f);
		arial.draw(batch, "Tank battle!", game.getCellWidth() * 8.2f, game.getCellHeight() * 13.0f);
//		batch.setShader(null);
		
		arial.setScale(1.0f);
		arial.draw(batch, "Create", create.getX() + (game.getCellWidth() / 3.0f), create.getY() + game.getCellHeight());
		arial.draw(batch, "Join", join.getX() + (game.getCellWidth() / 2.0f), join.getY() + game.getCellHeight());
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if (stage == null) {
			stage = new Stage(width, height, true);
		}
		stage.setViewport(width, height);
		stage.clear();
		
		initializeButtons();	
	}
	
	private void initializeButtons() {
		create = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		create.setPosition(game.getCellWidth() * 9, game.getCellHeight() * 9);
		create.setSize(game.getCellWidth() * 2.5f, game.getCellHeight() * 1.5f);
		create.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				game.getScreen().dispose();
				game.setScreen(new CreateScreen(game));
				return true;
			}
		});
		
		join = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		join.setPosition(game.getCellWidth() * 13, game.getCellHeight() * 9);
		join.setSize(game.getCellWidth() * 2.5f, game.getCellHeight() * 1.5f);
		join.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				game.getScreen().dispose();
				game.setScreen(new JoinScreen(game));
				return true;
			}
		});
		
		stage.addActor(create);
		stage.addActor(join);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
		atlas = new TextureAtlas("gamegraphics.pack");
		skin = new Skin(atlas);
		
		// Will use Distance font later
		Texture texture = new Texture(Gdx.files.internal("arial.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		arial = new BitmapFont(Gdx.files.internal("arial.fnt"), new TextureRegion(texture), false);
		arial.setUseIntegerPositions(false);
		
		shaderProgram = new ShaderProgram(Gdx.files.internal("font.vert"), Gdx.files.internal("font.frag"));
		
		if (!shaderProgram.isCompiled()) {
		    Gdx.app.error("fontShader", "compilation failed:\n" + shaderProgram.getLog());
		}
	}

	@Override
	public void hide() {
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
	public void dispose() {
		arial.dispose();
		stage.dispose();
	}
	
	
	
}

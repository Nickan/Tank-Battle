package com.nickan.tankbattle;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.kryonet.Server;
import com.nickan.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.tankbattle.game.GameScreen;
import com.nickan.tankbattle.model.Tank.TankType;
import com.nickan.tankbattle.packet.clientpackets.ClientPacket;
import com.nickan.tankbattle.packet.clientpackets.CreateClientTank;
import com.nickan.tankbattle.packet.clientpackets.NeedAllTankInfo;
import com.nickan.tankbattle.packet.clientpackets.TouchCommand;
import com.nickan.tankbattle.packet.serverpackets.ClientTankCreated;
import com.nickan.tankbattle.packet.serverpackets.ReplicateServerTank;
import com.nickan.tankbattle.packet.serverpackets.MovementSpeed;
import com.nickan.tankbattle.packet.serverpackets.Position;
import com.nickan.tankbattle.packet.serverpackets.ServerPacket;
import com.nickan.tankbattle.packet.serverpackets.TilesInformation;
import com.nickan.tankbattle.packet.serverpackets.View;

public class CreateScreen implements Screen {
	private TankBattle game;
	
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private BitmapFont arial;
	
	private Button start;
	
	private Server server;

	public CreateScreen(TankBattle game) {
		this.game = game;
		
		createServer();
	}
	
	private void createServer() {
		server = new Server();
		server.start();
		
		// Register the packet
		server.getKryo().register(ClientPacket.class);
		server.getKryo().register(NeedAllTankInfo.class);
		server.getKryo().register(CreateClientTank.class);
		server.getKryo().register(TouchCommand.class);
		server.getKryo().register(MessageType.class);
		
		//...
		server.getKryo().register(int[][].class);
		server.getKryo().register(int[].class);
		server.getKryo().register(TilesInformation.class);
		
		server.getKryo().register(TankType.class);
		server.getKryo().register(ServerPacket.class);
		server.getKryo().register(ReplicateServerTank.class);
		server.getKryo().register(Position.class);
		server.getKryo().register(View.class);
		server.getKryo().register(MovementSpeed.class);
		server.getKryo().register(ClientTankCreated.class);
		
		try {
			server.bind(54555, 54777);
		} catch (IOException e) {
			Gdx.app.error("ServerScreen", "Can't bind server");
		}
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		
		// Update and draw the separate stage's actors
		stage.act();
		stage.draw();
		
		// Just get the existing stage's SpriteBatch to use to draw the arial BitmapFont
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();
		batch.begin();
		arial.draw(batch, "Start!", start.getX() + (game.getCellWidth() / 3.0f), start.getY() + game.getCellHeight());
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
		Gdx.input.setInputProcessor(stage);
	}
	
	private void initializeButtons() {
		// Set the button's textures, position and the size
		start = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		start.setPosition(game.getCellWidth() * 9, game.getCellHeight() * 9);
		start.setSize(game.getCellWidth() * 2.5f, game.getCellHeight() * 1.5f);
		
		start.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				// Dispose the current screen and change to GameScreen
				game.getScreen().dispose();
				game.setScreen(new GameScreen(game, server));
				return true;
			}
		});
		
		// Add it to the stage for it to listen for the input
		stage.addActor(start);
	}

	@Override
	public void show() {
		// Initialize textures here when the screen is shown
		atlas = new TextureAtlas("gamegraphics.pack");
		skin = new Skin(atlas);
		arial = new BitmapFont(Gdx.files.internal("arial.fnt"));
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
	
	public Server getServer() { return server; }

}

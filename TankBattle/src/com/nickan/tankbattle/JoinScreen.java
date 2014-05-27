package com.nickan.tankbattle;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
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
import com.esotericsoftware.kryonet.Client;
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

public class JoinScreen implements Screen {
	private TankBattle game;
	
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private BitmapFont arial;
	
	private Client client;
	private Button join;

	public JoinScreen(TankBattle game) {
		this.game = game;
		
		initializeClient();
	}
	
	private void initializeClient() {
		client = new Client();
		client.start();
		
		client.getKryo().register(ClientPacket.class);
		client.getKryo().register(NeedAllTankInfo.class);
		client.getKryo().register(CreateClientTank.class);
		client.getKryo().register(TouchCommand.class);
		client.getKryo().register(MessageType.class);
		
		//...
		client.getKryo().register(int[][].class);
		client.getKryo().register(int[].class);
		client.getKryo().register(TilesInformation.class);

		client.getKryo().register(TankType.class);
		client.getKryo().register(ServerPacket.class);
		client.getKryo().register(ReplicateServerTank.class);
		client.getKryo().register(Position.class);
		client.getKryo().register(View.class);
		client.getKryo().register(MovementSpeed.class);
		client.getKryo().register(ClientTankCreated.class);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		
		stage.act();
		stage.draw();
		SpriteBatch batch = (SpriteBatch) stage.getSpriteBatch();
		batch.begin();
		arial.draw(batch, "Enter IP", join.getX() + (game.getCellWidth() / 3.0f), join.getY() + game.getCellHeight());
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if (stage == null) {
			stage = new Stage(width, height, true);
		}
		stage.clear();
		
		initializeButtons();
		Gdx.input.setInputProcessor(stage);
	}
	
	private void initializeButtons() {
		join = new Button(skin.getDrawable("equipmentslotnormal"), skin.getDrawable("equipmentslotpressed"));
		join.setPosition(game.getCellWidth() * 9, game.getCellHeight() * 9);
		join.setSize(game.getCellWidth() * 2.5f, game.getCellHeight() * 1.5f);
		join.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int button, int pointer) {
				ipAddressInput();
				return true;
			}
		});
		
		stage.addActor(join);
	}
	
	private void ipAddressInput() {
		Gdx.input.getTextInput(new TextInputListener() {

			@Override
			public void input(String text) {
				if (clientConnected(text)) {
					// Dispose the current screen and make a Game screen
					game.getScreen().dispose();
					game.setScreen(new GameScreen(game, client));
				} else {
					
				}
			}

			@Override
			public void canceled() {
				// TODO Auto-generated method stub
				
			}
			
		}, "Enter IP Address", "");
	}

	@Override
	public void show() {
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
	
	/*
	 * Helper methods
	 */
	
	/**
	 * Returns whether the client has successfully connected
	 * @param ipAddress - The IP Address of the server
	 * @return
	 */
	public boolean clientConnected(String ipAddress) {
		try {
			client.connect(5000, ipAddress, 54555, 54777);
			return true;
		} catch (IOException e) {
			Gdx.app.error("Join Screen", "Can't connect");
		}
		return false;
	}

}

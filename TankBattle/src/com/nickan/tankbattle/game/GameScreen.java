package com.nickan.tankbattle.game;

import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.nickan.tankbattle.TankBattle;

/**
 * It is really hard to follow the MVC Pattern for Stage class, as it is tightly coupled, it knows how to update and draw
 * itself.
 * @author Nickan
 *
 */
public class GameScreen implements Screen {
	TankBattle game;
	
	Updater updater;
	Renderer renderer;
	Controller controller;

	
	public GameScreen(TankBattle game, Server server) {
		this.game = game;
		
		updater = new ServerUpdater(this, server);
		renderer = new Renderer(updater);
		controller = new Controller(updater, renderer);
	}
	
	public GameScreen(TankBattle game, Client client) {
		this.game = game;

		updater = new ClientUpdater(this, client);
		renderer = new Renderer(updater);
		controller = new Controller(updater, renderer);
	}
	

	@Override
	public void render(float delta) {
		updater.update(delta);
		renderer.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		updater.resize(width, height);
		renderer.resize(width, height);
		controller.resize(width, height);
	}

	@Override
	public void show() {
		updater.show();
		renderer.show();
	}

	@Override
	public void hide() {
		updater.hide();
		renderer.hide();
	}

	@Override
	public void pause() {
		updater.pause();
		renderer.pause();
	}

	@Override
	public void resume() {
		updater.resume();
		renderer.resume();
	}

	@Override
	public void dispose() {
		updater.dispose();
		renderer.dispose();
	}

}

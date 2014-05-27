package com.nickan.tankbattle.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.nickan.framework.finitestatemachine.messagingsystem.MessageDispatcher;
import com.nickan.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.tankbattle.model.Bullet;
import com.nickan.tankbattle.model.Tank;
import com.nickan.tankbattle.model.Tank.TankType;
import com.nickan.tankbattle.model.level.StageBattle;
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

public class ServerUpdater extends Updater {
	private Server server;
	
	ServerPacket serverPacket;
	ClientPacket clientPacket;
	private Tank serverPlayer;
	
	// FIXME The client number is not final
	private static final int CLIENT_MAX = 5;
	private int[] clientIdPlayers = new int[CLIENT_MAX];
	
	private Pool<MovementSpeed> poolMovements;
	private Pool<Position> poolPositions;
	private Pool<View> poolViews;
	
	StageBattle stageBattle;
	
	public ServerUpdater(GameScreen screen, Server server) {
		super(screen);
		this.server = server;
		
		serverPlayer = new Tank(new Vector2(4, 0), new Vector2(1.0f, 1.0f),
				new Vector2(Vector2.Y), 2f, TankType.SERVER);
		MessageDispatcher.sendMessage(-1, serverPlayer.getId(), 0,
				MessageType.PLAYER_CONTROL, null);
		tanks.add(serverPlayer);
		
		serverPacket = new ServerPacket();
		clientPacket = new ClientPacket();
		initializeServerListener();
		
		initializePools();
		
		clearClientIds();
		
		stageBattle = new StageBattle();
	}
	
	private void clearClientIds() {
		for (int index = 0; index < clientIdPlayers.length; ++index) {
			clientIdPlayers[index] = -10;	// -10 means invalid client id
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return - Whether setting the client id registration is successful
	 */
	private boolean setClientId(int id) {
		for (int index = 0; index < clientIdPlayers.length; ++index) {
			if (clientIdPlayers[index] != -10) {
				clientIdPlayers[index] = id;
				return true;
			}
		}
		return false;
	}
	
	private void initializePools() {
		poolMovements = new Pool<MovementSpeed>() {

			@Override
			protected MovementSpeed newObject() {
				// TODO Auto-generated method stub
				return new MovementSpeed();
			}
			
		};
		
		poolPositions = new Pool<Position>() {

			@Override
			protected Position newObject() {
				// TODO Auto-generated method stub
				return new Position();
			}
			
		};
		
		poolViews = new Pool<View>() {

			@Override
			protected View newObject() {
				// TODO Auto-generated method stub
				return new View();
			}
			
		};
	}
	
	private void initializeServerListener() {
		
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof ClientPacket) {
					
					if (object instanceof NeedAllTankInfo) {
						NeedAllTankInfo tankInfo = (NeedAllTankInfo) object;
						sendAllTanksInfo(connection, tankInfo);
						
						// Send the tiles info too.
						sendAllTilesInfo();
						
						
					} else if (object instanceof CreateClientTank) {
						CreateClientTank create = (CreateClientTank) object;
						sendClientTankInfo(connection, create);
					} else if (object instanceof TouchCommand) {
						TouchCommand command = (TouchCommand) object;
						MessageDispatcher.sendMessage(-1, command.tankId, 0, command.type, null);
					}
				}
			}
		});
	}
	
	private void sendAllTilesInfo() {
		/*
		TilesInformation info = new TilesInformation();
		
		int[][] tilesInfo = stageBattle.getAllTilesInfo();
		
		// Cuts the tiles information into four pieces
		int totalRow = tilesInfo.length;
		int totalCol = tilesInfo[0].length;
		
		// Lower left part
		int[][] halfStage1 = new int[totalRow / 2][totalCol / 2];
		for (int row = 0; row < totalRow / 2; ++row) {
			for (int col = 0; col < totalCol; ++ col) { 
				halfStage1[row][col] = tilesInfo[row][col];
			}
		}
		info.part = 1;
		info.tilesInfo = halfStage1;
		info.totalCols = totalCol;
		info.totalRows = totalRow;
		server.sendToAllTCP(info);
		
		TilesInformation info2 = new TilesInformation();
		int[][] halfStage2 = new int[totalRow / 2][totalCol];
		for (int row = 0; row < totalRow / 2; ++row) {
			for (int col = 0; col < totalCol; ++ col) { 
				halfStage2[row ][col] = tilesInfo[row + totalRow / 2][col + totalCol / 2];
			}
		}
		info2.part = 2;
		info2.tilesInfo = halfStage2;
		info2.totalCols = totalCol;
		info2.totalRows = totalRow;
		server.sendToAllTCP(info2);
		*/
	}
	
	private void sendAllTanksInfo(Connection connection, NeedAllTankInfo info) {
		for (int index = 0; index < tanks.size; ++index) {
			Tank tempTank = tanks.get(index);
			
			ReplicateServerTank create = new ReplicateServerTank();
			create.tankId = tempTank.getId();
			create.x = tempTank.getPosition().x;
			create.y = tempTank.getPosition().y;
			create.viewX = tempTank.getView().x;
			create.viewY = tempTank.getView().y;
			create.movementSpeed = tempTank.getMovementSpeed();
			create.tankType = tempTank.getTankType();
			
			server.sendToTCP(connection.getID(), create);
		}
	}
	
	/**
	 * Sends the information of the tank created for client
	 * @param connection
	 * @param create
	 */
	private void sendClientTankInfo(Connection connection, CreateClientTank create) {
		Tank clientTank = new Tank(new Vector2(3.0f, 3.0f), new Vector2(1.0f, 1.0f), 
				new Vector2(Vector2.Y), 2.0f, TankType.CLIENT);
		MessageDispatcher.sendMessage(-1, clientTank.getId(), 0,
				MessageType.PLAYER_CONTROL, null);
		
		setClientId(clientTank.getId());
		tanks.add(clientTank);
		
		ClientTankCreated clientCreated = new ClientTankCreated();
		clientCreated.tankId = clientTank.getId();
		server.sendToTCP(connection.getID(), clientCreated);
	}
	
	@Override
	public void update(float delta) {
		for (Tank tank: tanks) {
			tank.update(delta);
			updateBullets(tank.getBullets());
			
			handleSendingPacketsToClients(tank);
		}
		MessageDispatcher.update(delta);
	}
	
	
	private void updateBullets(Bullet[] bullets) {
		for (int index = 0; index < bullets.length; ++index) {
			Bullet tempBullet = bullets[index];
			
			if (tempBullet.isFired()) {
				if (stageBattle.bulletValid(tempBullet.getPosition(), tempBullet.getView(), tempBullet.upgrade)) {
					if (stageBattle.bulletHit(tempBullet.getPosition(), tempBullet.getView(), tempBullet.upgrade)) {
						tempBullet.setFired(false);
					}
				}
			}
		}
	}
	
	
	/**
	 * Handles all about sending packets to clients
	 * @param tank
	 */
	private void handleSendingPacketsToClients(Tank tank) {
		// FIXME not satisfied of what I did here, it gets the job done but I think is ugly, fix later
		
		// Moving
		if (tank.getMovementSpeed() != 0) {
			if (!tank.setMoving) {
				tank.setMoving = true;
				tankMoving(tank);
			}
			
		} else {
			// Not moving
			if (tank.setMoving) {
				tankStopped(tank);
			}
			tank.setMoving = false;
		}
	}
	
	/**
	 * Will be called once every time the tank is moving
	 * @param tank
	 */
	private void tankMoving(Tank tank) {
		sendMovementSpeed(tank.getId(), tank.getMovementSpeed());
		sendViewVector(tank.getId(), tank.getView());
		sendPosition(tank.getId(), tank.getPosition()); // Just to make sure it starts at the same position
	}
	
	/**
	 * Will be called once every time the tank has stopped
	 * @param tank
	 */
	private void tankStopped(Tank tank) {
		sendMovementSpeed(tank.getId(), 0);
		sendPosition(tank.getId(), tank.getPosition()); // Just to make sure it ends at the same position
	}
	
	
	private void sendMovementSpeed(int tankId, float movementSpeed) {
		MovementSpeed tempMove = poolMovements.obtain();
		tempMove.tankId = tankId;
		
		// Send the movement
		tempMove.x = movementSpeed;
		server.sendToAllTCP(tempMove);
		poolMovements.free(tempMove);
	}
	
	private void sendViewVector(int tankId, Vector2 view) {
		// Send the view vector
		View tempView = poolViews.obtain();
		tempView.tankId = tankId;

		tempView.x = view.x;
		tempView.y = view.y;
		server.sendToAllTCP(tempView);
		poolViews.free(tempView);
	}
	
	private void sendPosition(int tankId, Vector2 position) {
		Position pos = poolPositions.obtain();
		pos.tankId = tankId;
		
		pos.x = position.x;
		pos.y = position.y;
		server.sendToAllTCP(pos);
		poolPositions.free(pos);
	}
	
	
	
	
	/*
	 *  Tank controls, encapsulated so that if the control system has changed, nothing here
	 *  would be affected
	 */

	@Override
	public void playerMoveLeft() {
		MessageDispatcher.sendMessage(-1, serverPlayer.getId(), 0, MessageType.MOVE_LEFT, null);
	}

	@Override
	public void playerMoveRight() {
		MessageDispatcher.sendMessage(-1, serverPlayer.getId(), 0, MessageType.MOVE_RIGHT, null);
	}

	@Override
	public void playerMoveUp() {
		MessageDispatcher.sendMessage(-1, serverPlayer.getId(), 0, MessageType.MOVE_UP, null);
	}

	@Override
	public void playerMoveDown() {
		MessageDispatcher.sendMessage(-1, serverPlayer.getId(), 0, MessageType.MOVE_DOWN, null);
	}

	@Override
	public void playerStop() {
		MessageDispatcher.sendMessage(-1, serverPlayer.getId(), 0, MessageType.STOP, null);
	}
	
	@Override
	public void fire() {
		MessageDispatcher.sendMessage(-1, serverPlayer.getId(), 0, MessageType.FIRE, null);
	}

	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int[][] getTilesInfo() {
		return stageBattle.getAllTilesInfo();
	}


}

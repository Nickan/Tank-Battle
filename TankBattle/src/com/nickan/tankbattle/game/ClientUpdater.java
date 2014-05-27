package com.nickan.tankbattle.game;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.nickan.framework.finitestatemachine.messagingsystem.Message.MessageType;
import com.nickan.tankbattle.model.Tank;
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


/**
 * Only sends touch event response and get information of the game from server.
 * @author Nickan
 *
 */
public class ClientUpdater extends Updater {
	Client client;
	private int tankId = -10; // Invalid tank id
	private int[][] tilesInfo = null;
	
	public ClientUpdater(GameScreen screen, Client client) {
		super(screen);
		this.client = client;
		
		initializeClientListener();
		
		CreateClientTank create = new CreateClientTank();
		client.sendTCP(create);
		
		NeedAllTankInfo info = new NeedAllTankInfo();
		info.tankId = tankId;
		client.sendTCP(info);
	}
	
	private void initializeClientListener() {
		client.addListener(new Listener() {
			public void received(Connection connect, Object object) {			
				if (object instanceof ServerPacket) {
					Tank tempTank;
					if (object instanceof ReplicateServerTank) {
						ReplicateServerTank create = (ReplicateServerTank) object;
						createNewTank(create);
					} else if (object instanceof Position) {
						Position p = (Position) object;
						tempTank = getTank(p.tankId);
						
						if (tempTank != null) {
							tempTank.setPosition(p.x, p.y);
						}
						
					} else if (object instanceof View) {
						View v = (View) object;
						tempTank = getTank(v.tankId);
						
						if (tempTank != null) {
							tempTank.setView(v.x, v.y);
						}
						
					} else if (object instanceof MovementSpeed) {
						MovementSpeed m = (MovementSpeed) object;
						tempTank = getTank(m.tankId);
						
						if (tempTank != null) {
							tempTank.setMovementSpeed(m.x);
						}
						
						
					} else if (object instanceof ClientTankCreated) {
						ClientTankCreated create = (ClientTankCreated) object;
						tankId = create.tankId;
					}
				}
				
				if (object instanceof TilesInformation) {
					TilesInformation info = (TilesInformation) object;
					if (info.part == 1) {
						tilesInfo = new int[info.totalRows][info.totalCols];
						
						int[][] firstPart = info.tilesInfo;
						
						for (int row = 0; row < firstPart.length; ++row) {
							for (int col = 0; col < firstPart[0].length; ++col) {
								tilesInfo[row][col] = firstPart[row][col];
							}
						}
					} else {
						// Second part
						int[][] secondPart = info.tilesInfo;
						
						for (int row = 0; row < secondPart.length; ++row) {
							for (int col = 0; col < secondPart[0].length; ++col) {
								tilesInfo[row + secondPart.length][col] = secondPart[row][col];
							}
						}
					}
					
					
				}
			}
		});
	}
	
	private void createNewTank(ReplicateServerTank create) {
		Tank newTank = new Tank(new Vector2(create.x, create.y), new Vector2(1.0f, 1.0f), 
				new Vector2(create.viewX, create.viewY), 1.0f, create.tankType);
		newTank.setId(create.tankId);
		newTank.setClientState();
		newTank.setMovementSpeed(create.movementSpeed);
		tanks.add(newTank);
	}
	
	
	
	@Override
	public void update(float delta) {
		for (Tank tempTank: tanks) {
			tempTank.update(delta);
		}
	}

	@Override
	public void playerMoveLeft() {
		TouchCommand command = new TouchCommand();
		command.tankId = tankId;
		command.type = MessageType.MOVE_LEFT;
		client.sendTCP(command);
	}

	@Override
	public void playerMoveRight() {
		TouchCommand command = new TouchCommand();
		command.tankId = tankId;
		command.type = MessageType.MOVE_RIGHT;
		client.sendTCP(command);
	}

	@Override
	public void playerMoveUp() {
		TouchCommand command = new TouchCommand();
		command.tankId = tankId;
		command.type = MessageType.MOVE_UP;
		client.sendTCP(command);
	}

	@Override
	public void playerMoveDown() {
		TouchCommand command = new TouchCommand();
		command.tankId = tankId;
		command.type = MessageType.MOVE_DOWN;
		client.sendTCP(command);
	}

	@Override
	public void playerStop() {
		TouchCommand command = new TouchCommand();
		command.tankId = tankId;
		command.type = MessageType.STOP;
		client.sendTCP(command);
	}
	
	@Override
	public void fire() {
		
	}

	/**
	 * 
	 * @param id
	 * @return The tank that has the id, null if none
	 */
	private Tank getTank(int id) {
		for (int index = 0; index < tanks.size; ++index) {
			Tank tempTank = tanks.get(index);
			if (tempTank.getId() == id) {
				return tempTank;
			}
		}
		return null;
	}
	
	public int[][] getTilesInfo() {
		return tilesInfo;
	}

}

package com.nickan.tankbattle.packet.serverpackets;

import com.nickan.tankbattle.model.Tank.TankType;


public class ReplicateServerTank extends ServerPacket {
	public float x;
	public float y;
	public float viewX;
	public float viewY;
	public float movementSpeed;
	public TankType tankType;
}

package com.nickan.framework.finitestatemachine.messagingsystem;

public class Message {
	// Edit the message type, will find a way to make it more flexible
	public enum MessageType { PLAYER_CONTROL, MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, STOP, FIRE }; // Player controls
	
	public int senderId;
	public int receiverId;
	public float dispatchTime;
	public MessageType type;
	public Object extraInfo;
	
	public Message() {
		this.senderId = 0;
		this.receiverId = 0;
		this.dispatchTime = 0;
		this.type = null;
		this.extraInfo = null;
	}
	
}

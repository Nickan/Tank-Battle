package com.nickan.framework.finitestatemachine;

import com.nickan.framework.finitestatemachine.messagingsystem.Message;

/**
 * Should be the ancestor to implement the FSM. Automatically creates an ID and interface for handling message
 * 
 * @author Nickan
 *
 */
public abstract class BaseEntity {
	protected int id;
	
	public BaseEntity() {
		this.id = EntityManager.getAssignedId(this);
	}
	
	public final int getId() {
		return id;
	}
	
	public abstract boolean handleMessage(Message message);
}

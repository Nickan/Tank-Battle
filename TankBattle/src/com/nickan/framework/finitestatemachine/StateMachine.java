package com.nickan.framework.finitestatemachine;

import com.nickan.framework.finitestatemachine.messagingsystem.Message;


public class StateMachine<EntityType> {
	EntityType entity;
	BaseState<EntityType> currentState;
	BaseState<EntityType> globalState;

	public StateMachine(EntityType entity, BaseState<EntityType> currentState, BaseState<EntityType> globalState) {
		this.entity = entity;
		this.currentState = currentState;
		this.globalState = globalState;
		
		currentState.start(entity);
		if (globalState != null) {
			globalState.start(entity);
		}
	}
	
	public void update(float delta) {
		// Global state might be null
		if (globalState != null) {
			globalState.update(entity, delta);
		}
		currentState.update(entity, delta);
	}
	
	public void changeState(BaseState<EntityType> state) {
		// Exit from the current state
		currentState.exit(entity);
		
		// Start the new state
		state.start(entity);
		
		// Set the current state to be the new state
		currentState = state;
	}
	
	public boolean handleMessage(Message message) {
		// Check if the current state can handle the message
		if (currentState.handleMessage(entity, message)) {
			return true;
		}
		
		// Global state might be null
		if (globalState != null) {
			if (globalState.handleMessage(entity, message)) {
				return true;
			}
		}
		return false;
	}
	
	public String getCurrentStateName() { return currentState.getStateName(); }
}

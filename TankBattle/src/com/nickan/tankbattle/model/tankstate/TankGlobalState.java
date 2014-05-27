package com.nickan.tankbattle.model.tankstate;

import com.nickan.framework.finitestatemachine.BaseState;
import com.nickan.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.tankbattle.model.Tank;

public class TankGlobalState implements BaseState<Tank> {
	private static final TankGlobalState instance = new TankGlobalState();
	
	private TankGlobalState() {}
	
	@Override
	public void start(Tank entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Tank entity, float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Tank entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage(Tank entity, Message message) {
		switch (message.type) {
		case PLAYER_CONTROL:
			entity.changeState(TankPlayerControlledState.getInstance());
			return true;
		default:
			break;
		}
		return false;
	}

	@Override
	public String getStateName() { return "Global State"; }
	
	public static final BaseState<Tank> getInstance() { return instance; }
	
}

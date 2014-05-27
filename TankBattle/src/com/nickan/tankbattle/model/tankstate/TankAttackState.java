package com.nickan.tankbattle.model.tankstate;

import com.nickan.framework.finitestatemachine.BaseState;
import com.nickan.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.tankbattle.model.Tank;

public class TankAttackState implements BaseState<Tank> {
	private static final TankAttackState instance = new TankAttackState();
	
	private TankAttackState() {}

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
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getStateName() { return "Attack State"; }

	public static BaseState<Tank> getInstance() { return instance; }

	
}

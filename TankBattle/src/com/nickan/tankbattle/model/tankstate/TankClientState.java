package com.nickan.tankbattle.model.tankstate;

import com.badlogic.gdx.math.Vector2;
import com.nickan.framework.finitestatemachine.BaseState;
import com.nickan.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.tankbattle.model.Tank;

public class TankClientState implements BaseState<Tank> {
	private static final TankClientState instance = new TankClientState();
	
	private static final Vector2 tmpVec = new Vector2();
	
	private TankClientState() { }

	@Override
	public void start(Tank entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Tank entity, float delta) {
		updateMovement(entity, delta);
		handleAnimationTimer(entity, delta);
	}
	
	private void updateMovement(Tank entity, float delta) {
		// If moving
		if (entity.getMovementSpeed() != 0) {
			Vector2 tempPos = entity.getPosition();
			
			// The velocity
			tmpVec.set(entity.getView()).scl(entity.getMovementSpeed() * delta);
			tempPos.add(tmpVec);
		}
	}
	
	private void handleAnimationTimer(Tank entity, float delta) {
		// If the entity is not moving
		if (entity.getMovementSpeed() != 0) {
			// Add the animation times
			entity.setAnimationTime(entity.getAnimationTime() + delta);

			// If the animation time is through
			if (entity.getAnimationTime() >= entity.getAnimationDuration()) {
				/*
				 * Just subtract the animation duration to the current animation
				 * time so that it will add the excess time to the next update
				 * of the animation time
				 */
				entity.setAnimationTime(entity.getAnimationTime()
						- entity.getAnimationDuration());
			}
		}
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
	public String getStateName() {
		// TODO Auto-generated method stub
		return "Client State";
	}
	
	public static final BaseState<Tank> getInstance() { return instance; }

}

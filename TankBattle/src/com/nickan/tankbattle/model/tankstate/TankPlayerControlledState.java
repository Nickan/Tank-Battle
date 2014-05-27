package com.nickan.tankbattle.model.tankstate;

import com.badlogic.gdx.math.Vector2;
import com.nickan.framework.Maths;
import com.nickan.framework.finitestatemachine.BaseState;
import com.nickan.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.tankbattle.model.Bullet;
import com.nickan.tankbattle.model.Tank;

public class TankPlayerControlledState implements BaseState<Tank> {
	private static final TankPlayerControlledState instance = new TankPlayerControlledState();
	private static final Vector2 tmpVec1 = new Vector2();
	private static final Vector2 tmpVec2 = new Vector2();
	
	private TankPlayerControlledState() { }
	
	@Override
	public void start(Tank entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Tank entity, float delta) {
		// Interpolated movement
		interpolateMovement(entity, delta);
		handleAnimationTimer(entity, delta);
		
		updateBullet(entity.getBullets(), delta);
	}
	
	private void updateBullet(Bullet[] bullets, float delta) {
		for (int index = 0; index < bullets.length; ++index) {
			Bullet tempBullet = bullets[index];
			
			// Don't update the position if it is not fired
			if (!tempBullet.isFired()) {
				continue;
			}
			tmpVec1.set(tempBullet.getView()).scl(tempBullet.getMovementSpeed() * delta);
			tmpVec2.set(tempBullet.getPosition());
			tmpVec1.add(tmpVec2);
			tempBullet.setPosition(tmpVec1);
		}
	}
	
	private void interpolateMovement(Tank entity, float delta) {
		tmpVec1.set(entity.getView()).scl(entity.getMovementSpeed() * delta);
		tmpVec2.set(entity.getPosition());
		tmpVec1.add(tmpVec2);
		entity.setPosition(tmpVec1);
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
		float halfCell = 0.5f;
		
		tmpVec1.set(Vector2.Zero);
		
		switch (message.type) {
		case MOVE_UP:
			tmpVec1.set(entity.getPosition());
			tmpVec1.x = Maths.round(entity.getPosition().x, halfCell);
			entity.setPosition(tmpVec1);
			
			entity.setView(tmpVec1.set(Vector2.Y));
			entity.setMovementSpeed(entity.getMaxSpeed());
			return true;
		case MOVE_DOWN:
			tmpVec1.set(entity.getPosition());
			tmpVec1.x = Maths.round(entity.getPosition().x, halfCell);
			entity.setPosition(tmpVec1);
			
			entity.setView(tmpVec1.set(Vector2.Y).scl(-1));
			entity.setMovementSpeed(entity.getMaxSpeed());
			return true;
		case MOVE_LEFT:
			tmpVec1.set(entity.getPosition());
			tmpVec1.y = Maths.round(entity.getPosition().y, halfCell);
			entity.setPosition(tmpVec1);
			
			entity.setView(tmpVec1.set(Vector2.X).scl(-1));
			entity.setMovementSpeed(entity.getMaxSpeed());
			return true;
		case MOVE_RIGHT:
			tmpVec1.set(entity.getPosition());
			tmpVec1.y = Maths.round(entity.getPosition().y, halfCell);
			entity.setPosition(tmpVec1);
			
			entity.setView(tmpVec1.set(Vector2.X));
			entity.setMovementSpeed(entity.getMaxSpeed());
			return true;
		case STOP:
			entity.setMovementSpeed(0);
			return true;
		case FIRE:
			entity.fire();
			return true;
		default:
			break;
		}
		return false;
	}

	@Override
	public String getStateName() { return "Player Controlled"; }
	
	public static BaseState<Tank> getInstance() { return instance; }

}

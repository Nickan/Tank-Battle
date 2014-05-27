package com.nickan.tankbattle.model;

import com.badlogic.gdx.math.Vector2;
import com.nickan.framework.finitestatemachine.messagingsystem.Message;

/**
 * Bullet for tank
 * @author Nickan
 *
 */
public class Bullet extends MoveableEntity {
	public static final float MAX_SPEED = 5.0f;
	boolean fired;
	public int upgrade;

	public Bullet(Vector2 position, Vector2 size, Vector2 view,
			float movementSpeed) {
		super(position, size, view, movementSpeed);
		
		fired = false;
		upgrade = 1;
	}
	
	
	public void setFired(boolean fired) {
		this.fired = fired;
	}
	
	public boolean isFired() {
		return fired;
	}


	@Override
	public boolean handleMessage(Message message) {
		// TODO Auto-generated method stub
		return false;
	}

}

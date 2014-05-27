package com.nickan.tankbattle.model;

import com.badlogic.gdx.math.Vector2;

/**
 * Abstract for Moving entity
 * @author Nickan
 *
 */
public abstract class MoveableEntity extends Entity {
	float movementSpeed;
	float maxSpeed;
	
	public MoveableEntity(Vector2 position, Vector2 size, Vector2 view, float maxSpeed) {
		super(position, size, view);
		this.maxSpeed = maxSpeed;
	}

	public float getMovementSpeed() { return movementSpeed; }
	public void setMovementSpeed(float movementSpeed) { this.movementSpeed = movementSpeed; }

	public float getMaxSpeed() { return maxSpeed; }
	public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
	
}

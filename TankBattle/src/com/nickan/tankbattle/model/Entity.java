package com.nickan.tankbattle.model;

import com.badlogic.gdx.math.Vector2;
import com.nickan.framework.finitestatemachine.BaseEntity;

/**
 * Basic entity class
 * @author Nickan
 *
 */
public abstract class Entity extends BaseEntity {
	Vector2 position;
	Vector2 size;
	Vector2 view;
	
	public Entity(Vector2 position, Vector2 size, Vector2 view) {
		this.position = position;
		this.size = size;
		this.view = view;
	}
	
	public Vector2 getPosition() { return position; }
	public void setPosition(Vector2 position) { this.position.set(position); }
	public void setPosition(float x, float y) { 
		position.x = x;
		position.y = y;
	}
	
	public Vector2 getSize() { return size; }
	public void setSize(Vector2 size) { this.size.set(size); }

	public Vector2 getView() { return view; }
	public void setView(Vector2 view) { this.view.set(view); }
	public void setView(float x, float y) {
		view.x = x;
		view.y = y;
	}
	
}

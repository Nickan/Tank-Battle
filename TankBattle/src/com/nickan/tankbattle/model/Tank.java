package com.nickan.tankbattle.model;

import com.badlogic.gdx.math.Vector2;
import com.nickan.framework.Maths;
import com.nickan.framework.finitestatemachine.BaseState;
import com.nickan.framework.finitestatemachine.StateMachine;
import com.nickan.framework.finitestatemachine.messagingsystem.Message;
import com.nickan.tankbattle.model.tankstate.TankAttackState;
import com.nickan.tankbattle.model.tankstate.TankClientState;
import com.nickan.tankbattle.model.tankstate.TankGlobalState;

/**
 * A generic Tank class
 * @author Nickan
 *
 */
public class Tank extends MoveableEntity {
	int damage;
	float atckSpd;
	float currentHp;
	float maxHp;
	
	float animationTime;
	float animationDuration;
	
	public enum TankType { SERVER, CLIENT, ENEMY };
	TankType tankType;
	
	// Max bullet number
	Bullet[] bullets = new Bullet[2];
	StateMachine<Tank> stateMachine;
	
	/** Only used to send packets when only needed for movement to utilize sending packets */
	public boolean setMoving = false;

	public Tank(Vector2 position, Vector2 size, Vector2 view, float maxSpeed, TankType tankType) {
		super(position, size, view, maxSpeed);
		this.tankType = tankType;
		
		for (int index = 0; index < bullets.length; ++index) {
			bullets[index] = new Bullet(new Vector2(0, 0), new Vector2(0.125f, 0.125f), 
					new Vector2(Vector2.Y), 0);
			
			// Test....
			bullets[index].upgrade = 0;
		}
		stateMachine = new StateMachine<Tank>(this, TankAttackState.getInstance(), TankGlobalState.getInstance());
	}
	
	/**
	 * Will be set as a copy from a instance of another, will never update its state
	 */
	public void setClientState() {
		stateMachine.changeState(TankClientState.getInstance());
	}
	
	public void update(float delta) {
		stateMachine.update(delta);
	}

	@Override
	public boolean handleMessage(Message message) { return stateMachine.handleMessage(message); }

	public int getDamage() { return damage; }
	public void setDamage(int damage) { this.damage = damage; }

	public float getAtckSpd() { return atckSpd; }
	public void setAtckSpd(float atckSpd) { this.atckSpd = atckSpd; }

	public float getCurrentHp() { return currentHp; }
	public void setCurrentHp(float currentHp) { this.currentHp = currentHp; }

	public float getMaxHp() { return maxHp; }
	public void setMaxHp(float maxHp) { this.maxHp = maxHp; }

	public Bullet[] getBullets() { return bullets; }
	
	public void changeState(BaseState<Tank> state) { stateMachine.changeState(state); }
	
	public String getCurrentStateName() { return stateMachine.getCurrentStateName(); }

	public float getAnimationTime() { return animationTime; }
	public void setAnimationTime(float animationTime) { this.animationTime = animationTime; }

	public float getAnimationDuration() { return animationDuration; }
	public void setAnimationDuration(float animationDuration) { this.animationDuration = animationDuration; }
	
	
	public final TankType getTankType() { return tankType; }
	
	
	public void fire() {
		for (Bullet bullet: bullets) {
			if (!bullet.isFired()) {
				setStartingOnTurret(bullet);
				break;
			}
		}
	}
	
	/**
	 * Will set up the bullet correctly
	 * @param bullet
	 */
	private void setStartingOnTurret(Bullet bullet) {
		Vector2 newPos = new Vector2(position);
		
		// Viewing up
		if (view.y == 1) {
			newPos.x = newPos.x + size.x / 2;
			newPos.y = newPos.y + size.y;
		} else if (view.y == -1) {
			// Viewing down
			newPos.x = newPos.x + size.x / 2;
			newPos.y = newPos.y;
		} else if (view.x == 1) {
			// Viewing right
			newPos.x = newPos.x + size.x;
			newPos.y = newPos.y + size.y / 2;
		} else {
			// Viewing left
			newPos.x = newPos.x;
			newPos.y = newPos.y + size.y / 2;
		}
		
		
		//...
		System.out.println("Tank: New pos: " + newPos);
		
		bullet.setPosition(newPos);
		bullet.fired = true;
		bullet.setView(view);
		bullet.setMovementSpeed(Bullet.MAX_SPEED);
	}
	
	
	// For replicating
	public void setId(int id) {
		this.id = id;
	}
	
}

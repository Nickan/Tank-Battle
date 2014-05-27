package com.nickan.framework.finitestatemachine;


/**
 * Manages the entities, as copying just the address of the entity will lead to some error (Well in C++ yes and 
 * it is the standard of the game development, I follow for now then later try to test to see if that is also
 * applicable in Java)
 * @author Nickan
 *
 */
public class EntityManager {
	// Limited quantity for now, to be changed later if needed
	private static final int QUANTITY = 300;
	private static BaseEntity[] entityList = new BaseEntity[QUANTITY];

	private EntityManager() {
		clear();
	}

	public static int getAssignedId(BaseEntity entity) {
		for (int i = 0; i < entityList.length; ++i) {
			if (entityList[i] == null) {
				entityList[i] = entity;
				return i;
			}
		}
		return -1;
	}

	public static boolean deleteEntity(int id) {
		if (id < 0 || id > QUANTITY) {
			System.out.println("ID is invalid");
			return false;
		}
		
		if (entityList[id] != null) {
			entityList[id] = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Get the entity by its id, will return null if the entity has been deleted or doesn't exists
	 * @param id
	 * @return
	 */
	public static BaseEntity getEntity(int id) {
		if (id < 0 || id > QUANTITY) {
			return null;
		}
		return entityList[id];
	}

	public static void clear() {
		for (int i = 0; i < entityList.length; ++i)
			entityList[i] = null;
	}

}

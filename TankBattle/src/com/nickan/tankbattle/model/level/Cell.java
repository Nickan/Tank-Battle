package com.nickan.tankbattle.model.level;

/**
 * Just a container for a cell value, can't have a floating type cell address...
 * @author Nickan
 *
 */
public class Cell {
	int x;
	int y;

	/* To determine which quadrant of cell the bullet is occupying. Bullet can only occupy maximum
	 * of two(2) quadrants
	 */
	private static final int MAX_QUADRANTS_BULLET_CAN_OCCUPY = 2;
	int[] quadrants = new int[MAX_QUADRANTS_BULLET_CAN_OCCUPY];
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		clearOccupiedQuadrants();
	}
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y) {
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public void addQuadrantOccupied(int quadrant) {
		for (int index = 0; index < MAX_QUADRANTS_BULLET_CAN_OCCUPY; ++index) {
			if (quadrants[index] == -1) { // The quadrant occupied is not set yet.
				quadrants[index] = quadrant;
				break;
			}
		}
	}
	
	public void clearOccupiedQuadrants() {
		for (int index = 0; index < MAX_QUADRANTS_BULLET_CAN_OCCUPY; ++index) {
			quadrants[index] = -1;
		}
	}
	
	public String toString() {
		return x + ": " + y + " Quadrant: " + quadrants[0] + ": " + quadrants[1];
	}

	
}

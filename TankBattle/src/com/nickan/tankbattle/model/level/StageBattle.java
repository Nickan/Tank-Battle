package com.nickan.tankbattle.model.level;

import java.util.Scanner;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.math.Vector2;
import com.nickan.framework.util.File;

/**
 * Contains tiles to be rendered on screen
 * @author Nickan
 *
 */
public class StageBattle {
	// Stage has 17 x 15 Cells and every cell has 4 mini cells
	
	private static final int COLUMNS = 52;
	private static final int ROWS = 52;
	private int[][] stage = new int[ROWS][COLUMNS];

	public enum BlockType { WOOD, METAL, FOREST, WATER, NONE } // Soon skidder
	
	public static final int MAX_CELL_ROW = 13;
	public static final int MAX_CELL_COL = 13;
	
 	public StageBattle() {
		clear();
		
		loadLevel(1);
	}
 	
 	/**
 	 * Use this to prevent error when using bulletHit()
 	 * @param bulletPos
 	 * @param direction
 	 * @param bulletUpgrade
 	 * @return
 	 */
 	public boolean bulletValid(Vector2 bulletPos, Vector2 direction, int bulletUpgrade) {
 		return !(bulletPos.x < 0 || bulletPos.y < 0 || bulletPos.x > MAX_CELL_COL || bulletPos.y > MAX_CELL_ROW);
 	}
 	
	/**
	 * It will update the status of the wood or metal on stage depending on the values passed in
	 * @param bulletPos
	 * @param direction
	 * @param bulletUpgrade
	 * @return - If the bullet hit or the bullet value passed are invalid
	 */
	public boolean bulletHit(Vector2 bulletPos, Vector2 direction, int bulletUpgrade) {
		int positionDivision = 4;
		int stageX = (int) (bulletPos.x / (1.0f / positionDivision) );
		int stageY = (int) (bulletPos.y / (1.0f / positionDivision) );
		
		// Moving vertically
		if (direction.y != 0) {
			// The bullet will always occupy left and right mini cells based on world coordinates
			BlockType rightBlock = getBlockType(stage[stageY][stageX]);
			BlockType leftBlock = getBlockType(stage[stageY][stageX - 1]); // The left side mini cell

			return verticalBulletHit(stageX, stageY, rightBlock, leftBlock, direction, bulletUpgrade);
			
		} else {
			// Moving horizontally
			BlockType topBlock = getBlockType(stage[stageY][stageX]);
			BlockType bottomBlock = getBlockType(stage[stageY - 1][stageX]); // The bottom side mini cell
			
			return horizontalBulletHit(stageX, stageY, topBlock, bottomBlock, direction, bulletUpgrade);
		}
		
	}
	
	private boolean verticalBulletHit(int stageX, int stageY, BlockType rightBlock, 
			BlockType leftBlock, Vector2 direction, int bulletUpgrade) {
		
		boolean bulletHit = false;
		switch (rightBlock) {
		case WOOD:
			stage[stageY][stageX] = 0; // Direct hit, destroy
			
			/* 
			 * Bullet upgrade two (3) should destroy two (2) wood at a time, but it should only destroy the
			 * wood that is belong in the same quadrant being hit, not the other quadrant
			 */
			if (bulletUpgrade == 3) {
				int stageModY = stageY % 2;
				int upperCellModY = stageY % 2;
				if (upperCellModY == stageModY) { // They are in the same quadrant
					/* 
					 * Above or below the hit block, depending on the direction
					 * bullet is coming from
					 */
					stage[(int) (stageY + direction.y)][stageX] = 0;
					stage[(int) (stageY + direction.y)][stageX + 1] = 0;
					
					stage[stageY][stageX + 1] = 0;
				}
			} else {
				
				if (leftBlock != BlockType.METAL) {
					if (getBlockType(stage[stageY][stageX + 1]) == BlockType.WOOD) { // If the neighbor on the right is wood, destroy it too
						stage[stageY][stageX + 1] = 0;
					}
				}
			}
			
			
			
			bulletHit = true;
			break;
		case METAL:
			// Destroy the entire quadrant
			if (bulletUpgrade == 3) {
				stage[stageY][stageX] = 0; // Direct hit, destroy
				stage[stageY][stageX + 1] = 0; // Destroy the right side too
				
				stage[(int) (stageY + direction.y)][stageX] = 0;
				stage[(int) (stageY + direction.y)][stageX + 1] = 0;	
			}
			
			bulletHit = true;
			break;
		default:
			break;
		}

		switch (leftBlock) {
		case WOOD:
			stage[stageY][stageX - 1] = 0;
			
			/* 
			 * Bullet upgrade two (3) should destroy two (2) wood at a time, but it should only destroy the
			 * wood that is belong in the same quadrant being hit, not the other quadrant
			 */
			if (bulletUpgrade == 3) {
				int stageModY = stageY % 2;
				int upperCellModY = stageY % 2;
				if (upperCellModY == stageModY) { // They are in the same quadrant
					
					/*
					 * Destroy the cell up or bottom depending on the bullet's vertical movement
					 */
					stage[(int) (stageY + direction.y)][stageX - 1] = 0;
					stage[(int) (stageY + direction.y)][stageX - 2] = 0;
					
					stage[stageY][stageX - 2] = 0;
				}
			} else {
				
				if (rightBlock != BlockType.METAL) {
					// If the left neighbor is wood, destroy it too
					if (getBlockType(stage[stageY][stageX - 2]) == BlockType.WOOD) {
						stage[stageY][stageX - 2] = 0;
					}
				}
			}
			

			bulletHit = true;
			break;
		case METAL:
			// Destroy the entire quadrant
			if (bulletUpgrade == 3) {
				stage[stageY][stageX - 1] = 0;
				stage[stageY][stageX - 2] = 0;
				
				stage[(int) (stageY + direction.y)][stageX - 1] = 0;
				stage[(int) (stageY + direction.y)][stageX - 2] = 0;
			}
			
			bulletHit = true;
			break;
		default:
			break;
		}

		return bulletHit;
	}
	
	private boolean horizontalBulletHit(int stageX, int stageY, BlockType topBlock, 
			BlockType bottomBlock, Vector2 direction, int bulletUpgrade) {
		boolean bulletHit = false;
		
		switch (topBlock) {
		case WOOD:
			stage[stageY][stageX] = 0;
			
			/* 
			 * Bullet upgrade two (3) should destroy two (2) wood at a time, but it should only destroy the
			 * wood that is belong in the same quadrant being hit, not the other quadrant
			 */
			if (bulletUpgrade == 3) {
				int stageModX = stageX % 2;
				int upperCellModX = stageX % 2;
				if (upperCellModX == stageModX) { // They are in the same quadrant
					/*
					 * Left and right of the hit block depending on where the bullet's horizontal movement
					 */
					stage[stageY][(int) (stageX + direction.x)] = 0;
					
					stage[stageY + 1][(int) (stageX + direction.x)] = 0;
					stage[stageY + 1][stageX] = 0;
				}
			} else {
				if (bottomBlock != BlockType.METAL) {
					// Destroy if the top block is wood
					if (getBlockType(stage[stageY + 1][stageX]) == BlockType.WOOD) {
						stage[stageY + 1][stageX] = 0;
					}
				}
			}
			
			bulletHit = true;
			break;
		case METAL:
			// Destroy the entire quadrant
			if (bulletUpgrade == 3) {
				stage[stageY][stageX] = 0;
				stage[stageY][(int) (stageX + direction.x)] = 0;
				
				stage[stageY + 1][(int) (stageX + direction.x)] = 0;
				stage[stageY + 1][stageX] = 0;
			}
			
			bulletHit = true;
			break;
			default:
				break;
		}
		
		switch (bottomBlock) {
		case WOOD:
			stage[stageY - 1][stageX] = 0;
			
			/* 
			 * Bullet upgrade two (3) should destroy two (2) wood at a time, but it should only destroy the
			 * wood that is belong in the same quadrant being hit, not the other quadrant
			 */
			if (bulletUpgrade == 3) {
				int stageModX = stageX % 2;
				int upperCellModX = stageX % 2;
				if (upperCellModX == stageModX) { // They are in the same quadrant
					/*
					 * Left and right of the hit block depending on where the bullet's horizontal movement
					 */
					stage[stageY - 1][(int) (stageX + direction.x)] = 0;
					stage[stageY - 2][(int) (stageX + direction.x)] = 0;
					
					stage[stageY - 2][stageX] = 0;
				}
			} else {
				if (topBlock != BlockType.METAL) {
					// Destroy if the bottom block is wood
					if (getBlockType(stage[stageY - 2][stageX]) == BlockType.WOOD) {
						stage[stageY - 2][stageX] = 0;
					}
					
				}
			}
			
			
			
			bulletHit = true;
			break;
		case METAL:
			// Destroy the entire quadrant
			if (bulletUpgrade == 3) {
				stage[stageY - 1][stageX] = 0;
				
				stage[stageY - 1][(int) (stageX + direction.x)] = 0;
				stage[stageY - 2][(int) (stageX + direction.x)] = 0;
				
				stage[stageY - 2][stageX] = 0;
			}
			
			bulletHit = true;
			break;
			default:
				break;
		}
		
		return bulletHit;
	}
	
	
	private BlockType getBlockType(int num) {
		switch (num) {
		case 1: return BlockType.WOOD;
		case 2: return BlockType.METAL;
		case 3: return BlockType.METAL;
		case 4: return BlockType.FOREST;
		case 5: return BlockType.FOREST;
		case 6: return BlockType.WATER;
		case 7: return BlockType.WATER;
			default:
				return BlockType.NONE;
		}
	}
	
	public void loadLevel(int level) {
		Scanner scan = File.newScanner(level + ".txt", FileType.Internal);
		
		int col = 0;
		while (scan.hasNext()) {
			// Ignore the Strings
			scan.next();
			
			while (scan.hasNextInt()) {
				int row = col / stage[0].length;
				stage[row][col % stage[0].length] = scan.nextInt();
				col++;
			}
			
		}
		scan.close();
	}
	
	private void clear() {
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLUMNS; ++col) {
				stage[row][col] = 0;
			}
		}
	}
	
	public final int[][] getAllTilesInfo() {
		return stage;
	}
	
	/*
	private void printStageValue(int x, int y) {
 		System.out.println("Stage at: " + x + ": " + y + " = " + stage[y][x]);
 	}
 	*/
}

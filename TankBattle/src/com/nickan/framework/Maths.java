package com.nickan.framework;

import com.badlogic.gdx.math.MathUtils;

/**
 * In test phase, beware!!!
 * @author Nickan
 *
 */
public class Maths {
	
	/**
	 * Working for 0.5f, I have not tested for other float value, to be tested later
	 * @param num
	 * @param nearest
	 * @return
	 */
	public static float round(float num, float nearest) {
		float num2 = num * (1.0f / nearest);
		int round = MathUtils.round(num2);
		return round * nearest;
	}
}

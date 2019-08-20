package org.lhs.notlaos.machine;

import org.joml.Vector2f;

/**
 * Bed corners, for offsetting
 * @author jediminer543
 *
 */
public enum BedSide {

	/**
	 * 0,0
	 */
	BottomLeft(0,0),
	/**
	 * 0, Max
	 */
	TopLeft(0,1),
	BottomRight(1,0),
	TopRight(1,1);
	
	int x, y;
	
	BedSide(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f getPoint(Vector2f bedSize) {
		return new Vector2f(bedSize.x*x, bedSize.y*y);
	}
}

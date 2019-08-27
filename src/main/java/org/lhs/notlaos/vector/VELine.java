package org.lhs.notlaos.vector;

import java.awt.Color;

import org.joml.Vector2f;

public class VELine implements IVectorElement {

	Vector2f p1, p2; 
	Color c;
	
	public VELine(Vector2f p1, Vector2f p2, Color c) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.c = c;
	}

	public Vector2f getP1() {
		return p1;
	}

	public Vector2f getP2() {
		return p2;
	}

	public Color getC() {
		return c;
	}
	
	
}

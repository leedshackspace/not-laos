package org.lhs.notlaos.gcode.codes;

import java.text.DecimalFormat;

import org.lhs.notlaos.gcode.IGCCommand;

/**
 * A move command
 * 
 * @author jediminer543
 *
 */
public class GCMove implements IGCCommand {
	
	//Decimal format with um precision
	private static DecimalFormat df = new DecimalFormat("#.###");
	
	/**
	 * Is this a G0 or a G1
	 * G0 isn't interpolated
	 */
	boolean interpolate = false;
	
	/**
	 * Move coordinates; null = non-present
	 */
	Double x, y, z;
	
	/**
	 * Feedrate; units/min
	 */
	Double feed;
	
	public GCMove(boolean interp, Double x, Double y) {
		this(interp, x, y, null);
	}
	
	public GCMove(boolean interp, Double f) {
		this(interp, null, null, f);
	}
	
	public GCMove(boolean interp, Double x, Double y, Double f) {
		this.interpolate = interp;
		this.x = x;
		this.y = y;
		this.feed = f;
	}
	
	public Double getX() {
		return x;
	}
	
	public Double getY() {
		return y;
	}
	
	public Double getZ() {
		return z;
	}
	
	public Double getF() {
		return feed;
	}
	
	@Override
	public String toString() {
		return (interpolate ? "G1 " : "G0 ") 
				+ (x == null ? "" : ("X"+df.format(x)+" "))
				+ (y == null ? "" : ("Y"+df.format(y)+" "))
				+ (z == null ? "" : ("Z"+df.format(z)+" "))
				+ (feed == null ? "" : ("F"+df.format(feed)+" "))
				+ "\n";
	}
}

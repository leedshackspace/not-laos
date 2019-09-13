package org.lhs.notlaos.gcode.codes;

import java.text.DecimalFormat;

import org.lhs.notlaos.gcode.IGCCommand;

/**
 * A power change command, 
 * either setting the power, 
 * or turning off the laser.
 * 
 * Equivelent to an M3/4/5 command
 * 
 * @author jediminer543
 *
 */
public class GCPower implements IGCCommand {
	
	//Decimal format with um precision
	private static DecimalFormat df = new DecimalFormat("#.###");
	
	private double pow = 0.0;
	private boolean on = false;
	private boolean inline = true; // Where firmware supported
	
	public GCPower(boolean on, double pow, boolean inline) {
		this.on = on;
		this.pow = pow;
		this.inline = inline;
	}
	
	public GCPower(double pow) {
		this(pow > 0, pow, true);
	}
	
	public double getPower() {
		return on ? pow : 0;
	}
	
	public boolean getInline() {
		return inline;
	}
	
	public boolean getState() {
		return on;
	}
	
	public String getCommandString() {
		return (on ? "M3 S" + df.format(pow) + (inline ?  " I \n" : " \n") : (inline ? "M5 I\n" : "M5\n"));
	}
}

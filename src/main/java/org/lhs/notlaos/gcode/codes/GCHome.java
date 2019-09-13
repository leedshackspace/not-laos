package org.lhs.notlaos.gcode.codes;

import org.lhs.notlaos.gcode.IGCCommand;

/**
 * A Homing command
 * 
 * Used for returning to machine root coordinates
 * 
 * @author jediminer543
 *
 */
public class GCHome implements IGCCommand {

	public boolean X, Y, Z;
	
	public GCHome() { 
		this(true, true, false);
	}
	
	public GCHome(boolean X, boolean Y) { 
		this(X, Y, false);
	}
	
	public GCHome(boolean X, boolean Y, boolean Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}

	@Override
	public String getCommandString() {
		return "G28" + (X?" X":"") + (Y?" Y":"") + (Z?" Z":"") + "\n";
	}
}

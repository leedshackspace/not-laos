package org.lhs.notlaos.gcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;
import org.lhs.notlaos.gcode.codes.GCMove;

public class GCode implements Iterable<IGCCommand> {

	Deque<IGCCommand> mainCommand = new ArrayDeque<IGCCommand>();
	
	public void append(IGCCommand cmd) {
		mainCommand.add(cmd);
	}

	@Override
	public Iterator<IGCCommand> iterator() {
		return mainCommand.iterator();
	}
	
	public List<Vector3f> getPoints(Vector3f origin) {
		ArrayList<Vector3f> out = new ArrayList<>();
		Vector3f cur = new Vector3f(origin); //Always starts at origin
		for (IGCCommand gc : mainCommand) {
			if (gc instanceof GCMove) {
				GCMove gcm = ((GCMove) gc);
				if (gcm.getX() != null) cur.x = gcm.getX().floatValue();
				if (gcm.getY() != null) cur.y = gcm.getY().floatValue();
				if (gcm.getZ() != null) cur.z = gcm.getZ().floatValue();
			}
		}
		return out;
	}
	
	/**
	 * Returns a 4 float array of [xmin, ymin, xmax, ymax] of the current gcode
	 * 
	 * @param origin
	 * @return
	 */
	public float[] getBoundaries(Vector3f origin) {
		List<Vector3f> points = getPoints(origin);
		float[] out = new float[4];
		out[0] = Float.MAX_VALUE;
		out[1] = Float.MAX_VALUE;
		out[2] = -1*Float.MAX_VALUE;
		out[3] = -1*Float.MAX_VALUE;
		for (Vector3f v : points) {
			out[0] = Math.min(v.x, out[0]);
			out[1] = Math.min(v.y, out[1]);
			out[2] = Math.max(v.x, out[2]);
			out[3] = Math.max(v.y, out[3]);
		}
		return out;
	}
}

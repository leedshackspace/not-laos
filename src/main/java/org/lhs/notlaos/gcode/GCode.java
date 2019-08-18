package org.lhs.notlaos.gcode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class GCode implements Iterable<IGCCommand> {

	Deque<IGCCommand> mainCommand = new ArrayDeque<IGCCommand>();
	
	public void append(IGCCommand cmd) {
		mainCommand.add(cmd);
	}

	@Override
	public Iterator<IGCCommand> iterator() {
		return mainCommand.iterator();
	}
}

package org.lhs.notlaos.gcode;

import java.io.IOException;
import java.io.Writer;

public class GCodeWriter extends Writer {

	Writer backing;
	
	public GCodeWriter(Writer backing) {
		this.backing = backing;
	}
	
	public void write(IGCCommand igc) throws IOException {
		write(igc.toString());
	}
	
	public void write(GCode gc) throws IOException {
		for (IGCCommand cmd : gc) {
			write(cmd);
		}
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		backing.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		backing.flush();
	}

	@Override
	public void close() throws IOException {
		backing.close();
	}

}

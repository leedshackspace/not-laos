package org.lhs.notlaos.translation;

import java.io.InputStream;

import org.lhs.notlaos.gcode.GCode;

/**
 * A gcode translator, will read gcode from a given InputStream,
 * and provide back a gcode object
 * 
 * @author jediminer543
 *
 */
public interface ITranslator {

	/**
	 * Translate the given input stream into gcode
	 * 
	 * @param s InputStream to process
	 * @return
	 */
	public GCode translate(InputStream s);
}

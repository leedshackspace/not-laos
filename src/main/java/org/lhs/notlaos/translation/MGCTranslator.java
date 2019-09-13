package org.lhs.notlaos.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lhs.notlaos.gcode.GCode;
import org.lhs.notlaos.gcode.codes.GCHome;
import org.lhs.notlaos.gcode.codes.GCMove;
import org.lhs.notlaos.gcode.codes.GCPower;

/**
 * Loads default marlin gcode
 * 
 * Easiest translator
 * 
 * @author jediminer543
 *
 */
public class MGCTranslator implements ITranslator {

	public static Double getValue(String line, char code) {
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == code) {
				int end = line.indexOf(' ', i);
				String val = line.substring(i+1, end < 0 ? line.length() : end);
				return Double.valueOf(val);
			}
		}
		return null;
	}
	
	@Override
	public GCode translate(InputStream s) {
		GCode out = new GCode();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(s))) {
			br.lines().forEachOrdered(line -> {
				if (line == null || line.equals("")) {
					return;
				}
				switch (line.split(" ", 2)[0]) {
				case "G0":
				case "G1":
					out.append(new GCMove(getValue(line, 'G').equals(0.0), getValue(line, 'X'), getValue(line, 'Y'), getValue(line, 'Z'), getValue(line, 'F')));
					break;
				case "M3":
				case "M5":
					out.append(new GCPower(getValue(line, 'M').equals(3.0), getValue(line, 'M').equals(3.0) ? getValue(line, 'S') : 0, line.contains("I")));
					break;
				case "G28":
					boolean any = line.contains("X") || line.contains("Y") || line.contains("Z");
					out.append(new GCHome(any || line.contains("X"), any || line.contains("Y"), any || line.contains("Z")));
				default:
					break;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return out;
	}

}

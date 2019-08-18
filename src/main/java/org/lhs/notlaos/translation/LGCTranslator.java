package org.lhs.notlaos.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.lhs.notlaos.gcode.GCode;
import org.lhs.notlaos.gcode.codes.GCMove;
import org.lhs.notlaos.gcode.codes.GCPower;

public class LGCTranslator implements ITranslator {

	@Override
	public GCode translate(InputStream stream) {
		GCode out = new GCode();
		String lgc = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			lgc = br.lines().reduce("", (a,b) -> a + b + "\n");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		double lx = 0, ly = 400;
		double power = 0;
		double speed = 0;
		double overspeed = 200*60;
		double mmPerStep = 0.001;
		long[] dwords = null;
		boolean state = false;
		int line = 0;
		for (String s : lgc.split("\n")) {
			line++;
			if (s.startsWith("7 100")) {
				speed = Double.parseDouble(s.split(" ")[2])/100.0*60.0*2.0;
			}
			else if (s.startsWith("7 101")) {
				power = Double.parseDouble(s.split(" ")[2])/100.0;
			}
			else if (s.startsWith("0")) {
				if (state == true) {
					out.append(new GCPower(0));
					out.append(new GCMove(false, overspeed));
				}
				double x = Double.parseDouble(s.split(" ")[1])*mmPerStep;
				double y = Double.parseDouble(s.split(" ")[2])*mmPerStep;
				out.append(new GCMove(false, x, y));
				state = false;
				lx = x; ly = y;
			}
			else if (s.startsWith("1")) {
				if (dwords == null) {
					if (state == false) {
						out.append(new GCPower(power));
						out.append(new GCMove(true, speed));
					}
					double x = Double.parseDouble(s.split(" ")[1])*mmPerStep;
					double y = Double.parseDouble(s.split(" ")[2])*mmPerStep;
					out.append(new GCMove(true, x, y));
					state = true;
					lx = x; ly = y;
				} else {
					//Raster mode
					//out.append(new GCMove(false, speed));// Necesary for seperate G0/G1 Speeds
					out.append(new GCMove(true, speed));
					long sections = dwords.length*32;
					double dx = (Double.parseDouble(s.split(" ")[1])*mmPerStep) - lx;
					double dy = (Double.parseDouble(s.split(" ")[2])*mmPerStep) - ly;
					dx /= (double)sections;
					dy /= (double)sections;
					double accx = 0, accy = 0;
					for (int i = 0; i < sections; i++) {
						boolean tgtstate = ((dwords[i/32] & 1 << (i % 32)) == 1 << (i % 32));
						if (tgtstate != state) {
							if (tgtstate == true) {
								out.append(new GCMove(true, lx+accx, ly+accy));
								out.append(new GCPower(power));
							}
							if (tgtstate == false) {
								out.append(new GCMove(true, lx+accx, ly+accy));
								out.append(new GCPower(0));
							}
							state = tgtstate;
						}
						accx += dx;
						accy += dy;
					}
					dwords = null;
					out.append(new GCPower(0));
					state = false;
				}
			}
			else if (s.startsWith("9")) {
				s = s.replaceAll(" +", " ");
				String[] sections = s.split(" ");
				if (!sections[1].equals("1")) {
					//throw new IllegalArgumentException();
					System.err.println("INVALID");
					System.err.println("Invalid command on line " + line);
					System.err.println(s);
					return null;
				}
				int count = Integer.parseInt(sections[2])/32;
				dwords = new long[count];
				for (int i = 0; i < count; i++) {
					dwords[i] = Long.parseLong(sections[3+i]);
				}
			}
			else {
				if (s.startsWith("201 ") || s.startsWith("202 ") || s.startsWith("203 ") || s.startsWith("204 ")) {
					continue;
				} else {
					System.err.println("INVALID");
					System.err.println("Invalid command on line " + line);
					System.err.println(s);
					return null;
				}
			}
		}
		return out;
	}
}

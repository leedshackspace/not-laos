package org.lhs.notlaos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lhs.notlaos.gcode.GCode;
import org.lhs.notlaos.gcode.GCodeWriter;
import org.lhs.notlaos.translation.LGCTranslator;
import org.lhs.notlaos.translation.server.TFTPServer;
import org.lhs.notlaos.translation.server.TFTPServer.ServerMode;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		File tftpRoot = new File("C:\\Users\\benob\\Documents\\not-laos\\test");
		TFTPServer server = new TFTPServer(tftpRoot, 
				tftpRoot, ServerMode.PUT_ONLY);
		new Thread(server).start();
		List<File> files = new ArrayList<File>(Arrays.asList(tftpRoot.listFiles()));
		while (true) {
			if (!files.containsAll(Arrays.asList(tftpRoot.listFiles()))) {
				List<File> newFiles = new ArrayList<File>(Arrays.asList(tftpRoot.listFiles()));
				List<File> created = new ArrayList<File>();
				newFiles.removeAll(files);
				for (File f : newFiles) {
					System.out.println("Found new file to translate");
					InputStream is = new FileInputStream(f);
					LGCTranslator t = new LGCTranslator();
					GCode gcode = t.translate(is);
					File outFile = new File(f.getAbsolutePath() + ".gc");
					System.out.println("Saving to: " + outFile.getAbsolutePath());
					outFile.createNewFile();
					try (GCodeWriter br = new GCodeWriter(new FileWriter(outFile))) {
						br.write(gcode);
					}
					created.add(outFile);
				}
				files.addAll(newFiles);
				files.addAll(created);
			}
			Thread.sleep(1000);
		}
	}
}

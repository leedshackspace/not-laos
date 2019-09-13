package org.lhs.notlaos.machine;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.fazecast.jSerialComm.SerialPort;

public class Machine {

	String serial;
	int baud;
	SerialPort sp;
	
	int bedWidth, bedHeight;
	
	public Machine(String serial, int baud, int bedWidth, int bedHeight) {
		this.serial = serial;
		this.baud = baud;
		this.bedWidth = bedWidth;
		this.bedHeight = bedHeight;
	}
	
	public void connect() {
		sp = SerialPort.getCommPort(serial);
		sp.openPort();
		sp.setBaudRate(baud);
	}
	
	
	public int getBedWidth() {
		return bedWidth;
	}

	public int getBedHeight() {
		return bedHeight;
	}



	public void sendRaw(String rawCMD) throws IOException {
		//System.out.println(rawCMD);
		try (Writer w = new OutputStreamWriter(sp.getOutputStream())) {
			w.write(rawCMD);
		}
		pollRead();
	}
	
	public void pollRead() {
		byte[] recv = new byte[sp.bytesAvailable()];
		sp.readBytes(recv, recv.length);
		//System.out.println(new String(recv));
		currentLine.append(new String(recv));
		if (currentLine.toString().contains("\n")) {
			lastFullLine = currentLine.toString().split("\n", 1)[0];
			currentLine = new StringBuilder(currentLine.toString().split("\n", 1).length == 2 ? currentLine.toString().split("\n", 1)[1] : "");
			if (!isClearToSend()) {
				System.out.println(lastFullLine);
			}
		}
	}
	
	private String lastFullLine = "";
	private StringBuilder currentLine = new StringBuilder();
	
	public boolean isClearToSend() {
		return lastFullLine.equals("") || lastFullLine.contains("ok");
	}
}

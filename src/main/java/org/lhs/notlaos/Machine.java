package org.lhs.notlaos;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class Machine {

	String serial;
	int baud;
	SerialPort sp;
	
	public Machine(String serial, int baud) {
		this.serial = serial;
		this.baud = baud;
		sp = SerialPort.getCommPort(serial);
		sp.openPort();
		sp.setBaudRate(baud);
		sp.addDataListener(new SerialPortDataListener() {
			
			@Override
			public void serialEvent(SerialPortEvent event) {
				/*
				byte[] recv = new byte[event.getSerialPort().bytesAvailable()];
				event.getSerialPort().readBytes(recv, recv.length);
				System.out.println(new String(recv));
				currentLine.append(new String(recv));
				if (currentLine.toString().contains("\n")) {
					lastFullLine = currentLine.toString().split("\n", 1)[0];
					currentLine = new StringBuilder(currentLine.toString().split("\n", 1).length == 2 ? currentLine.toString().split("\n", 1)[1] : "");
					System.out.println(lastFullLine);
				}
				*/
			}
			
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}
		});
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

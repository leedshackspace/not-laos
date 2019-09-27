package org.lhs.notlaos.machine;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class Machine implements SerialPortDataListener {

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
		sp.addDataListener(this);
	}
	
	public boolean isOpen() { 
		return sp.isOpen();
	}
	
	public void disconnect() {
		sp.closePort();
		sp.removeDataListener();
	}
	
	
	public int getBedWidth() {
		return bedWidth;
	}

	public int getBedHeight() {
		return bedHeight;
	}

	public void send(String rawCMD) throws IOException {
		//TODO
		sp.writeBytes(rawCMD.getBytes(), rawCMD.getBytes().length);
	}
	
	public String getResponse(boolean consume) {
		return consume ? responses.removeFirst() : responses.peekFirst();
	}
	
	Deque<String> responses = new LinkedBlockingDeque<String>();	
	

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if ((event.getEventType() & getListeningEvents()) != getListeningEvents()) {
			return;
		}
		
		int bytesAvaliable = sp.bytesAvailable();
		if (bytesAvaliable <= 0) {
			return;
		}
		
		byte[] tmp = new byte[1024 + bytesAvaliable];
		int count = sp.readBytes(tmp, tmp.length);
		String response = new String(tmp, 0, count);
		
		responses.add(response);
	}
}

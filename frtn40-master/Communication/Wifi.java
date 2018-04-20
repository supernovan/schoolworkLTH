package Communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import lejos.utility.Delay;

public class Wifi extends Thread {
	
	
	
	
	private DataInputStream is;
	private DataOutputStream os;
	private int message = 0;
	private boolean newMessage = false;

	public Wifi(String ip, int port) {
		String address = ip;
		Socket s = null;
		try {
			s = new Socket(address, port);

			is = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			os = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));

		} catch (IOException ex) {
			System.out.println("failure");
			Delay.msDelay(3000);
		}

	}
	
	public void run() {
		try {
			while (true) {
				int temp = is.readInt();
				
				synchronized(this) {
					message = temp;
					newMessage = true;
				}
			}
		} catch (IOException ex) {
			
		}
	}

	public DataOutputStream getOS() {
		return os;
	}
	
	public synchronized int getMessage() {
		int temp = message;
		message = 0;
		newMessage = false;
		return temp;
	}
	
	public synchronized boolean isThereMessage() {
		return newMessage;
	}
	
	
}

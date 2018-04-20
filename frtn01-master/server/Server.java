package server;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Server {
	
	
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			
			double uMin = -10;
			double uMax = 10;
			server = new ServerSocket(53333);
			Socket s = server.accept();
			
			DataInputStream is = new DataInputStream(s.getInputStream());
			DataOutputStream os = new DataOutputStream(s.getOutputStream());
			
			Controler c = new Controler();
			double ang;
			while (!s.isClosed()) {
				
				int command = is.readInt();
				
				switch (command) {
				case Protocol.SEND_BEAM:
					
					ang = is.readDouble();
					double ref_ang = is.readDouble();
					
					double u_ang = c.calculatePI(ang, ref_ang, 0.0);
					os.writeDouble(u_ang);
					c.updateStatesPI(u_ang, 0.0);
					break;
					
				case Protocol.SEND_BALL:
					double pos = is.readDouble();
					double ref_pos = is.readDouble();
					double phiff = is.readDouble();
					
					double ang_ref = c.calculatePID(pos, ref_pos, phiff);
					os.writeDouble(ang_ref);
					ang = is.readDouble();
					double uff = is.readDouble();
					
					double u = c.calculatePI(ang, ang_ref, uff);
					os.writeDouble(u);
					
					//update states
					c.updateStatesPI(u, uff);
					c.updateStatesPID(ang_ref , phiff);
					
					
					break;
				case Protocol.GET_PARAMETERS:
					break;
					
				case Protocol.SET_PARAMETERS:
					break;
				
				default:
					System.out.println("Something is very very wrong");
				}
			}
			
		} catch (IOException ex) {
			System.out.println("Error, socket already busy. Shuting down");
			System.exit(-1);
		}
		

	}
}

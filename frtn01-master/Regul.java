import se.lth.control.DoublePoint;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;
import se.lth.control.realtime.IOChannelException;
import se.lth.control.realtime.Semaphore;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;


public class Regul extends Thread {
	public static final int OFF = 0;
	public static final int BEAM = 1;
	public static final int BALL = 2;

	// added for the project
	public static final int LOCAL = 3;
	public static final int REMOTE = 4;

	// limits
	private double uMin = -10.0;
	private double uMax = 10.0;

	private PI inner = new PI("PI");
	private PID outer = new PID("PID");

	private AnalogIn analogInAngle;
	private AnalogIn analogInPosition;
	private AnalogOut analogOut;

	private ReferenceGenerator referenceGenerator;
	private OpCom opcom;

	private int priority;
	private boolean running = true;
	private long starttime;
	private Semaphore mutex; // used for synchronization at shut-down

	private ModeMonitor modeMon;

	// Inner monitor class
	class ModeMonitor {
		private int mode;

		// Synchronized access methods
		public synchronized void setMode(int newMode) {
			mode = newMode;
			inner.reset();
			outer.reset();
		}

		public synchronized int getMode() {
			return mode;
		}
	}

	public Regul(int pri) {
		priority = pri;
		mutex = new Semaphore(1);
		try {
			analogInAngle = new AnalogIn(0);
			analogInPosition = new AnalogIn(1);
			analogOut = new AnalogOut(0);
		} catch (IOChannelException e) {
			System.out.print("Error: IOChannelException: ");
			System.out.println(e.getMessage());
		}
		modeMon = new ModeMonitor();
	}

	public void setOpCom(OpCom opcom) {
		this.opcom = opcom;
	}

	public void setRefGen(ReferenceGenerator referenceGenerator) {
		this.referenceGenerator = referenceGenerator;
	}

	// Called in every sample in order to send plot data to OpCom
	private void sendDataToOpCom(double yref, double y, double u) {
		double x = (double) (System.currentTimeMillis() - starttime) / 1000.0;
		DoublePoint dp = new DoublePoint(x, u);
		PlotData pd = new PlotData(x, yref, y);
		opcom.putControlDataPoint(dp);
		opcom.putMeasurementDataPoint(pd);
	}

	public synchronized void setInnerParameters(PIParameters p) {
		inner.setParameters(p);
	}

	public synchronized PIParameters getInnerParameters() {
		return inner.getParameters();
	}

	public synchronized void setOuterParameters(PIDParameters p) {
		outer.setParameters(p);
	}

	public synchronized PIDParameters getOuterParameters() {
		return outer.getParameters();
	}

	public void setOFFMode() {
		modeMon.setMode(OFF);
	}

	public void setBEAMMode() {
		modeMon.setMode(BEAM);
	}

	public void setBALLMode() {
		modeMon.setMode(BALL);
	}

	public int getMode() {
		return modeMon.getMode();
	}

	// Called from OpCom when shutting down
	public synchronized void shutDown() {
		running = false;
		mutex.take();
		try {
			analogOut.set(0.0);
		} catch (IOChannelException x) {
		}
	}

	private double limit(double v, double min, double max) {
		if (v < min) {
			v = min;
		} else if (v > max) {
			v = max;
		}
		return v;
	}

	public void run() {
		long duration;
		long t = System.currentTimeMillis();
		starttime = t;
		Socket s;
		DataInputStream is;
		DataOutputStream os;
		try {
			s = new Socket("localhost", 53333); //dummyaddress right now
			is = new DataInputStream(s.getInputStream());
			os = new DataOutputStream(s.getOutputStream());
		} catch (IOException ex) {
			System.out.println("error, couldn't find the server");
			return;
		}
		

		
		int calculationMode = LOCAL;

		setPriority(priority);
		mutex.take();
		while (running) {
			try {

				switch (modeMon.getMode()) {
				case OFF: {
					// Code for the OFF mode.
					// Written by you.
					// Should include resetting the controllers
					// Should include a call to sendDataToOpCom
					
					//maybe shut down socket aswell or something
					
					s.close();
					inner.reset();
					outer.reset();
					sendDataToOpCom(0.0, 0.0, 0.0);
					break;
				}
				case BEAM: {
					// Code for the BEAM mode
					// Written by you.
					// Should include a call to sendDataToOpCom

					double ref_ang = referenceGenerator.getRef();
					double ang = analogInAngle.get();
					double u_ang;
					switch (calculationMode) {
					case LOCAL:
						synchronized (inner) {
							u_ang = limit(inner.calculateOutput(ang, ref_ang), uMin, uMax);
							analogOut.set(u_ang);
							inner.updateState(u_ang);
						}
						sendDataToOpCom(ref_ang, ang, u_ang);
						break;
					case REMOTE:
						os.writeInt(Protocol.SEND_BEAM);
						os.writeDouble(ang);
						os.writeDouble(ref_ang);
						
						// wait until there is something in the input stream (gotta make a function
						// for doing this)
						u_ang = is.readDouble();
						synchronized (inner) {
							inner.updateState(u_ang);
						}
						sendDataToOpCom(ref_ang, ang, u_ang);
						break;
					}
					break;
					
				}
				case BALL: {
					// Code for the BALL mode
					// Written by you.
					// Should include a call to sendDataToOpCom
					double pos = analogInPosition.get();
					double ang = analogInAngle.get();
					double ref_pos = referenceGenerator.getRef();
					double ang_ref;
					double u;
					double phiff = referenceGenerator.getPhiff();
					double uff = referenceGenerator.getUff();
					switch (calculationMode) {
					case LOCAL:
						synchronized (outer) {
							ang_ref = limit(outer.calculateOutput(pos, ref_pos) + phiff, uMin, uMax);

							// Compute the motor voltage with inner controller
							synchronized (inner) {
								u = limit(inner.calculateOutput(ang, ang_ref) + uff, uMin, uMax);
								analogOut.set(u);
								inner.updateState(u - uff);
							}
							outer.updateState(ang_ref - phiff);
						}
						sendDataToOpCom(ref_pos, pos, u);
						break;
					case REMOTE:
						os.writeInt(Protocol.SEND_BALL);
						os.writeDouble(pos);
						os.writeDouble(ref_pos);
						os.writeDouble(phiff);
						
						//accept input
						ang_ref = is.readDouble();
						
						os.writeDouble(ang);  // This data could be sent earlier with the rest
						os.writeDouble(uff);  //so it's worth trying out what is the best
						
						u = is.readDouble();
						analogOut.set(u);
						sendDataToOpCom(ref_pos, pos, u);
						inner.updateState(u - uff);
						outer.updateState(ang_ref- phiff);
						break;
					}
					break;
					
				}
				default: {
					System.out.println("Error: Illegal mode.");
					break;
				}
				}
			} catch (Exception e) {
				System.out.println(e);
				return;
			}
			// sleep
			t = t + inner.getHMillis();
			duration = t - System.currentTimeMillis();
			if (duration > 0) {
				try {
					sleep(duration);
				} catch (InterruptedException x) {
				}
			}
		}
		mutex.give();
	}
}

package server;



public class Controler {
	private double uMin = -10.0;
	private double uMax = 10.0;

	private PI pi = new PI("PI");
	private PID pid = new PID("PID");
	
	
	private double limit(double val) {
		if (val < uMin) {
			val = uMin;
		} else if (val > uMax) {
			val = uMax;
		}
		return val;
	}
	
	public double calculatePI(double ang, double ref_ang, double uff) {
		double u_ang = limit(pi.calculateOutput(ang, ref_ang) + uff);

		return u_ang;
	}
	
	public double calculatePID(double pos, double ref_pos, double phiff) {
		double ang_ref = limit(pid.calculateOutput(pos, ref_pos) + phiff);
		return ang_ref;
	}
	
	public void updatePI(PIParameters piPara) {
		pi.setParameters(piPara);
	}
	
	public void updatePID(PIDParameters pidPara) {
		pid.setParameters(pidPara);
	}
	
	public void updateStatesPI(double u, double uff) {
		pi.updateState(u - uff);
	}
	
	public void updateStatesPID(double ang, double phiff) {
		pid.updateState(ang - phiff);
		
	}
}

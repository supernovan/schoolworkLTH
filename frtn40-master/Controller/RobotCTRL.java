package Controller;

import Communication.Wifi;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.hardware.sensor.HiTechnicGyro;

public class RobotCTRL extends Thread {
	private UnregulatedMotor rightMotor = new UnregulatedMotor(MotorPort.A);
	private UnregulatedMotor leftMotor = new UnregulatedMotor(MotorPort.D);
	private HiTechnicGyro gyroSensor = new HiTechnicGyro(SensorPort.S2);
	private HiTechnicAccelerometer accel = new HiTechnicAccelerometer(SensorPort.S3);
	private double speed = 0; 		// Forward motion speed of robot [-10,10]
	private double direction = 0; // Direction of robot [-50(left),50(right)]

	public void run() {
		double angle = 0; // gyro angle in degrees
		double angleVel = 0; // gyro angle speed in degrees/sec
		double mPos = 0; // Rotation angle of motor in degrees
		double mSpd = 0; // Rotation speed of motor in degrees/sec
		double mSum = 0;
		double currPos = 0;
		double oldPos1 = 0;
		double oldPos2 = 0;
		double oldPos3 = 0;
		double pwr = 0; // motor power in [-100,100]
		int loopCount=0;			// postpone activation of the motors until dt in the loop is stable
		boolean ready=false;
		
		
		
		rightMotor.resetTachoCount();
		leftMotor.resetTachoCount();
//		gyroSensor.reset();
		SampleProvider gyroReader = gyroSensor.getRateMode();
		float[] sampleV = new float[gyroReader.sampleSize()];
		float[] sampleA = new float[accel.sampleSize()];
		
		Button.waitForAnyPress();
		double offset = 0;
		double sampleSize = 10;
		
		for (int i = 0; i<10; i++) {
			Delay.msDelay(2);
			gyroReader.fetchSample(sampleV, 0);
			offset += Math.abs(sampleV[0]);
		}
		offset /= 10.0;
		offset = 0;
		long lastTimeStep = System.nanoTime();
		
		
		boolean wifiConn = false;
		Wifi conn = null;
		if (wifiConn) {
			conn = new Wifi("192.168.1.98", 6666);
			conn.start();
		}
		
		
		
		
		
		double angInt = 0;
		angle = -0.25; 					// Start angle when sitting on support
		Sound.beepSequenceUp();
		Thread.currentThread().setPriority(MAX_PRIORITY);
		
		direction = 0;
		double sinAmp = 1;
		double sinInc = 0.02;
		double sinValue = 0;
		double r = 0;
		int mode = 0; //mode 0 = balance, 1 = forward, 2 = right, 3 = backwards, 4 = left;
		Button.waitForAnyPress();
		rightMotor.resetTachoCount();
		leftMotor.resetTachoCount();
		// feed back loop
		while (!Button.ESCAPE.isDown()) {
			
//			if (conn.isThereMessage()) {
//				direction = 0;
//				mode = conn.getMessage();
////				Sound.buzz();
//			}
			// Get time in seconds since last step
			long now = System.nanoTime();
			double dt = (now - lastTimeStep) / 1000000000.0;	// Time step in seconds
			lastTimeStep = now;
			
			// Get gyro angle and speed
			gyroSensor.fetchSample(sampleV, 0);
			accel.fetchSample(sampleA, 0);
//			offset = ((sampleSize-1)*offset + sampleV[0])/sampleSize;
			angleVel = -(sampleV[0] + 2.1); // invert sign to undo negation in class EV3GyroSensor
//			gAng = gAng + (gSpd * dt); // integrate angle speed to get angle
			angInt = angInt + (angleVel * dt); // integrate angle speed to get angle
			double tempAngle = -Math.toDegrees(Math.atan(sampleA[0]/(Math.hypot(sampleA[1], sampleA[2]))));
			if (ready) {
//				angle = complementryFilter2(tempAngle, angleVel, dt);
//				angle = kalmanFilter(tempAngle, angleVel, dt);
				angle = (angInt*0.95 +  tempAngle*0.05);
			} else {
				angle = angInt;

			}
			
			// Get motor rotation angle and rotational angle speed
			double mSumOld = mSum;
			double rightTacho = rightMotor.getTachoCount();
			double leftTacho = leftMotor.getTachoCount();
			mSum = rightTacho + leftTacho;
			currPos = mSum - mSumOld;
			mPos = mPos + currPos;
			mSpd = ((currPos + oldPos1 + oldPos2 + oldPos3) / 4.0) / dt; // motor rotational speed
			oldPos3 = oldPos2;
			oldPos2 = oldPos1;
			oldPos1 = currPos;
			
			sinValue += sinInc;

			// 0.08, 0.12, 0.8, 15 old value
			//0.0316, 0.0294, 1.1021, 17.6689 lqr values
			pwr = 0.083 * mSpd + (0.08 -r) * mPos + 0.85 * angleVel + 18.1696 * angle;
			
			switch (mode) {
			case 1:
				pwr -= 15;
				break;
			case 2:
				direction = 8;
				break;
			case 3: 
				pwr += 15;
				break;
			case 4:
				direction = -8;
			}
			
//			pwr = 0.027 * mSpd + 0.0795 * mPos + 1.9198 * angleVel + 17.4290 * angle;
			if (pwr > 100) pwr = 100;
			if (pwr < -100) pwr = -100;
			if (ready){
				rightMotor.setPower((int) (pwr - direction));
				leftMotor.setPower((int) (pwr + direction));
			}

			if (!wifiConn) Delay.msDelay(5);
			loopCount++;
			if (loopCount==10) ready=true;	// skip first 10 iterations
			if (loopCount % 1000 == 0) {
				mode += 1;
				direction = 0;
//				Sound.beepSequenceUp();
				mode %= 5;
			}
		}

		rightMotor.close();
		leftMotor.close();
		gyroSensor.close();
	}
	
	
	double qAccel = 0.03;
	double qGyro = 0.0000002;
	double rAngle = 43;
	
	double xBias = 0;
	double P[][] = {{0, 0}, {0, 0}};
	double y, s;
	double k0, k1;
	
	double xAngle = 0;
	
	double kalmanFilter(double accelAngle, double gyroAngle, double dt) {
		
		xAngle  += dt*(gyroAngle - xBias);
		P[0][0] += qAccel*dt - (P[1][0] + P[0][1])*dt;
		P[0][1] += -dt*P[1][1];
		P[1][0] += -dt*P[1][1];
		P[1][1] += qGyro*dt;
		
		y = accelAngle - xAngle;
		s = P[0][0] + rAngle;
		k0 = P[0][0]/s;
		k1 = P[1][0]/s;
		
		xAngle += k0*y;
		xBias += k1*y;
		P[0][0] -= k0*P[0][0];
		P[0][1] -= k0*P[0][1];
		P[1][0] -= k1*P[0][0];
		P[0][0] -= k1*P[0][1];
		
		
		return xAngle;
	}
	
	double y1Temp = 0;
	double xAngle2C = 0;
	double k = 6;
	
	double complementryFilter2(double tempAngle, double angleVel, double dt) {
		
		double x1Temp = (tempAngle -   xAngle2C)*k*k;
		y1Temp = dt*x1Temp+y1Temp;
		double  x2Temp = y1Temp + (tempAngle - xAngle2C)*2*k + (angleVel*dt);
		xAngle2C = dt*x2Temp + xAngle2C;
		
		return xAngle2C;
	}
	

}
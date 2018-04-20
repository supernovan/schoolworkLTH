package Main;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.LED;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.hardware.sensor.HiTechnicGyro;
import lejos.hardware.sensor.HiTechnicAccelerometer;
import lejos.hardware.sensor.HiTechnicCompass;
import lejos.hardware.sensor.HiTechnicAngleSensor;

import java.util.Scanner;
import lejos.utility.Delay;

import Controller.SegWayControl;
import Controller.SegwayControlInt;
import Controller.Dansk;
import Controller.RobotCTRL;
import Controller.ControlLQR;

public class Main {
	private final static int DELAY_SAMPLE = 5;
	private final static int DEFAULT_OFFSET= 42;
	
	
	
	
	public static void main(String[] args) {
		
//		ControlLQR t1 = new ControlLQR();
//		SegWayControl t1 = new SegWayControl();
//		SegwayControlInt t1 = new SegwayControlInt();
		RobotCTRL t1 = new RobotCTRL();
//				Dansk t1 = new Dansk();
		t1.run();
		
//		Scanner scan = new Scanner(System.in);
//		boolean flag = true;
//		while (flag) {
//			
//		t1.interrupt();
//		scan.close();
//		System.exit(0);
		
		
		
		
		
		
		
		
//		LCD.drawString("Program1", 0, 0);
//		Button.waitForAnyPress();
//		LCD.clear();
//		Motor.A.forward();
//		Motor.D.forward();
//		
//		LCD.drawString("FORWARD", 0, 0);
//		Button.waitForAnyPress();
//		Motor.A.backward();
//		Motor.D.backward();
//		LCD.drawString("BACKWARD", 0, 0);
//		Button.waitForAnyPress();
//		Motor.A.stop();
		//System.out.println("testar");
		
//		Delay.msDelay(500);
//		HiTechnicGyro gyroH = new HiTechnicGyro(SensorPort.S2);
//		HiTechnicAccelerometer gyroA = new HiTechnicAccelerometer(SensorPort.S3);
//		//HiTechnicCompass comp = new HiTechnicCompass(SensorPort.S1);
//		HiTechnicAngleSensor comp = new HiTechnicAngleSensor(SensorPort.S1);
//		System.out.println("MEr test");
//		Delay.msDelay(500);
//		System.out.println("press stuff soon");
//		Sound.beepSequenceUp();
//		Sound.buzz();
//		Sound.twoBeeps();
//		Sound.beepSequenceUp();
//
//		
//		float[] sampleV = new float[gyroH.sampleSize()];
//		float[] sampleA = new float[gyroA.sampleSize()];
//		float[] sampleC = new float[comp.sampleSize()];
//		while (Button.ESCAPE.isUp()) {
//			System.out.println(Motor.A.getTachoCount());
//			gyroH.fetchSample(sampleV, 0);
//			gyroA.fetchSample(sampleA, 0);
//			comp.fetchSample(sampleC, 0);
//			//System.out.println("Hastighet: " + sampleV[0] + " Tilt: " + sampleA[0] + "\n Hastighet? " + sampleA[1]);// + " Accel? " + sampleA[2]);
//			//System.out.println(gyro.getAngleMode());
//			//System.out.println("VInkel: " + sampleC[0]);
//			Delay.msDelay(1000);
//            
//		}
	}
}
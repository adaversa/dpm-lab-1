import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController {
	private final int bandCenter, bandwith;
	private final int motorLow, motorHigh;
	private final int motorStraight = 300;
	private final NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.C,
			headMotor = Motor.A;
	private int distance;
	private int currentLeftSpeed;

	public static final int WALLDIST = 20;
	public static final int DEADBAND = 2;

	static UltrasonicSensor mySensor = new UltrasonicSensor(SensorPort.S1);

	public BangBangController(int bandCenter, int bandwith, int motorLow,
			int motorHigh) {
		// Default Constructor

		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		headMotor.stop();
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		 leftMotor.forward();
		 rightMotor.forward();
		currentLeftSpeed = 0;
	}
	
	public void turnRight(int leftSpeed, int rightSpeed){
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
		leftMotor.forward();
		rightMotor.backward();
	}
	public void moveForward(int straightSpeed){
		leftMotor.setSpeed(straightSpeed);
		rightMotor.setSpeed(straightSpeed);
		leftMotor.forward();
		rightMotor.forward();
	}
	public void turnLeft(int leftSpeed, int rightSpeed){
		leftMotor.setSpeed(leftSpeed); // -275
		rightMotor.setSpeed(rightSpeed); // 500
		leftMotor.forward();
		rightMotor.forward();
	}

	@Override
	public void processUSData(int distance) {
		this.distance = distance;
		// TODO: process a movement based on the us distance passed in
		// (BANG-BANG style)

		int error = 0;

		error = distance - WALLDIST;
		//distance = mySensor.getDistance();

		// LCD.drawString("Distance:", 0, 4);
		// LCD.drawInt(error, 4, 11, 4);

		// mySensor.ping();


		if (Math.abs(error) <= DEADBAND) {
			moveForward(motorStraight);
//			leftMotor.setSpeed(motorStraight);
//			rightMotor.setSpeed(motorStraight);
//			leftMotor.forward();
//			rightMotor.forward();
		} else if (error < 0) { // Towards the Right
			turnRight(150,150);
//			leftMotor.setSpeed(150);
//			rightMotor.setSpeed(150);
//			leftMotor.forward();
//			rightMotor.backward();
		} else if (error >= 60) { // Towards the left
			// if (error >= 235){
			// no();
			turnLeft(100,250);
			// leftMotor.setSpeed(100); // -275
			// rightMotor.setSpeed(250); // 500
			// leftMotor.forward();
			// rightMotor.forward();
			// }
			// else{
			// leftMotor.setSpeed(100);
			// rightMotor.setSpeed(100);
			// }
		} else{
			//leftMotor.setSpeed(motorStraight);
			//rightMotor.setSpeed(motorStraight);
		}

	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}

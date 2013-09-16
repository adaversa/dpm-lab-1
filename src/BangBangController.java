/*Written by:
 * Hadi Sayar, Student ID: 260531679 
 * Antonio D'Aversa, Student ID: 260234498
 */
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController {
	private final int bandCenter, bandwith;
	private final int motorLow, motorHigh;
	private final int motorStraight = 250;
	private final NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.C,
			headMotor = Motor.A;
	private int distance;
	private int currentLeftSpeed;

	public static final int WALLDIST = 20;
	public static final int DEADBAND = 3;

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

	// Due to the ultrasonic sensor
	// Sharp right turn.
	public void turnRight(int leftSpeed, int rightSpeed) {
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
		leftMotor.forward();
		rightMotor.backward();
	}

	public void moveForward(int straightSpeed) {
		leftMotor.setSpeed(straightSpeed);
		rightMotor.setSpeed(straightSpeed);
		leftMotor.forward();
		rightMotor.forward();
	}

	public void turnLeft(int leftSpeed, int rightSpeed) {
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

		headMotor.rotateTo(-45);

		// If the error is within the tolerance continue to move straight.
		if (Math.abs(error) <= DEADBAND) {
			moveForward(motorStraight);
		}
		// If the error is negative then we are too close to the wall, adjust
		// such that we move away from the wall.
		else if (error < 0) { 
			// Turn towards the Right
			turnRight(125, 125);
		}
		/* The third and final case. The error is positive and thus we are too
		   far away from the wall.
		   Correct this by moving towards the wall.*/

		else { // Turn towards the left
			turnLeft(150, 300);
		}

	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}

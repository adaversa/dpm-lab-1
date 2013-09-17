import lejos.nxt.*;

public class PController implements UltrasonicController {

	private final int bandCenter, bandwith;
	private final int motorStraight = 250, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.C,
			headMotor = Motor.A;
	private int distance;
	private int currentLeftSpeed;
	private int currentRightSpeed;
	private int filterControl;

	private int WALLDIST = 20;
	private int DEADBAND = 3;
	

	public PController(int bandCenter, int bandwith) {
		// Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);

		currentLeftSpeed = 0;
		filterControl = 0;
	}

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
		// (P-controller style)
		currentLeftSpeed = leftMotor.getSpeed();
		currentRightSpeed = rightMotor.getSpeed();
		int error = 0;
		

		error = distance - bandCenter;
		
		int delta = Math.abs((error * 100)/ 235);
		

		// to adjust the sensor
		//headMotor.rotateTo(-45);

		// If the error is within the tolerance continue to move straight.
		if (Math.abs(error) <= bandwith) {
			moveForward(motorStraight);
		}
		// If the error is negative then we are too close to the wall, adjust
		// such that we move away from the wall.
		else if (error < 0  ) {
			// Turn towards the Right
			leftMotor.setSpeed(motorStraight + (int) (delta * 1.4));
			
			rightMotor.setSpeed(motorStraight - (int) (delta * 1.4));

			leftMotor.forward();
			rightMotor.backward();
			LCD.drawString("I am here!", 0, 6);

			// turnRight(125, 125);
		}
		
		else if (distance < 85 && distance > 30){
			leftMotor.setSpeed(motorStraight - delta/15);
			delta = (int) (delta * 12);
			rightMotor.setSpeed(motorStraight + (int) (delta* 3));

			leftMotor.forward();
			rightMotor.forward();
			
		}
		
		/*
		 * The third and final case. The error is positive and thus we are too
		 * far away from the wall. Correct this by moving towards the wall.
		 */

		else { // Turn towards the left
		
			leftMotor.setSpeed(motorStraight - (int) (delta * 0.4));
			
			rightMotor.setSpeed(motorStraight + (int) (delta * 2.5));
			//2.3
			leftMotor.forward();
			rightMotor.forward();

			// turnLeft(150, 300);
		}
	}

	// Getter methods to get the current speeds of the wheels

	@Override
	public int readUSDistance() {
		return this.distance;
	}

}

import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwith;
	private final int motorStraight = 200, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.C, headMotor = Motor.A;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	
	private int WALLDIST;
	private int DEADBAND;
	
	public PController(int bandCenter, int bandwith) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		
		currentLeftSpeed = 0;
		filterControl = 0;
	}
	
	
	private void no()
	{
		for (int i = 0; i < 5; i++){
			headMotor.rotate(60);
			headMotor.rotate(-60);
		}
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
		// TODO: process a movement based on the us distance passed in (P style)
		
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}

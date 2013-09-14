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

	public static final int WALLDIST = 23;
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
			leftMotor.forward();
			rightMotor.forward();
		} else if (error < 0) { // Towards the Right
			leftMotor.setSpeed(200);
			rightMotor.backward();
		} else if (error >= 60) { // Towards the left
			// if (error >= 235){
			// no();
			leftMotor.setSpeed(-215); // -275
			rightMotor.setSpeed(400); // 500
			// }
			// else{
			// leftMotor.setSpeed(100);
			// rightMotor.setSpeed(100);
			// }
		} else{
			leftMotor.setSpeed(motorStraight);
			rightMotor.setSpeed(motorStraight);
		}

	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}

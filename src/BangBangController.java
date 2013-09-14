import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController {
	private final int bandCenter, bandwith;
	private final int motorLow, motorHigh;
	private final int motorStraight = 200;
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
		// leftMotor.forward();
		// rightMotor.forward();
		currentLeftSpeed = 0;
	}

	private void no() {
		for (int i = 0; i < 5; i++) {
			headMotor.rotate(60);
			headMotor.rotate(-60);
		}
	}

	@Override
	public void processUSData(int distance) {
		this.distance = distance;
		// TODO: process a movement based on the us distance passed in
		// (BANG-BANG style)
		boolean traveling = true;
		int status = 0;
		int error = 0;

		LCD.clear(); // Sets up the screan for distance readings.
		LCD.drawString("Bang-Bang", 0, 0, false);
		LCD.drawString("Distance:", 0, 32, false);

		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();

		while (traveling) {
			status = Button.readButtons();
			if (status == 8) {
				// this.exit(0);
			}
			mySensor.ping();
			distance = mySensor.getDistance();
			error = distance - bandCenter;
			LCD.drawInt(error, 4, 11, 4);
			

			if (Math.abs(error) <= bandwith) {
				leftMotor.forward();
				rightMotor.forward();
			} else if (error < 0) { // Towards the Right
				leftMotor.setSpeed(motorHigh);
				rightMotor.setSpeed(-motorLow);
			} else /*if (error >= 200)*/{ // Towards the left
				//if (error >= 235){
				//no();
					leftMotor.setSpeed(-215); //-275
					rightMotor.setSpeed(500); //500
				//}
				//else{
					//leftMotor.setSpeed(100);
					//rightMotor.setSpeed(100);
				//}
			}
		}
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}

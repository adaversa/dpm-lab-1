import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwith;
	private final int motorStraight = 200, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.B, rightMotor = Motor.C, headMotor = Motor.A;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	
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
	
	
	@Override
	public void processUSData(int distance) {
		
		// rudimentary filter
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			no();
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		// TODO: process a movement based on the us distance passed in (P style)
		
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}

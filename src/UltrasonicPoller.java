import lejos.nxt.UltrasonicSensor;

public class UltrasonicPoller extends Thread {
	private UltrasonicSensor us;
	private UltrasonicController cont;

	public UltrasonicPoller(UltrasonicSensor us, UltrasonicController cont) {
		this.us = us;
		this.cont = cont;
	}
	int distance = 0;
	int filter = 0;
	int filterBound = 33;
	public void run() {
		
		while (true) {
			// process collected data
			distance = us.getDistance();
			if (distance >= 235 && filter < filterBound) {
				filter++;
			}
			if (filter == filterBound) {
				cont.processUSData(distance);
				filter = 0;
			} else if (distance < 101  && filter < filterBound){
				cont.processUSData(distance);
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}
	}

}

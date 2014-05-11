/**
 * 
 */
package screen;

import util.ExperimentException;

/**
 * @author gbonnema
 * 
 */
public class Experiment {

	private static void pause(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException e) {

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ExperimentException {
		MainScreen screen = new MainScreen();
		pause(10);
		screen.stopScreen();
	}

}

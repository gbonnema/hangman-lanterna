/**
 * 
 */
package screen;

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
	public static void main(String[] args) {
		MainScreen screen = new MainScreen();
		pause(5);
		screen.stopScreen();
	}

}

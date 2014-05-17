/**
 * Copyright 2014 Guus Bonnema, Dieren, The Netherlands.
 * 
 * This file is part of hangman-lanterna.
 * 
 * hangman-lanterna is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * hangman-lanterna is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * hangman-lanterna. If not, see <http://www.gnu.org/licenses/>.
 */
package screen;

import util.ExperimentException;

/**
 * @author gbonnema
 * 
 */
public class Experiment {

	// private static void pause(int secs) {
	// try {
	// Thread.sleep(secs * 1000);
	// } catch (InterruptedException e) {
	// // don't care
	// }
	// }

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ExperimentException {
		MainScreen screen = new MainScreen();
		screen.run();
	}

}

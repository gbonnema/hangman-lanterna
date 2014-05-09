/**
 * 
 */
package todo;

import java.io.IOException;

/**
 * Add an item to the todolist in current directory.
 * @author gbonnema
 *
 */
public final class Tda {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Tda() {
	}
	
	/**
	 * @param args arguments
	 * @throws IOException if the configuration file or the todo
	 * file cannot be read
	 */
	public static void main(final String[] args) throws IOException {
		TodoApp t = new TodoApp();
		String item = "Another item";
		t.addItem(item);
	}

}

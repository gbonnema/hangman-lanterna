/**
 * 
 */
package todo;

import java.io.IOException;

/**
 * @author gbonnema
 *
 */
public final class Todo {
	
	/**
	 * private constructor to prevent instantiation.
	 */
	private Todo() {
	}

	/**
	 * Starts the application.
	 * @param args the arguments.
	 * @throws IOException .
	 */
	public static void main(final String[] args) throws IOException {
		TodoApp t = new TodoApp();
		t.show();
	}

}

/**
 * copyright 2014, Guus Bonnema.
 * User license GPL version 3.
 */
package todo9;

/**
 * 
 * A class to manage a todo items.
 * 
 * @author gbonnema
 *
 */
public class Todo {

	private String configFile;
	
	public Todo() {
		configFile = "~/.todo9/config";
	}
	
	public Todo addItem(String item) {
		// determine the current directory
		
		// read the todo list
		// add an item to the todo list
		// save the todo list
		// how about concurrent access? 
		// --> it is one user only, maybe lock
		return this;
	}
	
}

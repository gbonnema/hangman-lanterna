/**
 * Copyright 2014, Guus Bonnema. User license GPL version 3.
 */
package todo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 
 * A class to manage a todo items.
 * 
 * @author gbonnema
 * 
 */
public class TodoApp {

	private String configFileName;
	// TODO: keep a stream for input for the config file
	// TODO: create a stream for input and output of the todo file

	/**
	 * Constructor.
	 * 
	 * @throws IOException .
	 */
	public TodoApp() throws IOException {
		String home = System.getProperty("user.home");
		configFileName = home + File.separator + ".todo9rc";
		boolean append = true;
		BufferedWriter w =
				new BufferedWriter(new FileWriter(configFileName, append));
		/* Read and process todo options */
		w.close();
	}

	/**
	 * Adds an item to the todo list.
	 * 
	 * @param item
	 *            the item to add
	 * @return self.
	 * @throws Exception for any reason.
	 */
	public TodoApp addItem(final String item) throws Exception {
		// read the todo list
		// add an item to the todo list
		// save the todo list
		// how about concurrent access?
		// --> it is one user only, maybe lock
		return this;
	}
	
	private Path getTodoFileForCurrentDir() {
		// determine the current directory
		try {
			Path path = Paths.get(".todo9", new String[0]);
			path = path.toAbsolutePath();
			return path;
		} catch (Exception e) {
			System.err.println("Something wrong with this directory? error: ");
			System.err.println(e.toString());
		}
		return null;		
	}

	/**
	 * Executes the engine.
	 */
	public void show() {
		// Print the todolist
		Path path = getTodoFileForCurrentDir();
		System.out.println("path = \"" + path + "\"");
		System.out.flush();
		return;
	}
}

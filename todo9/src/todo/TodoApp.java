/**
 * Copyright 2014, Guus Bonnema. User license GPL version 3.
 */
package todo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

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
	public TodoApp addItem(final String item) throws IOException {
		
		// read the todo list
		// add an item to the todo list
		// save the todo list
		// how about concurrent access?
		// --> it is one user only, maybe lock
		return this;
	}
	
	private FileInputStream getTodo() {
		try {
			Path path = Paths.get(".todo9", new String[0]);
			path = path.toAbsolutePath();
			File todoFile = new File(path.toString());
			FileInputStream fin = new FileInputStream(todoFile);
			return fin;
		} catch (FileNotFoundException fnfex) {
			return null;
		} catch (Exception e) {
			System.err.println("Something wrong with this directory? error: ");
			System.err.println(e.toString());
		}
		return null;	
	}
	
	private FileLock getLock(final FileInputStream fin) throws IOException {
		FileLock lock;
		try {
			FileChannel channel = fin.getChannel();
			lock = channel.lock();
		} catch (FileNotFoundException e) {
			return null;		// should never happen
		}
		return lock;
	}
	
	private void releaseLock(final FileLock lock) throws IOException {
		lock.release();
	}

	/**
	 * Executes the engine.
	 * @throws IOException if the file lock is not set or released.
	 */
	public void show() throws IOException {
		FileInputStream fin = getTodo();
		if (fin == null) {
			return;
		}
		BufferedInputStream bin = new BufferedInputStream(fin);
		Scanner in = new Scanner(bin);
		FileLock lock = getLock(fin);
		String line;
		while ((line = in.nextLine()) != null) {
			System.out.println(line);
		}
		releaseLock(lock);
		in.close();
		return;
	}
}

/**
 * jcurses.tests package.
 */
package jcurses.tests;

import java.awt.Choice;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
/**
 * This is a test class. TODO: find out what it does and document.
 *
 */
public class AwtTest extends Frame implements ItemListener {

	/**
	 * Required because the java.awt.Frame is serializable.
	 */
	private static final long serialVersionUID = 1L;
	private final int width = 200;
	private final int height = 200;
	private final int leftTopX = 100;
	private final int leftTopY = 100;
	/**
	 * Constructor for the test.
	 */
	public AwtTest() {
		super("JCURSES AwtTest");
		this.setSize(width, height);
		this.setLocation(leftTopX, leftTopY);
		Choice list = new Choice();
		list.add("item1");
		list.add("item2");
		list.add("item3");
		list.add("item4");
		list.add("item5");
		list.add("item6");
		final int nrOcc = 40;
		for (int i = 0; i < nrOcc; i++) {
			list.add("item7" + i);
		}

		list.addItemListener(this);
		add(list);
		setVisible(true);
	}

	/**
	 * Logging the itemStateChanged in System.err.
	 * 
	 * @param e the {@link java.awt.event.ItemEvent} that triggered a change.
	 */
	public final void itemStateChanged(final ItemEvent e) {
		System.err.println("changed: " + e);
	}

	/**
	 * Main.
	 * @param args the command line arguments.
	 */
	public static void main(final String[] args) {
		AwtTest test = new AwtTest();
		new FileDialog(test).setVisible(true);
	}

}
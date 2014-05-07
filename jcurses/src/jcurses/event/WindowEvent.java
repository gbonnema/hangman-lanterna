/**
 * jcurses.event package.
 */
package jcurses.event;

import jcurses.widgets.Window;

/**
 * Instances of this class are generated, if the status of a window is modified.
 * for example, if an window is closed.
 */
public class WindowEvent extends Event {

	/**
	 * a type of event.
	 */
	public static final int CLOSED = 0;
	/**
	 * a type of event.
	 */
	public static final int CLOSING = 1;
	/**
	 * a type of event.
	 */
	public static final int ACTIVATED = 2;
	/**
	 * a type of event.
	 */
	public static final int DEACTIVATED = 3;
	/**
	 * The type of event for this Event. 
	 */
	private int type = 0;
	/**
	 * The source Window.
	 */
	private Window sourceWindow = null;


	/**
	 * The constructor.
	 * 
	 * @param aSourceWindow
	 *            the affected window
	 * @param aType
	 *            the type of the event, must be equal to one of four following
	 *            constants: <br>
	 *            <code>ACTIVATED</code> - the window has got the focus <br>
	 *            <code>DEACTIVATED</code> - the window has lost the focus <br>
	 *            <code>CLOSE</code> - the window has been closed <br>
	 *            <code>CLOSING</code> - the window has begun the process of
	 *            closing
	 */

	public WindowEvent(final Window aSourceWindow, final int aType) {
		super(null);
		sourceWindow = aSourceWindow;
		type = aType;
	}

	/**
	 * @return the type of the event
	 */
	public final int getType() {
		return type;
	}

	/**
	 * @return the affected window
	 */
	public final Window getSourceWindow() {
		return sourceWindow;
	}
}
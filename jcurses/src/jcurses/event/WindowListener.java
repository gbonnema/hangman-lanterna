/**
 * jcurses.event.
 */
package jcurses.event;

/**
 * The interface has to be implemented to listen on instances of
 * <code>WindowEvent</code>.
 */
public interface WindowListener {
	/**
	 * The method will be called by an widget, generating
	 * <code>WindowEvent</code> instances, if the listener has been registered
	 * by it.
	 * 
	 * @param event
	 *            the event occurred.
	 */
	void windowChanged(WindowEvent event);

}

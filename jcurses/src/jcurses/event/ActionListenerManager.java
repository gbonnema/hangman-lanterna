/**
 * jcurses.event package.
 */
package jcurses.event;

/**
 * This class implements a listener manager to manage
 * <code>jcurses.event.ActionEvent</code> instances and listeners on these. Only
 * possible type of handled events is <code>jcurses.event.ActionEvent</code>, of
 * managed listeners id <code>jcurses.event.ActionListener</code>
 */
public class ActionListenerManager extends ListenerManager {

	/**
	 * Handle an event.
	 * 
	 * @param event
	 *            the event that occurred.
	 * @param listener
	 *            the object that is listening (has registered as
	 *            {@link ActionListener}.
	 */
	protected final void
			doHandleEvent(final Event event, final Object listener) {
		((ActionListener) listener).actionPerformed((ActionEvent) event);
	}

	/**
	 * Verify that a listener is an ActionListener.
	 * This throws a RuntimeException if the listener
	 * is not an ActionListener.
	 * 
	 * @param listener an object that is registering as listener.
	 */
	protected final void verifyListener(final Object listener) {
		if (!(listener instanceof ActionListener)) {
			throw new RuntimeException("illegal listener type");
		}
	}
	/**
	 * verify that the event is indeed an ActionEvent.
	 * Throw a RuntimeException if it is not.
	 * 
	 * @param event the event that caused calling this method.
	 */
	protected final void verifyEvent(final Event event) {
		if (!(event instanceof ActionEvent)) {
			throw new RuntimeException("illegal event type");
		}
	}

}

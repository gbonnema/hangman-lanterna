/**
 * jcurses.event package.
 */
package jcurses.event;

/**
 * This class implements a listener manager to manage
 * <code>jcurses.event.ItemEvent</code> instances and listeners on these. The
 * only possible type of handled events is <code>jcurses.event.ItemEvent</code>
 * of managed listeners id <code>jcurses.event.ItemListener</code>
 */
public class ItemListenerManager extends ListenerManager {

	/**
	 * This method called a registered listener.
	 * 
	 * @param event
	 *            The event that occurred.
	 * @param listener
	 *            The listener to be called.
	 */
	protected final void
			doHandleEvent(final Event event, final Object listener) {
		((ItemListener) listener).stateChanged((ItemEvent) event);
	}

	/**
	 * Verifies the listener.
	 * 
	 * @param listener The listener
	 */
	protected final void verifyListener(final Object listener) {
		if (!(listener instanceof ItemListener)) {
			throw new RuntimeException("illegal listener type");
		}
	}

	/**
	 * Verifies an event.
	 * 
	 * @param event the event that occurred.
	 * 
	 */
	protected final void verifyEvent(final Event event) {
		if (!(event instanceof ItemEvent)) {
			throw new RuntimeException("illegal event type");
		}
	}

}
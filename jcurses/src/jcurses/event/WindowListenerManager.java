/**
 * jcurses.event.
 */
package jcurses.event;

/**
 * This class implements a listener manager to manage
 * <code>jcurses.event.ValueChangedEvent</code> instances and listener on these.
 * Only possible type of handled events is
 * <code>jcurses.event.ValueChangedEvent</code>, of managed listeners id
 * <code>jcurses.event.ValueChangedListener</code>.
 */
public class WindowListenerManager extends ListenerManager {

	/**
	 * Calls one of the listeners for the triggered event.
	 * 
	 * @param event
	 *            the event that was triggered.
	 * @param listener
	 *            one of the listeners that registered.
	 */
	protected final void
			doHandleEvent(final Event event, final Object listener) {
		((WindowListener) listener).windowChanged((WindowEvent) event);
	}

	/**
	 * Verifies the listener is of type WindowListener.
	 * 
	 * @param listener a listener that registered for a window event.
	 */
	protected final void verifyListener(final Object listener) {
		if (!(listener instanceof WindowListener)) {
			throw new RuntimeException("illegal listener type");
		}
	}

	/**
	 * Verifies the event is a WindowEvent.
	 * 
	 * @param event the event that was triggered
	 */
	protected final void verifyEvent(final Event event) {
		if (!(event instanceof WindowEvent)) {
			throw new RuntimeException("illegal event type");
		}
	}
}
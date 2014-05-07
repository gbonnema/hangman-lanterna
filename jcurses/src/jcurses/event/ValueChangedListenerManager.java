package jcurses.event;

/**
 * This class implements a listener manager to manage
 * <code>jcurses.event.ValueChangedEvent</code> instances and listener on these.
 * Only possible type of handled events is
 * <code>jcurses.event.ValueChangedEvent</code>, of managed listeners id
 * <code>jcurses.event.ValueChangedListener</code>.
 */
public class ValueChangedListenerManager extends ListenerManager {

	/**
	 * Handles the event by calling the listener.
	 * 
	 * @param event
	 *            the event triggering this method.
	 * @param listener
	 *            a ValueChangedListener registered for the event.
	 */
	protected final void
			doHandleEvent(final Event event, final Object listener) {
		((ValueChangedListener) listener)
				.valueChanged((ValueChangedEvent) event);
	}

	/**
	 * Verify that the listener is a ValueChangedListener.
	 * 
	 * @param listener
	 *            an ActionListener registered for the event.
	 */
	protected final void verifyListener(final Object listener) {
		if (!(listener instanceof ValueChangedListener)) {
			throw new RuntimeException("illegal listener type");
		}
	}

	/**
	 * Verify the event is a ValueChangedEvent.
	 * 
	 * @param event
	 *            the event triggering this verification.
	 */
	protected final void verifyEvent(final Event event) {
		if (!(event instanceof ValueChangedEvent)) {
			throw new RuntimeException("illegal event type");
		}
	}

}

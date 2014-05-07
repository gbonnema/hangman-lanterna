package jcurses.event;

import java.util.Vector;

/**
 * This class is the basic class for listener manager. Listener manager are used
 * in widgets classes to manage listeners on widgets's events. This can be also
 * implemented without an listener manager, but these make it easier.
 * 
 * @author Alexei Chmelev
 */
public abstract class ListenerManager {

	/**
	 * Vector of listeners.
	 */
	private Vector<Object> listeners = new Vector<>();

	/**
	 * The method adds a listener to the list of managed listeners.
	 * 
	 * @param aListener
	 *            The listener to be added. The listener must be of right type
	 *            for listened events.
	 */
	public final void addListener(final Object aListener) {
		verifyListener(aListener);
		listeners.add(aListener);
	}

	/**
	 * The method removes a listener from the list of managed listeners.
	 * 
	 * @param aListener
	 *            The listener to be removed from the list.
	 */
	public final void removeListener(final Object aListener) {
		listeners.remove(aListener);
	}

	/**
	 * The method handles an occurred event. This method is called in a widget
	 * if an event is occurred and has to be delegated to listeners.
	 * 
	 * @param event
	 *            event to be handled
	 */
	public final void handleEvent(final Event event) {
		verifyEvent(event);
		for (int i = 0; i < listeners.size(); i++) {
			doHandleEvent(event, listeners.elementAt(i));
		}
	}

	/**
	 * The method is called by <code>handleElent</code> for each registered
	 * listener. In order this method has to call an method of the listener
	 * object with handled event as parameter
	 * 
	 * @param event
	 *            event to be handled
	 * @param listener
	 *            listener to handle
	 */
	protected abstract void doHandleEvent(Event event, Object listener);

	/**
	 * The method verifies an listener object to be of the right type for this
	 * listener manager.
	 * 
	 * @param aListener
	 *            listener to be verified
	 */
	protected abstract void verifyListener(Object aListener);

	/**
	 * The method verifies an event object to be of the right type for this
	 * listener manager.
	 * 
	 * @param event
	 *            event to be verified
	 */
	protected abstract void verifyEvent(Event event);

	/**
	 * The method returns the number of listeners registered by this listener
	 * manager.
	 * 
	 * @return number of registered listeners
	 */
	public final int countListeners() {
		return listeners.size();
	}

}
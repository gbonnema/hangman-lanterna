/**
 * jcurses.event package.
 */
package jcurses.event;

import jcurses.widgets.Widget;

/**
 * This is the basic class for all events, that are generated by jcurses
 * widgets.
 * 
 */
public class Event {

	/**
	 * The source of the event (a widget).
	 */
	private Widget source = null;

	/**
	 * The constructor.
	 * 
	 * @param aWidget
	 *            the widgets, that has generated this event
	 */
	public Event(final Widget aWidget) {
		source = aWidget;
	}

	/**
	 * The method returns the widget, that has generated this event.
	 * 
	 * @return the widget, that has generated this event.
	 */
	public final Widget getSource() {
		return source;
	}

}

/**
 * jcurses.util.
 */
package jcurses.util;

import java.util.StringTokenizer;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.widgets.Button;
import jcurses.widgets.DefaultLayoutManager;
import jcurses.widgets.ModalDialog;
import jcurses.widgets.Label;
import jcurses.widgets.WidgetsConstants;

/**
 * This is a class to create and show user defined messages. Such message is a
 * dialog with an user defined title, containing an user defined text and a
 * button to close the window with an user defined label.
 */

public class Message extends ModalDialog implements ActionListener {

	/**
	 * Constant padding inside the screen edges.
	 */
	private static final int DEFAULT_SCREEN_PADDING = 3;
	/**
	 * the default height of the title.
	 */
	private static final int DEFAULT_TITLE_HEIGHT = 5;
	/**
	 * The title.
	 */
	private String title = null;

	/**
	 * A Button.
	 */
	private Button button = null;
	/**
	 * a label.
	 */
	private Label label = null;

	/**
	 * The constructor of the Message object.
	 * 
	 * @param aTitle
	 *            the message's title.
	 * @param aText
	 *            the message's text.
	 * @param aButtonLabel
	 *            the label on the message's button.
	 * 
	 */
	public Message(final String aTitle, final String aText,
			final String aButtonLabel) {
		super(getWidth(aText, aTitle) + DEFAULT_SCREEN_PADDING + 1,
				getHeight(aText) + DEFAULT_TITLE_HEIGHT + 2, true, aTitle);

		DefaultLayoutManager manager =
				(DefaultLayoutManager) getRootPanel().getLayoutManager();

		label = new Label(aText);
		button = new Button(aButtonLabel);
		title = aTitle;

		button.addListener(this);

		manager.addWidget(label, 0, 0, getWidth(aText, title) + 2,
				getHeight(aText) + 2, WidgetsConstants.ALIGNMENT_CENTER,
				WidgetsConstants.ALIGNMENT_CENTER);

		final int defaultTitleHeight = 5;
		manager.addWidget(button, 0, getHeight(aText) + 2,
				getWidth(aText, title) + 2, defaultTitleHeight,
				WidgetsConstants.ALIGNMENT_CENTER,
				WidgetsConstants.ALIGNMENT_CENTER);

	}

	/**
	 * Calculates the necessary width using a label and a title. Both must fit
	 * on the screen.
	 * 
	 * @param aLabel
	 *            the label.
	 * @param aTitle
	 *            the title.
	 * @return the calculated width to fit on the screen.
	 */
	private static int getWidth(final String aLabel, final String aTitle) {
		int maxLineLength = 0;

		if (aLabel != null) {
			StringTokenizer tokenizer = new StringTokenizer(aLabel, "\n");
			while (tokenizer.hasMoreElements()) {
				String token = tokenizer.nextToken();
				if (maxLineLength < token.length()) {
					maxLineLength = token.length();
				}
			}
		}

		if (aTitle != null) {
			maxLineLength = Math.max(maxLineLength, aTitle.length());
		}

		// message must fit on the screen
		return Math.min(maxLineLength, jcurses.system.Toolkit.getScreenWidth()
				- DEFAULT_SCREEN_PADDING);
	}

	/**
	 * Calculates the height necessary to accomodate the number
	 * of lines in the message.
	 * 
	 * @param aLabel The label, possibly multiple lines.
	 * @return the necessary height.
	 */
	private static int getHeight(final String aLabel) {
		int result = 0;
		if (aLabel != null) {
			StringTokenizer tokenizer = new StringTokenizer(aLabel, "\n");
			while (tokenizer.hasMoreElements()) {
				tokenizer.nextElement();
				result++;
			}
		}
		return result;
	}

	/**
	 * Required for implementing <code>jcurses.event.ActionListener</code>.
	 * @param event the event that triggers the action.
	 */
	public final void actionPerformed(final ActionEvent event) {
		close();
	}

}
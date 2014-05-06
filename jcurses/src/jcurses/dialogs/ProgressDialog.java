/**
 * jcurses.dialogs.
 */
package jcurses.dialogs;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.system.Toolkit;
import jcurses.widgets.Button;
import jcurses.widgets.DefaultLayoutManager;
import jcurses.widgets.Label;
import jcurses.widgets.WidgetsConstants;
import jcurses.widgets.Window;

/**
 * A dialog to indicate progress which can be updated from another thread.
 * 
 * @author alewis
 * 
 */
public class ProgressDialog {
	/**
	 * number of rows needed to account for borders and padding.
	 */
	private static final int WIDTH_PADDING = 2;
	/**
	 * number of rows needed to account for borders, padding, and buttons.
	 */
	private static final int HEIGHT_FACTOR = 6;
	/**
	 * number of rows needed to account for borders and padding.
	 */
	private static final int WIDTH_FACTOR = 4;
	/**
	 * Default label for the confirmation button.
	 */
	public static final String DEFAULT_LABEL = "Ok";
	/**
	 * maximum dialog width.
	 */
	private static final int MAX_DIALOG_WIDTH = 40;
	/**
	 * message label.
	 */
	private Label msgLabel = null;
	/**
	 * cancel actionlistener.
	 */
	private ActionListener doCancel = null;
	/**
	 * message.
	 */
	private String message = "Please wait....";
	/**
	 * title.
	 */
	private String title = "Progress...";
	/**
	 * window.
	 */
	private Window window = null;
	/**
	 * current.
	 */
	private int current;
	/**
	 * maximum.
	 */
	private int max;
	/**
	 * minimum.
	 */
	private int min;

	/**
	 * Constructor for the ProgressDialog object providing a title.
	 * 
	 * @param aTitle
	 *            Title of the ProgressDialog
	 */
	public ProgressDialog(final String aTitle) {
		setTitle(aTitle);
	}

	/**
	 * Sets the current attribute (progress value) of the ProgressDialog.
	 * 
	 * @param aCurrent
	 *            The new current (progress) value
	 */
	public final void setCurrent(final int aCurrent) {
		current = aCurrent;
	}

	/**
	 * Gets the current attribute (progress value) of the ProgressDialog.
	 * 
	 * @return The current (progress) value
	 */
	public final int getCurrent() {
		return current;
	}

	/**
	 * Instances the Listener for user input during progress.
	 * 
	 * @param aDoCancel
	 *            The new doCancel value
	 */
	public final void setDoCancel(final ActionListener aDoCancel) {
		doCancel = aDoCancel;
	}

	/**
	 * Sets the max (complete progress) of the ProgressDialog.
	 * 
	 * @param aMax
	 *            The new max value
	 */
	public final void setMax(final int aMax) {
		max = aMax;
	}

	/**
	 * Gets the max (complete progress) of the ProgressDialog.
	 * 
	 * @return The max value
	 */
	public final int getMax() {
		return max;
	}

	/**
	 * Sets the message attribute of the ProgressDialog object.
	 * 
	 * @param aMessage
	 *            The new message value
	 */
	public final void setMessage(final String aMessage) {
		message = aMessage;
		if (msgLabel != null) {
			msgLabel.setText(message);
			window.pack();
			// window.show();
		}
	}

	/**
	 * Gets the message attribute of the ProgressDialog object.
	 * 
	 * @return The message value
	 */
	public final String getMessage() {
		return message;
	}

	/**
	 * Sets the min (zero progress) of the ProgressDialog.
	 * 
	 * @param aMin
	 *            The new min value
	 */
	public final void setMin(final int aMin) {
		min = aMin;
	}

	/**
	 * Gets the min (zero progress)of the ProgressDialog.
	 * 
	 * @return The min value
	 */
	public final int getMin() {
		return min;
	}

	/**
	 * Close the dialog.
	 */
	public final void close() {
		if (window != null) {
			window.close();
		}
	}

	/**
	 * Show the dialog.
	 */
	public final void show() {
		int height = 1;
		int width = Math.min(Toolkit.getScreenWidth() - WIDTH_FACTOR,
				MAX_DIALOG_WIDTH);

		window = new Window(width + WIDTH_FACTOR, height + HEIGHT_FACTOR,
				true, title);
		DefaultLayoutManager layout = new DefaultLayoutManager();
		window.getRootPanel().setLayoutManager(layout);

		if (doCancel != null) {
			Button mCancel = new Button(DEFAULT_LABEL);
			mCancel.addListener(
					new ActionListener() {
						public void actionPerformed(final ActionEvent arg0) {
							doCancel.actionPerformed(arg0);
						}
					});

			int btnX = (width - DEFAULT_LABEL.length() - WIDTH_PADDING) / 2;
			int btnY = height + (HEIGHT_FACTOR / 2);
			layout.addWidget(mCancel, btnX, btnY, DEFAULT_LABEL.length()
					+ WIDTH_FACTOR, 1, WidgetsConstants.ALIGNMENT_CENTER,
					WidgetsConstants.ALIGNMENT_CENTER);
		}

		msgLabel = new Label(message);
		layout.addWidget(msgLabel, 1, 1, width, height,
				WidgetsConstants.ALIGNMENT_CENTER,
				WidgetsConstants.ALIGNMENT_CENTER);

		window.show();
	}

	/**
	 * Gets the title attribute of the ProgressDialog.
	 * 
	 * @return The title value
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * Sets the title attribute of the ProgressDialog.
	 * 
	 * @param aTitle
	 *            The new title value
	 */
	public final void setTitle(final String aTitle) {
		title = aTitle;
	}
}

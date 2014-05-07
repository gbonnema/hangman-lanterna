package jcurses.widgets;

import jcurses.event.WindowEvent;
import jcurses.event.WindowListener;
import jcurses.event.WindowManagerBlockingCondition;

/**
 * This class implements a modal dialog. The difference from a normal window is,
 * that a call of the 'show' - method blocks, until the dialog window is closed.
 */
public class ModalDialog extends Window implements WindowListener,
		WindowManagerBlockingCondition {

	/**
	 * The constructor of the dialog.
	 * 
	 * @param x
	 *            the x coordinate of the dialog window's top left corner
	 * @param y
	 *            the y coordinate of the dialog window's top left corner
	 * @param width
	 *            the width of the dialog window
	 * @param height
	 *            the height of the dialog window
	 * @param title
	 *            dialog's title
	 * @param border
	 *            true, if the dialog window has a border, false otherwise
	 */
	public ModalDialog(final int x, final int y, final int width,
			final int height, final boolean border, final String title) {
		super(x, y, width, height, border, title);
		addListener(this);
	}

	/**
	 * The constructor. The dialog window is centered on the screen.
	 * 
	 * @param width
	 *            the width of the dialog window
	 * @param height
	 *            the height of the dialog window
	 * @param title
	 *            dialog's title
	 * @param border
	 *            true, if the dialog window has a border, false otherwise
	 */
	public ModalDialog(final int width, final int height, final boolean border,
			final String title) {
		super(width, height, border, title);
		addListener(this);
	}

	/**
	 * @return true if the dialog is still open (!isClosed()).
	 */
	public final boolean evaluate() {
		return !isClosed();
	}

	/**
	 * show.
	 */
	public final void showOnly() {
		super.show();
	}

	/**
	 * execute the dialog.
	 */
	public final void execute() {
		if (!isVisible()) {
			showOnly();
		}

		if (WindowManager.isInputThread()) {
			WindowManager.blockInputThread(this);
		} else {
			waitForNotify();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jcurses.widgets.Window#show()
	 */
	/**
	 * @see jcurses.widgets.Window#show().
	 */
	public final void show() {
		showOnly();
		execute();
	}

	/**
	 * @param event the triggering event
	 */
	public final void windowChanged(final WindowEvent event) {
		if (event.getType() == WindowEvent.CLOSING) {
			close();
		} else if (event.getType() == WindowEvent.CLOSED) {
			notifyOfClosing();
		}
	}

	/**
	 * wait for notify. This method is synchronized on this.
	 */
	private synchronized void waitForNotify() {
		try {
			wait();
		} catch (InterruptedException e) {
			/* ignore this, is normal */
			return;
		}
		return;
	}
	/**
	 * notify of closing(). This method is synchronized on this.
	 */
	private synchronized void notifyOfClosing() {
		notify();
	}

}
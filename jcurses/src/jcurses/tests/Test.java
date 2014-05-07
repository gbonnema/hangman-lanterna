/**
 * jcurses.tests package.
 */
package jcurses.tests;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.event.ItemEvent;
import jcurses.event.ItemListener;
import jcurses.event.ValueChangedEvent;
import jcurses.event.ValueChangedListener;
import jcurses.event.WindowEvent;
import jcurses.event.WindowListener;
import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Message;
import jcurses.util.Protocol;
import jcurses.widgets.BorderPanel;
import jcurses.widgets.Button;
import jcurses.widgets.FileDialog;
import jcurses.widgets.GridLayoutManager;
import jcurses.widgets.List;
import jcurses.widgets.PasswordField;
import jcurses.widgets.PopUpMenu;
import jcurses.widgets.TextArea;
import jcurses.widgets.Widget;
import jcurses.widgets.WidgetsConstants;
import jcurses.widgets.Window;

/**
 * This class performs a test.
 * <p>
 * TODO: find out what it does exactly and describe it.
 * <p>
 * 
 */
public class Test extends Window implements ItemListener, ActionListener,
		ValueChangedListener, WindowListener, WidgetsConstants {
	/**
	 * button1.
	 */
	private Button button1 = null;
	/**
	 * button2.
	 */
	private Button button2 = null;
	// private CheckBox _c1 = null;
	// private CheckBox _c2 = null;
	// private Label _l1 = null;
	// private Label _l2 = null;
	/**
	 * A list.
	 */
	private List list = null;
	/**
	 * The password.
	 */
	private PasswordField password = new PasswordField();
	/**
	 * the text area.
	 */
	private TextArea textArea = new TextArea(-1, -1,
			"1111\n2222\n3333\n4444\n\n66666\n77777\n"
					+ "888888\n99999999999999999\n1010100101");

	/**
	 * The constructor for the test.
	 * 
	 * @param width
	 *            the width of the test window.
	 * @param height
	 *            the height of the test window.
	 */
	public Test(final int width, final int height) {
		super(width, height, true, "Test");

		BorderPanel bp = new BorderPanel();

		// _c1 = new CheckBox();

		// _c2 = new CheckBox(true);

		// _l1 = new Label("textfeld");

		// _l2 = new Label("checkbox2");

		button1 = new Button("OK");

		button1.setShortCut('o');

		button1.addListener(this);

		button2 = new Button("Cancel");

		button2.setShortCut('p');

		button2.addListener(this);

		list = new List();

		list.add("item1");

		list.add("item201234567890123456789");

		list.add("item3");

		list.add("item4");

		list.add("item5");

		list.addListener(this);

		list.getSelectedColors().setColorAttribute(CharColor.BOLD);

		GridLayoutManager manager1 = new GridLayoutManager(1, 1);

		getRootPanel().setLayoutManager(manager1);

		manager1.addWidget(bp, 0, 0, 1, 1, ALIGNMENT_CENTER, ALIGNMENT_CENTER);

		final int widthInCells = 2;
		final int heightInCells = 5;
		GridLayoutManager manager =
				new GridLayoutManager(widthInCells, heightInCells);

		bp.setLayoutManager(manager);

		// manager.addWidget(_l1,0,0,1,2,ALIGNMENT_CENTER, ALIGNMENT_CENTER);
		final int x1 = 0;
		final int y1 = 0;
		final int w1 = 1;
		final int h1 = 4;
		/* @formatter:off */
		manager.addWidget(list, x1, y1, w1, h1, ALIGNMENT_TOP, 
				ALIGNMENT_CENTER);
		/* @formatter:on */

		final int x2 = 1;
		final int y2 = 0;
		final int w2 = 1;
		final int h2 = 2;
		manager.addWidget(textArea, x2, y2, w2, h2, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);

		final int x3 = 1;
		final int y3 = 2;
		final int w3 = 1;
		final int h3 = 2;
		manager.addWidget(password, x3, y3, w3, h3, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);

		final int x4 = 0;
		final int y4 = 4;
		final int w4 = 1;
		final int h4 = 1;
		manager.addWidget(button1, x4, y4, w4, h4, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);

		final int x5 = 1;
		final int y5 = 4;
		final int w5 = 1;
		final int h5 = 1;
		manager.addWidget(button2, x5, y5, w5, h5, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);
	}

	/**
	 * The main entry point.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws Exception
	 *             if something went wrong.
	 */
	public static void main(final String[] args) throws Exception {
		// Protocol initialize
		System.setProperty("jcurses.protocol.filename", "jcurses.log");

		Protocol.activateChannel(Protocol.DEBUG);

		Protocol.debug("Programm beginnt");

		/*
		 * FileOutputStream stream = new FileOutputStream("test.txt");
		 * 
		 * OutputStreamWriter writer = new OutputStreamWriter(stream,"Cp850");
		 * 
		 * writer.write("W?hlen");
		 * 
		 * writer.flush();
		 * 
		 * writer.close();
		 */
		Toolkit.beep();

		final int width = 28;
		final int height = 20;
		Window test = new Test(width, height);

		test.addListener((WindowListener) test);

		test.show();

		// Toolkit.clearScreen(new CharColor(CharColor.BLUE, CharColor.BLUE,
		// CharColor.REVERSE));
	}

	/**
	 * Process the event that got triggered in a meaningful way.
	 * 
	 * @param event
	 *            the event that triggered this method.
	 */
	public final void actionPerformed(final ActionEvent event) {
		Widget w = event.getSource();

		if (w == button1) {
			Protocol.debug("point1");

			FileDialog dial = new FileDialog("File w?hlen");

			Protocol.debug("point2");

			dial.show();

			Protocol.debug("point3");

			if (dial.getChosenFile() != null) {
				new Message("Meldung!", dial.getChosenFile().getAbsolutePath()
						+ "", "OK").show();
			}

			Protocol.debug("point4");

			password.setVisible(!password.isVisible());

			pack();

			paint();
		} else {
			new Message("Meldung!", "01234567890\nassssssss\naaaaaaa\naaaaaa",
					"CANCEL").show();

			final int popUpTopLeftX = 53;
			final int popUpTopLeftY = 5;
			PopUpMenu menu =
					new PopUpMenu(popUpTopLeftX, popUpTopLeftY, "test");

			final int nrMenuItems = 100;
			final int sep1 = 35; // the place for just a separator
			final int sep2 = 4; // the place for just a separator
			for (int i = 1; i < nrMenuItems; i++) {
				if ((i == sep1) || (i == sep2)) {
					menu.addSeparator();
				} else {
					menu.add("item" + i);
				}
			}

			menu.show();

			new Message("meldung", menu.getSelectedItem() + ":"
					+ menu.getSelectedIndex(), "OK").show();
		}

		// close();
	}

	/**
	 * Indicate that the state has changed.
	 * 
	 * @param e
	 *            the ItemEvent that triggered the stateChanged to be called.
	 */
	public final void stateChanged(final ItemEvent e) {
		Protocol.debug("-----------------");

		new Message("meldung", e.getItem() + ":" + e.getType(), "OK").show();
	}

	/**
	 * Signal that the value changed.
	 * 
	 * @param e
	 *            the ValueChangedEvent.
	 */
	public final void valueChanged(final ValueChangedEvent e) {
		new Message("Alarm", "Ge?ndert in ", "" + list.getSelectedIndex())
				.show();
	}

	/**
	 * Signal that the window changed.
	 * 
	 * @param event
	 *            that triggered this method.
	 */
	public final void windowChanged(final WindowEvent event) {
		Protocol.debug("window event: " + event.getType());

		if (event.getType() == WindowEvent.CLOSING) {
			event.getSourceWindow().close();
		}
	}
}

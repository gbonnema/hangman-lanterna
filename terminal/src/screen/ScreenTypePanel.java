package screen;

/**
 * 
 */

/**
 * @author gbonnema
 * 
 */
public class ScreenTypePanel extends AbstractPanel {

	private final String _query = "Choose \"[g]ui\" or \"[s]creen\": ";

	private String _message;

	/**
	 * @param mainScreen
	 */
	public ScreenTypePanel(TextDraw mainScreen) {
		super(mainScreen);
		_message = "";
	}

	/**
	 * @param mainScreen
	 * @param title
	 */
	public ScreenTypePanel(TextDraw mainScreen, String title) {
		super(mainScreen, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see screen.AbstractPanel#refresh()
	 */
	@Override
	public void refresh() {
		drawString(getPadding(), getPadding(), _query);
		drawString(getPadding(), getPadding() + 2, _message);
		refreshScreen();
		_message = "";
	}

	public void setMessage(String message) {
		_message = message;
	}

}

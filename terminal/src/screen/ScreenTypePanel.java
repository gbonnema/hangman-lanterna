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

	/**
	 * @param mainScreen
	 */
	public ScreenTypePanel(TextDraw mainScreen) {
		super(mainScreen);
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
	}

}

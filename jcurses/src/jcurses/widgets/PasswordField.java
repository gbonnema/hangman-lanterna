package jcurses.widgets;

/**
 * Title: JCurses Description:
 * 
 * @author Dawie Malan
 * @author Alexei Chmelev
 */
public class PasswordField extends TextField {

	/**
	 * Constructor
	 */
	public PasswordField() {
		super();
	}
	/**
	 * Constructor
	 * @param width width
	 * @param text text
	 */
	public PasswordField(final int width, final String text) {
		super(width, text);
	}

	/**
	 * The constructor
	 * 
	 * @param width
	 *            the preferred width, if -1, there is no preferred size.
	 */
	public PasswordField(final int width) {
		super(width);
	}

	/**
	 * Override method in TextComponent to display '*' instead of actual
	 * character values.
	 * @param line the text to be replaced
	 * @return the resulting string
	 */
	protected String replaceTextLineForPrinting(final String line) {
		int length = line.length();
		StringBuffer buf = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			buf.append('*');
		}
		return buf.toString();
	}
}
/**
 * jcurses.system.
 */
package jcurses.system;

/**
 * The instances of this class represent characters or key codes, that are input
 * by an user. An instance of the class contains either an ASCII character or
 * one of in the class declared constants for function keys and control keys.
 */
public class InputChar {
	/** Down arrow. */
	public static final int KEY_DOWN = Toolkit.getSpecialKeyCode(0402);
	/** Up arrow. */
	public static final int KEY_UP = Toolkit.getSpecialKeyCode(0403);
	/** Left arrow. */
	public static final int KEY_LEFT = Toolkit.getSpecialKeyCode(0404);
	/** Right arrow. */
	public static final int KEY_RIGHT = Toolkit.getSpecialKeyCode(0405);
	/** Home. */
	public static final int KEY_HOME = Toolkit.getSpecialKeyCode(0406);
	/** Backspace ( unreliable ). */
	public static final int KEY_BACKSPACE = Toolkit.getSpecialKeyCode(0407);
	/** Function keys. Space for 64 */
	public static final int KEY_F1 = Toolkit.getSpecialKeyCode(0411);
	/** Function keys. */
	public static final int KEY_F2 = Toolkit.getSpecialKeyCode(0412);
	/** Function keys. */
	public static final int KEY_F3 = Toolkit.getSpecialKeyCode(0413);
	/** Function keys. */
	public static final int KEY_F4 = Toolkit.getSpecialKeyCode(0414);
	/** Function keys. */
	public static final int KEY_F5 = Toolkit.getSpecialKeyCode(0415);
	/** Function keys. */
	public static final int KEY_F6 = Toolkit.getSpecialKeyCode(0416);
	/** Function keys. */
	public static final int KEY_F7 = Toolkit.getSpecialKeyCode(0417);
	/** Function keys. */
	public static final int KEY_F8 = Toolkit.getSpecialKeyCode(0420);
	/** Function keys. */
	public static final int KEY_F9 = Toolkit.getSpecialKeyCode(0421);
	/** Function keys. */
	public static final int KEY_F10 = Toolkit.getSpecialKeyCode(0422);
	/** Function keys. */
	public static final int KEY_F11 = Toolkit.getSpecialKeyCode(0423);
	/** Function keys. */
	public static final int KEY_F12 = Toolkit.getSpecialKeyCode(0424);
	/** Delete character. */
	public static final int KEY_DC = Toolkit.getSpecialKeyCode(0512);
	/** Insert character or enter insert mode. */
	public static final int KEY_IC = Toolkit.getSpecialKeyCode(0513);
	/** next page. */
	public static final int KEY_NPAGE = Toolkit.getSpecialKeyCode(0522);
	/** previous page. */
	public static final int KEY_PPAGE = Toolkit.getSpecialKeyCode(0523);
	/** Print. */
	public static final int KEY_PRINT = Toolkit.getSpecialKeyCode(0532);
	/** End. */
	public static final int KEY_END = Toolkit.getSpecialKeyCode(0550);
	/** Escape character. ( == 0x1b) */
	public static final int KEY_ESC = 27;
	/** Tab character. */
	public static final int KEY_TAB = '\t';

	/**
	 * bytes.
	 */
	private static byte[] bytes = new byte[1];
	/**
	 * string.
	 */
	private String string = null;
	/**
	 * code.
	 */
	private int code = -1;

	/**
	 * The constructor.
	 * 
	 * @param aCode
	 *            the code of input char
	 */
	public InputChar(final int aCode) {
		this.code = aCode;

		final int highmark = 0xff;
		if (this.code <= highmark) {
			string = convertByteToString(this.code);
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param character
	 *            an input ascii character
	 */
	public InputChar(final char character) {
		string = "" + character;
		this.code = string.getBytes()[0];
	}

	/**
	 * The method returns the character, contained in this object.
	 * 
	 * throws RuntimeException
	 *             if the instance doesn't contain a character, but a control
	 *             code
	 *             
	 * @return the character, contained in this object
	 * 
	 */
	public final char getCharacter() {
		if (isSpecialCode()) {
			throw new RuntimeException("this is a special key");
		}

		return string.charAt(0);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return the code ( ASCII or control), contained in this instance
	 */
	public final int getCode() {
		return this.code;
	}

	/**
	 * The return value of this method tells, whether the instance contains a
	 * control code or an ascii character.
	 * 
	 * @return <code>true</code>, if a control code is contained,
	 *         <code>false</code> otherwise.
	 */
	public final boolean isSpecialCode() {
		final int highmark = 0xff;
		return (this.code > highmark);
	}

	/**
	 * Two instances of this class are equal, if they contain same codes.
	 * 
	 * @param obj
	 *            the object to compare
	 * 
	 * @return <code>true</code>, if this instance equal to <code>obj</code>,
	 *         false otherwise
	 */
	public final boolean equals(final Object obj) {
		if (!(obj instanceof InputChar)) {
			return false;
		}

		InputChar character2 = (InputChar) obj;

		return (this.code == character2.getCode());
	}

	/**
	 * The method needed to make it possible to use instances of this class as
	 * keys for <code>java.util.Hashtable</code>.
	 * 
	 * @return the code contained in the instance
	 */
	public final int hashCode() {
		return this.code;
	}

	/**
	 * Printing this object.
	 * 
	 * @return the string that prints this object.
	 */
	public final String toString() {
		return string;
	}

	/**
	 * TODO: to be documented.
	 * 
	 * This method is synchronized on this. TODO: find out why
	 * 
	 * @param code a code.
	 * @return the string.
	 */
	private static synchronized String convertByteToString(final int code) {
		bytes[0] = (byte) code;
		String result = null;
		String encoding = Toolkit.getEncoding();

		if (encoding == null) {
			result = new String(bytes);
		} else {
			try {
				result = new String(bytes, encoding);
			} catch (java.io.UnsupportedEncodingException e) {
				result = new String(bytes);
				Toolkit.setEncoding(null);
			}
		}

		return result;
	}
}
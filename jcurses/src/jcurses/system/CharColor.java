package jcurses.system;

/**
 * Instances of this class are used by painting to set color attributes of
 * painted chars. Both black-white mode and color mode attributes can ( and must
 * be) be declared. For the color mode, colors of the background an the
 * foreground can be declared, for the background mode can be declared, whether
 * painted chars are output normal, reverse or in string font (bold).
 * <p>
 * possible values for colors: <br>
 * <code>BLACK</code><br>
 * <code>BLUE</code><br>
 * <code>CYAN</code><br>
 * <code>GREEN</code><br>
 * <code>NAGENTA</code><br>
 * <code>RED</code><br>
 * <code>YELLOW</code><br>
 * <code>WHITE</code>
 * <p>
 * possible values for black-white mode attributes (these attributes are also
 * available for some color displays): <br>
 * <code>BOLD</code><br>
 * <code>NORMAL</code><br>
 * <code>REVERSE</code>
 * 
 */
public class CharColor {
	// color constants
	/**
	 * The color.
	 */
	public static final short BLACK = 0;
	/**
	 * The color.
	 */
	public static final short RED = 1;
	/**
	 * The color.
	 */
	public static final short GREEN = 2;
	/**
	 * The color.
	 */
	public static final short YELLOW = 3;
	/**
	 * The color.
	 */
	public static final short BLUE = 4;
	/**
	 * The color.
	 */
	public static final short MAGENTA = 5;
	/**
	 * The color.
	 */
	public static final short CYAN = 6;
	/**
	 * The color.
	 */
	public static final short WHITE = 7;

	/**
	 * The black-white mode constant.
	 */
	public static final short NORMAL = 0;
	/**
	 * The black-white mode constant.
	 */
	public static final short REVERSE = 1;
	/**
	 * The black-white mode constant.
	 */
	public static final short BOLD = 2;

	/**
	 * background color.
	 */
	private short bgColor;
	/**
	 * black & white attribute.
	 */
	private short blackWhiteAttribute = 0;
	/**
	 * color attribute.
	 */
	private short colorAttribute = 0;
	/**
	 * foreground color.
	 */
	private short foreground;

	/**
	 * The constructor.
	 * 
	 * @param bgColorInit
	 *            background color
	 * @param fgColorInit
	 *            foreground color
	 * @param blackWhiteAttributeInit
	 *            mode attribute
	 * @param colorAttributeInit
	 *            mode attribute
	 */
	public CharColor(final short bgColorInit, final short fgColorInit,
			final short blackWhiteAttributeInit,
			final short colorAttributeInit) {
		verifyColor(bgColorInit);
		verifyColor(fgColorInit);
		verifyAttribute(colorAttributeInit);
		verifyAttribute(blackWhiteAttributeInit);
		setBackground(bgColorInit);
		setForeground(fgColorInit);
		setBlackWhiteAttribute(blackWhiteAttributeInit);
		setColorAttribute(colorAttributeInit);
		// initChtype();
	}

	/**
	 * The constructor.
	 * 
	 * @param bgColorInit
	 *            background color
	 * @param fgColorInit
	 *            foreground color
	 * @param blackWhiteAttributeInit
	 *            mode attribute color mode attribute will be set to
	 *            <code>NORMAL</code>
	 */
	public CharColor(final short bgColorInit, final short fgColorInit,
			final short blackWhiteAttributeInit) {
		this(bgColorInit, fgColorInit, blackWhiteAttributeInit, NORMAL);
	}

	/**
	 * The constructor, sets both the black-white mode attribute and the color
	 * mode attribute to <code>NORMAL</code>.
	 * 
	 * @param bgColorInit
	 *            background color
	 * @param fgColorInit
	 *            foreground color
	 */
	public CharColor(final short bgColorInit, final short fgColorInit) {
		this(bgColorInit, fgColorInit, NORMAL);
	}

	/**
	 * The method sets the background color.
	 * 
	 * @param bgColorInit
	 *            value to be set
	 */
	public final void setBackground(final short bgColorInit) {
		verifyColor(bgColorInit);
		bgColor = bgColorInit;
		// initChtype();
	}

	/**
	 * Accessor pattern.
	 * 
	 * @return the background color
	 */
	public final short getBackground() {
		return bgColor;
	}

	/**
	 * Sets the black-white mode attribute.
	 * 
	 * @param blackWhiteAttributeInit
	 *            new black-white mode attribute
	 */
	public final void setBlackWhiteAttribute(
			final short blackWhiteAttributeInit) {
		this.blackWhiteAttribute = blackWhiteAttributeInit;
	}

	/**
	 * Accessor pattern.
	 * 
	 * @return the black-white mode attribute
	 */
	public final short getBlackWhiteAttribute() {
		return this.blackWhiteAttribute;
	}

	/**
	 * Sets the color mode attribute.
	 * 
	 * @param colorAttr
	 *            new color mode attribute
	 */
	public final void setColorAttribute(final short colorAttr) {
		this.colorAttribute = colorAttr;
	}

	/**
	 * Accessor pattern.
	 * 
	 * @return the color mode attribute
	 */
	public final short getColorAttribute() {
		return this.colorAttribute;
	}

	/**
	 * The method sets the foreground color.
	 * 
	 * @param fgColorInit
	 *            value to be set
	 */
	public final void setForeground(final short fgColorInit) {
		verifyColor(fgColorInit);
		this.foreground = fgColorInit;
		// initChtype();
	}

	/**
	 * The method gets the foreground color.
	 * 
	 * @return the foreground color
	 */
	public final short getForeground() {
		return this.foreground;
	}

	/**
	 * Represent the character colors as a string.
	 * 
	 * @return the character colors as a string
	 */
	public final String toString() {
		if (Toolkit.hasColors()) {
			return "[background=" + getColorName(getBackground())
					+ ", foreground=" + getColorName(getForeground()) + "]";
		}

		return "[modi=" + getModusName(getBlackWhiteAttribute()) + "]";
	}

	/**
	 * Gets the colorName attribute of the CharColor object.
	 * 
	 * @param index
	 *            Description of the Parameter
	 * @return The colorName value
	 */
	private String getColorName(final short index) {
		switch (index) {
		case BLACK:
			return "BLACK";
		case WHITE:
			return "WHITE";
		case GREEN:
			return "GREEN";
		case YELLOW:
			return "YELLOW";
		case MAGENTA:
			return "MAGENTA";
		case CYAN:
			return "CYAN";
		case BLUE:
			return "BLUE";
		case RED:
			return "RED";
		default:
			return "UNKNOWN COLOR";
		}
	}

	/**
	 * Gets the modusName attribute of the CharColor object.
	 * 
	 * @param index
	 *            Description of the Parameter
	 * @return The modusName value
	 */
	private String getModusName(final short index) {
		switch (index) {
		case NORMAL:
			return "NORMAL";
		case REVERSE:
			return "REVERSE";
		case BOLD:
			return "BOLD";
		default:
			return "UNKNOWN MODUS";
		}
	}

	/**
	 * Gets the pairNo attribute of the CharColor object.
	 * 
	 * @return The pairNo value
	 */
	protected final short getPairNo() {
		if (Toolkit.hasColors()) {
			return Toolkit.getColorPairNo(this);
		}

		return -1;
	}

	/**
	 * Gets the attribute attribute of the CharColor object.
	 * 
	 * @return The attribute value
	 */
	protected final long getAttribute() {
		if (Toolkit.hasColors()) {
			return Toolkit.mapAttribute(getColorAttribute());
		}

		return Toolkit.mapAttribute(getBlackWhiteAttribute());
	}

	/**
	 * Verify the color attribute as being one we support.
	 * It throws an IllegalArgumentException if the attribute
	 * is not valid.
	 * 
	 * @param attribute
	 *            the color attribute
	 */
	private void verifyAttribute(final short attribute) {
		if ((attribute != NORMAL) && (attribute != REVERSE)
				&& (attribute != BOLD)) {
			throw new IllegalArgumentException("Unknown color attribute:"
					+ attribute);
		}
	}

	/**
	 * Verify the color attribute as being one we support.
	 * It throws an IllegalArgumentException if the attribute
	 * is not valid.
	 * 
	 * @param color
	 *            the color
	 */
	private void verifyColor(final short color) {
		if ((color != BLACK) && (color != RED) && (color != GREEN)
				&& (color != YELLOW) && (color != BLUE) && (color != MAGENTA)
				&& (color != CYAN)
				&& (color != WHITE)) {
			throw new IllegalArgumentException("Unknown color:" + color);
		}
	}
}

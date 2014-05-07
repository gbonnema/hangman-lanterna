/*
 * Created on Jul 23, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

package jcurses.themes;

import jcurses.system.CharColor;
import jcurses.widgets.Window;

/**
 * @author alewis
 * 
 */
public class WindowThemeOverride extends BaseTheme {
	/**
	 * Contains the base theme, if not null
	 */
	private Theme baseTheme = null;

	/**
	 * Default constructor.
	 */
	public WindowThemeOverride() {
	}

	/**
	 * Copy constructor from a base theme.
	 * 
	 * @param aBaseTheme
	 *            the base theme to override the current theme with.
	 */
	public WindowThemeOverride(final Theme aBaseTheme) {
		setBaseTheme(aBaseTheme);
	}

	/**
	 * Returns the color for the specified theme.
	 * 
	 * @param aKey
	 *            the theme key.
	 * @return a CharColor.
	 */
	public CharColor getColor(final String aKey) {
		CharColor mColor = (CharColor) themeKeys.get(aKey);

		if (mColor == null && baseTheme != null) {
			mColor = baseTheme.getColor(aKey);
		}

		if (mColor == null) {
			mColor = Window.getTheme().getColor(aKey);
		}

		return mColor;
	}

	/**
	 * Get the overriding base theme.
	 * @return the base theme.
	 */
	public final Theme getBaseTheme() {
		return baseTheme;
	}

	/**
	 * set the overriding base theme.
	 * @param aBaseTheme the overriding base theme.
	 */
	public final void setBaseTheme(final Theme aBaseTheme) {
		baseTheme = aBaseTheme;
	}
}
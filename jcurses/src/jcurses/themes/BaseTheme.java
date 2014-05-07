package jcurses.themes;

import java.util.HashMap;
import java.util.Map;

import jcurses.system.CharColor;

/**
 * TODO: find out what this class represents
 * 
 * @author alewis
 * 
 */
public class BaseTheme implements Theme {
	/**
	 * 
	 */
	protected Map themeKeys = new HashMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see themes.Theme#getColor(java.lang.String)
	 */
	/**
	 * @param aKey the key to the theme (TODO: what key is that?)
	 * @return a CharColor
	 */
	public CharColor getColor(final String aKey) {
		CharColor mColor = (CharColor) themeKeys.get(aKey);

		if (mColor == null) {
			mColor = (CharColor) themeKeys.get(Theme.COLOR_DEFAULT);
		}

		if (mColor == null) {
			mColor = new CharColor(CharColor.BLACK, CharColor.WHITE);
		}

		return mColor;
	}

	/**
	 * Stores the character color for this theme key.
	 * 
	 * @param aKey the theme key.
	 * @param aColor the color for this theme key.
	 */
	public final void setColor(final String aKey, final CharColor aColor) {
		themeKeys.put(aKey, aColor);
	}

}
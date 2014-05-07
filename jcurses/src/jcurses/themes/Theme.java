package jcurses.themes;

import jcurses.system.CharColor;

/**
 * Adds theme support for JCurses components.
 * 
 * @author alewis
 * 
 */
public interface Theme {
	/**
	 * 
	 */
	String COLOR_DEFAULT = "default";

	/**
	 * 
	 */
	String COLOR_WINDOW_BORDER = "window.border";
	/**
	 * 
	 */
	String COLOR_WINDOW_BACKGROUND = "window.background";
	/**
	 * 
	 */
	String COLOR_WINDOW_TITLE = "window.title";
	/**
	 * 
	 */
	String COLOR_WINDOW_TEXT = "window.text";
	/**
	 * 
	 */
	String COLOR_WINDOW_SHADOW = "window.shadow";

	// String COLOR_DIALOG_BORDER = "dialog.border";
	// String COLOR_DIALOG_BACKGROUND = "dialog.background";
	// String COLOR_DIALOG_TITLE = "dialog.title";
	// String COLOR_DIALOG_TEXT = "dialog.text";
	// String COLOR_DIALOG_SHADOW = "dialog.shadow";

	/**
	 * 
	 */
	String COLOR_WIDGET_BORDER = "widget.border";
	/**
	 * 
	 */
	String COLOR_WIDGET_BACKGROUND = "widget.background";
	/**
	 * 
	 */
	String COLOR_WIDGET_TITLE = "widget.title";
	/**
	 * 
	 */
	String COLOR_WIDGET_TEXT = "widget.text";
	/**
	 * 
	 */
	String COLOR_WIDGET_ACTION = "widget.action";
	/**
	 * 
	 */
	String COLOR_WIDGET_SELECTED = "widget.selected";
	/**
	 * 
	 */
	String COLOR_WIDGET_SHORTCUT = "widget.shortcut";
	/**
	 * 
	 */
	String COLOR_WIDGET_SCROLLBAR = "widget.scrollbar";

	/**
	 * Gets the color with the theme key.
	 * @param aKey the theme key.
	 * @return a CharColor.
	 */
	CharColor getColor(String aKey);

	/**
	 * sets the theme color.
	 * @param aKey the theme key.
	 * @param aColor the CharColor for the theme.
	 */
	void setColor(String aKey, CharColor aColor);

}
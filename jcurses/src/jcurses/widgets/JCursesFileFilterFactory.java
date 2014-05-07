
package jcurses.widgets;

import java.io.FileFilter;

/**
 *  Interface for a file filter factory.
 *
 * @see jcurses.widgets.FileDialog
 *
 */
public interface JCursesFileFilterFactory {

	/**
	 *  Generate a file filter from a string.
	 *
	 * @param  filterString  string showing acceptable file patterns
	 * @return               Filter object modeled on filterString
	 */
	FileFilter generateFileFilter(String filterString);

}

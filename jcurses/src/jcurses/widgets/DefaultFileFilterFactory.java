package jcurses.widgets;

import java.io.File;
import java.io.FileFilter;

/**
 * Factory for file filters used in FileDialog.
 * 
 * @see FileDialog
 */
public class DefaultFileFilterFactory implements JCursesFileFilterFactory {
	/**
	 * Generate a file filter from a string.
	 * 
	 * @param filterString
	 *            string showing acceptable file patterns
	 * @return Filter object modeled on filterString
	 */
	public final FileFilter generateFileFilter(final String filterString) {
		return new DefaultFileFilter(filterString);
	}
}

/**
 * A FileFilter extender to be returned by this the default factory.
 * 
 */
class DefaultFileFilter implements FileFilter {

	/**
	 * 
	 */
	private String filterString = null;

	/**
	 * Constructor for the DefaultFileFilter object.
	 * 
	 * @param aFilterString
	 *            String specifying the filter
	 * @see java.io.FileFilter
	 */
	public DefaultFileFilter(final String aFilterString) {
		if (aFilterString != null) {
			filterString = aFilterString.trim();
		}
	}

	/**
	 * Description of the Method.
	 * 
	 * @param fileF
	 *            File to test for membership in filter set
	 * @return true if member
	 */
	public final boolean accept(final File fileF) {
		if ((filterString == null) || (fileF == null)) {
			return true;
		}

		String file = fileF.getAbsolutePath().trim();

		if (file.lastIndexOf(File.separator) != -1) {
			file =
					file.substring(file.lastIndexOf(File.separator) + 1,
							file.length());
		}

		int index = filterString.indexOf("*");

		if (index == -1) {
			return (filterString.equals(file));
		} else if (index == 0) {
			if (filterString.length() == 1) {
				return true;
			}

			return file.endsWith(filterString.substring(1,
					filterString.length()));
		} else if (index == (filterString.length() - 1)) {
			return file.startsWith(filterString.substring(0,
					filterString.length() - 1));
		} else {
			return (file.startsWith(filterString.substring(0, index)))
					&& (file.endsWith(filterString.substring(index + 1,
							filterString.length())));
		}
	}
}

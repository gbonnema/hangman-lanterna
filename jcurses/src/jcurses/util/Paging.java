package jcurses.util;

/**
 * A utility class to handle paging in components. The class get the 'page size'
 * and the size as constructor argument and calculates page numbers, start and
 * end indexes etc...
 * 
 * 
 */
public class Paging {

	/**
	 * pagesize.
	 */
	private int pageSize = 0;
	/**
	 * the size.
	 */
	private int size = 0;

	/**
	 * The constructor.
	 * 
	 * @param aPageSize
	 *            the page size
	 * @param aSize
	 *            the size
	 * 
	 */
	public Paging(final int aPageSize, final int aSize) {
		pageSize = aPageSize;
		size = aSize;
	}

	/**
	 * The method returns the page number for the given index.
	 * TODO: Document how this really works.
	 * 
	 * @param anIndex
	 *            index, to calculate the page number
	 * @return the page number for the index
	 * 
	 */
	public final int getPageNumber(final int anIndex) {
		int result = 0;
		int index = anIndex;

		if (index <= 0) {
			result = 0;
		} else if (pageSize == 1) {
			result = index;
		} else {
			if ((size - index - 1) < pageSize) {
				index = size - 1;
			}
			/* TODO: document how this works */
			result =
					(index + 1) / pageSize
							+ (((index + 1) % pageSize > 0) ? 1 : 0) - 1;
		}

		return result;
	}

	/**
	 * The method returns the number of pages.
	 * 
	 * @return the number of pages
	 * 
	 */
	public final int getPageSize() {
		return getPageNumber(size) + 1;
	}

	/**
	 * The method returns the start index for the given page.
	 * 
	 * @param pageNumber
	 *            the number of the page to calculate start index
	 * @return start index
	 * 
	 */
	public final int getPageStartIndex(final int pageNumber) {
		int result = Math.max(0, getPageEndIndex(pageNumber) - pageSize + 1);
		return result;
	}

	/**
	 * The method returns the end index for the given page.
	 * 
	 * @param pageNumber
	 *            the number of the page to calculate end index
	 * @return start index
	 * 
	 */
	public final int getPageEndIndex(final int pageNumber) {
		int result = Math.min(size - 1, (pageNumber + 1) * pageSize - 1);
		return result;
	}

	/**
	 * The method returns the page offset for the given index.
	 * 
	 * @param index
	 *            the index to calculate the page offset
	 * @return start index
	 * 
	 */
	public final int getPageOffset(final int index) {
		return index - getPageStartIndex(getPageNumber(index));
	}

	/**
	 * The method returns an index for the given page offset of the given page
	 * The extra handling for the last by one page.
	 * 
	 * @param pageNumber
	 *            pageNumber
	 * @param pageOffset
	 *            page offset
	 * @return index
	 */
	public final int getIndexByPageOffset(final int pageNumber,
			final int pageOffset) {
		int startIndex = getPageStartIndex(pageNumber);
		int index = Math.min(size, startIndex + pageOffset);
		if (getPageNumber(index) != pageNumber) {
			index = startIndex;
		}

		return index;
	}
}
/**
 * 
 */
package util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author gbonnema
 * 
 */
public class TextViewTest {

	private int pageSize = 5;
	private int pageWidth = 40;

	private String text = "This is a new text. And it\n"
			+ "is not any more useful than\n"
			+ "\n\n\njust for test. Let us think, whether"
			+ "this will make any difference.";
	private String onlyLines = "\n\n\n\n";
	private String linesAndPunct = "\n.,.\n\n.\n;;;;;;;\n\n";
	private String emptyText = "";

	private TextView view;
	private TextView emptyView;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		view = new TextView();
		view.formatPage(text, pageSize, pageWidth);

		emptyView = new TextView();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link util.TextView#formatPage(java.lang.String, int, int)}.
	 */
	@Test
	public void testFormatPage() {
		int startLine = 0;
		ArrayList<String> expected = new ArrayList<>();
		expected.add("This is a new text. And it is not any");
		List<String> actual = view.page();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link util.TextView#formatPage(java.lang.String, int, int)}.
	 */
	@Test
	public void testOnlyNewLines() {
		ArrayList<String> expected = new ArrayList<>();
		/* Test empty, nothing added yet */
		List<String> actual = emptyView.page();
		assertEquals(expected, actual);

		/* Now add only newlines and test again */
		emptyView.formatPage(onlyLines, pageSize, pageWidth);
		actual = emptyView.page();
		assertEquals(expected, actual);
	}

}

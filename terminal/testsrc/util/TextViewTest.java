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
 * 
 * @author gbonnema
 * 
 */
public class TextViewTest {





















	private int				pageHeight			= 5;
	private int				pageWidth			= 40;

	/* @formatter:off */
	private String line1 = "Hangman";
	private String line2 = "=======";
	private String line3 = "";
	private String line4 = "This game searches a random word in a ";
	private String line5 = "vocabulary that shows 2 words on each";
	private String line6 = "line. The first is Dutch, the second ";
	private String line7 = "word is English, like this:";
	private String text	= ""
			+ "Hangman\n"
			+ "=======\n"
			+ "\n"
			+ "This game searches a random word in a vocabulary that shows\n"
			+ "2 words on each line. The first is Dutch, the second word is English,\n"
			+ "like this:\n"
			+ "\n"
			+ "een meer;a lake\n"
			+ "een broodje;a roll\n"
			+ "\n"
			+ "The program chooses from about 300 words.\n"
			+ "\n"
			+ "The goal of the game is to guess a word correctly, thus saving\n"
			+ "a person from the gallows. The player wins if she guesses right before\n"
			+ "execution. Otherwise she loses.\n\n";
	/* @formatter:on */

	private String		onlyLines			= "\n\n\n\n";
	private String		linesAndPunct	= "\n.,.\n\n.\n;;;;;;;\n\n";
	private String		emptyText			= "";

	private TextView	view;
	private TextView	emptyView;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		view = new TextView();
		view.formatPage(text, pageWidth, pageHeight);

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
		ArrayList<String> expected = new ArrayList<>();
		expected.add(line1);
		expected.add(line2);
		expected.add(line3);
		expected.add(line4);
		expected.add(line5);
		expected.add(line6);
		expected.add(line7);
		List<String> actual = view.formatLines(text, pageWidth);
		// assertTrue(actual.containsAll(expected));
		assertEquals(expected.toString(), actual.subList(0, 7).toString());
	}

	/**
	 * Test method for
	 * {@link util.TextView#formatPage(java.lang.String, int, int)}.
	 */
	@Test
	public void testOnlyNewLines() {
		ArrayList<String> expected = new ArrayList<>();
		/* Test empty, nothing added yet */
		List<String> actual = emptyView.nextPage();
		assertEquals(expected, actual);

		/* Now add only newlines and test again */
		emptyView.formatPage(onlyLines, pageWidth, pageHeight);
		actual = emptyView.nextPage();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link util.TextView#formatPage(java.lang.String, int, int)}.
	 */
	@Test
	public void testNewLinesPlus() {
		ArrayList<String> expected = new ArrayList<>();
		expected.add("");
		expected.add(".,.");
		expected.add("");
		expected.add(".");
		expected.add(";;;;;;;");

		/* add newlines + punctuation and test again */
		emptyView.formatPage(linesAndPunct, pageWidth, pageHeight);
		List<String> actual = emptyView.nextPage();
		assertEquals(expected, actual);
	}
}

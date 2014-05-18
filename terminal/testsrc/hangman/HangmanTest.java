/**
 * 
 */
package hangman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.ExperimentException;

/**
 * @author gbonnema
 * 
 */
public class HangmanTest {

	private String emptyWord = "";
	private String word = "abcdb";
	/* ========================================== */
	private String guess0toolong = "too long";
	private String guess1wrong = "x";
	private String guess2ok = "d";
	private String guess3ok = "b";
	/* ========================================== */
	private String expectResult1 = "[_, _, _, _, _]";
	private String expectResult2 = "[_, _, _, d, _]";
	private String expectResult3 = "[_, b, _, _, b]";
	/* ========================================== */
	private Hangman hangman;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		hangman = new Hangman(word);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link hangman.Hangman#guess(java.lang.String)}.
	 */
	@Test
	public void testGuessEmpty() {
		try {
			new Hangman(emptyWord);
			fail("Empty Hangman should not succeed.");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link hangman.Hangman#guess(java.lang.String)}.
	 */
	@Test
	public void testGuess() throws ExperimentException {
		try {
			hangman.guess(emptyWord);
			fail("Should throw an exception.");
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	/**
	 * Test method for {@link hangman.Hangman#guess(java.lang.String)}.
	 */
	@Test
	public void testGuessTooLong() throws ExperimentException {
		try {
			hangman.guess(this.guess0toolong);
			fail("Should throw an exception.");
		} catch (Exception e) {
			assertTrue(true);
		}

	}

	/**
	 * Test method for {@link hangman.Hangman#guess(java.lang.String)}.
	 */
	@Test
	public void testGuess1wrong() throws ExperimentException {
		hangman.guess(guess1wrong);
		if (hangman.getLastGuess().isInWord()) {
			fail("Should not have guessed char: " + guess1wrong);
		} else {
			String actual = hangman.getCorrectlyGuessedArray().toString();
			assertEquals(expectResult1, actual.toString());
		}

	}

	/**
	 * Test method for {@link hangman.Hangman#guess(java.lang.String)}.
	 */
	@Test
	public void testGuess2ok() throws ExperimentException {
		hangman.guess(guess2ok);
		if (hangman.getLastGuess().isInWord()) {
			String actual = hangman.getCorrectlyGuessedArray().toString();
			assertEquals(expectResult2, actual.toString());
		} else {
			fail("Should have guessed char: " + guess2ok);
		}

	}

	/**
	 * Test method for {@link hangman.Hangman#guess(java.lang.String)}.
	 */
	@Test
	public void testGuess3ok() throws ExperimentException {
		hangman.guess(guess3ok);
		if (hangman.getLastGuess().isInWord()) {
			String actual = hangman.getCorrectlyGuessedArray().toString();
			assertEquals(expectResult3, actual.toString());
		}
	}

}

/**
 * 
 */
package hangman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.ExperimentException;

/**
 * @author gbonnema
 * 
 */
public class HangmanTest {

	private String	emptyWord			= "";
	private String	word					= "abcdb";
	private String	guess0toolong	= "too long";
	private String	guess1wrong		= "x";
	private String	guess2ok			= "d";
	private String	guess3ok			= "b";
	/* ========================== "abcdb"; */
	private String	expectGuess1	= "_____";
	private String	expectGuess2	= "___d_";
	private String	expectGuess3	= "_b__b";
	private String	expectResult1	= "_____";
	private String	expectResult2	= "___d_";
	private String	expectResult3	= "_b_db";
	private Hangman	hangman;

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
		ArrayList<Character> actual;
		try {
			actual = hangman.guess(emptyWord);
			fail("Should throw an exception.");
		} catch (Exception e) {
			assertTrue(true);
		}

		try {
			actual = hangman.guess(this.guess0toolong);
			fail("Should throw an exception.");
		} catch (Exception e) {
			assertTrue(true);
		}

		actual = hangman.guess(guess1wrong);
		assertEquals(expectGuess1, actual.toString());
		actual = hangman.guess(guess2ok);
		assertEquals(expectGuess2, actual.toString());
		actual = hangman.guess(guess3ok);
		assertEquals(expectGuess3, actual.toString());
	}

	/**
	 * Test method for {@link hangman.Hangman#updateGuessOk(char[])}.
	 */
	@Test
	public void testUpdateGuess() throws ExperimentException {
		ArrayList<Character> actual;
		ArrayList<Character> chArr;

		chArr = hangman.guess(guess1wrong);
		if (hangman.updateGuessOk(chArr)) {
			fail("Should not have guessed char: " + guess1wrong);
		} else {
			actual = hangman.getCorrectlyGuessedArray();
			assertEquals(expectResult1, actual.toString());
		}

		chArr = hangman.guess(guess2ok);
		if (hangman.updateGuessOk(chArr)) {
			actual = hangman.getCorrectlyGuessedArray();
			assertEquals(expectResult2, actual.toString());
		} else {
			fail("Should have guessed char: " + guess2ok);
		}

		chArr = hangman.guess(guess3ok);
		if (hangman.updateGuessOk(chArr)) {
			actual = hangman.getCorrectlyGuessedArray();
			assertEquals(expectResult3, actual.toString());
		}
	}

	/**
	 * Test method for {@link hangman.Hangman#getHangFig()}.
	 */
	@Test
	public void testGetHangFig() {
	}

}

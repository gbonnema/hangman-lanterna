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

	private String	emptyWord			= "";
	private String	word					= "abcdb";
	private String	guess0toolong	= "too long";
	private String	guess1				= "x";
	private String	guess2				= "d";
	private String	guess3				= "b";
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
		} catch (ExperimentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test method for {@link hangman.Hangman#guess(java.lang.String)}.
	 */
	@Test
	public void testGuess() throws ExperimentException {
		char[] actual;
		try {
			actual = hangman.guess(emptyWord);
			fail("Should throw an exception.");
		} catch (ExperimentException e) {
			assertTrue(true);
		}

		try {
			actual = hangman.guess(this.guess0toolong);
			fail("Should throw an exception.");
		} catch (ExperimentException e) {
			assertTrue(true);
		}

		actual = hangman.guess(guess1);
		assertEquals(expectGuess1, new String(actual));
		actual = hangman.guess(guess2);
		assertEquals(expectGuess2, new String(actual));
		actual = hangman.guess(guess3);
		assertEquals(expectGuess3, new String(actual));
	}

	/**
	 * Test method for {@link hangman.Hangman#updateGuess(char[])}.
	 */
	@Test
	public void testUpdateGuess() throws ExperimentException {
		char[] actual;
		char[] chArr;
		chArr = hangman.guess(guess1);
		actual = hangman.updateGuess(chArr);
		assertEquals(expectResult1, new String(actual));
		chArr = hangman.guess(guess2);
		actual = hangman.updateGuess(chArr);
		assertEquals(expectResult2, new String(actual));
		chArr = hangman.guess(guess3);
		actual = hangman.updateGuess(chArr);
		assertEquals(expectResult3, new String(actual));
	}

	/**
	 * Test method for {@link hangman.Hangman#getHangFig()}.
	 */
	@Test
	public void testGetHangFig() {
	}

}

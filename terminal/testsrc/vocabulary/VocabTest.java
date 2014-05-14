/**
 * 
 */
package vocabulary;

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
public class VocabTest {

	private Vocab	_vocab;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		_vocab = new Vocab("nederlands.csv");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link vocabulary.Vocab#Vocab(java.lang.String)}.
	 */
	@Test
	public void testVocab1() throws Exception {

		String expectedNL1 = "een meer";
		String expectedEN1 = "a lake";

		String actualNL1 = _vocab.getNL(0);
		String actualEN1 = _vocab.getEN(0);

		assertEquals(expectedNL1, actualNL1);
		assertEquals(expectedEN1, actualEN1);
	}

	/**
	 * Test method for {@link vocabulary.Vocab#Vocab(java.lang.String)}.
	 */
	@Test
	public void testVocab2() throws Exception {

		String expectedNL2 = "een broodje";
		String expectedEN2 = "a roll";

		String actualNL2 = _vocab.getNL(1);
		String actualEN2 = _vocab.getEN(1);

		assertEquals(expectedNL2, actualNL2);
		assertEquals(expectedEN2, actualEN2);
	}

	/**
	 * Test method for {@link vocabulary.Vocab#Vocab(java.lang.String)}.
	 */
	@Test
	public void testVocabLast() throws Exception {

		int size = _vocab.getSize();

		String expectedNLLast = "je voornaam";
		String expectedENLast = "your first name";

		String actualNLLast = _vocab.getNL(size - 1);
		String actualENLast = _vocab.getEN(size - 1);

		assertEquals(expectedNLLast, actualNLLast);
		assertEquals(expectedENLast, actualENLast);
	}

	/**
	 * Test method for {@link vocabulary.Vocab#Vocab(java.lang.String)}.
	 */
	@Test
	public void testException() throws Exception {

		try {
			Vocab vocab = new Vocab("xxx.csv");
			fail("Should have thrown an exception.");
		} catch (ExperimentException e) {
			assertTrue(true);
		}
	}
}

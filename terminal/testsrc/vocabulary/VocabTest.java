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

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
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
	public void testVocab() throws Exception {
		Vocab vocab = new Vocab("nederlands.csv");

		String expectedPrefix =
				"/home/gbonnema/projects/ws"
						+ "/terminal/nederlands.csv: [een meer, een broodje";
		String expectedSuffix = "voornaam]";
		String actual = vocab.toString();
		assertTrue(actual.startsWith(expectedPrefix));
		int endOffset = actual.length() - expectedSuffix.length();
		assertEquals(expectedSuffix, actual.substring(endOffset));
		// assertTrue(actual.endsWith(expectedSuffix));
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

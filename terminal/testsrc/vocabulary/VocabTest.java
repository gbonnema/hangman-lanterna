/**
 * 
 */
package vocabulary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		String expectedPrefix, expectedSuffix, actual;
		Vocab vocab = new Vocab("nederlands.csv");

		expectedPrefix =
				"/home/gbonnema/projects/ws"
						+ "/terminal/nederlands.csv: [een meer, een broodje";
		expectedSuffix = "je voornaam]";
		actual = vocab.toString();
		assertTrue(actual.startsWith(expectedPrefix));
		assertTrue(actual.endsWith(expectedSuffix));
		assertEquals(actual, expectedSuffix);
	}
}

/**
 * 
 */
package vocabulary;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import util.ExperimentException;
import util.Utils;

/**
 * Vocab reads a vocabulary from a file with the csv format: '<word>;<word>"\n"'
 * 
 * You can get the size with vocab.getSize();
 * 
 * 
 * Remark: The header could contain something like: 'Title:;Nederlands.kvtml' or
 * 'Author:;'. These entries Vocab will skip.
 * 
 * @author gbonnema
 * 
 */
public class Vocab {

	private String						_absPathName;
	private ArrayList<String>	_wordListNL;
	private ArrayList<String>	_wordListEN;

	public Vocab(String fname) throws ExperimentException, IOException {
		_wordListNL = new ArrayList<>();
		_wordListEN = new ArrayList<>();
		Utils.checkNotNull(fname, "fname");
		File file = new File(fname);
		Utils.checkNotNull(file, "vocabulary filename for " + fname);
		_absPathName = file.getAbsolutePath();
		if (file.exists() && file.canRead() && file.isFile()) {
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			Scanner in = new Scanner(bin);
			while (in.hasNext()) {
				String line = in.nextLine();
				if (line.matches(".*:;.*$")) {
					continue;
				}
				String[] words = line.split(";");
				if (words == null || words.length == 0) {
					continue;
				}
				String word = words[0];
				_wordListNL.add(word);
				word = words[1];
				_wordListEN.add(word);
			}
			in.close();
		} else {
			String msg =
					"File " + fname + " does not exist " + "or is not readable "
							+ "or is not a file.";
			throw new ExperimentException(msg);
		}
	}

	public VocabEntry getRandomEntry() {
		double random = Math.random();
		int index = (int) (random * getSize());
		return getEntry(index);
	}

	public VocabEntry getEntry(int index) {
		Utils.checkArg(index >= 0 && index < getSize(), "Index out of range.");
		return new VocabEntry(index, getNL(index), getEN(index));
	}

	/**
	 * Get the Dutch word at the specified index (0-based).
	 * 
	 * @param index
	 *          the specified index (0-based)
	 * @return the Dutch word at the specified index
	 */
	public String getNL(int index) {
		return _wordListNL.get(index);
	}

	/**
	 * Get the English word at the specified index (0-based).
	 * 
	 * @param index
	 *          the specified index (0-based)
	 * @return the English word at the specified index
	 */
	public String getEN(int index) {
		return _wordListEN.get(index);
	}

	/**
	 * 
	 * @return the number of entries in the NL - EN vocabulary.
	 * @throws ExperimentException
	 *           when NL and EN sizes differ. This should never happen.
	 */
	public int getSize() {
		Utils.checkInternal(_wordListNL.size() == _wordListEN.size(),
				"Size of vocabularies are unequal.");
		return _wordListNL.size();
	}

	/**
	 * Print the vocabulary.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("NL ").append(_absPathName).append(": ").append(_wordListNL);
		result.append("\n");
		result.append("EN ").append(_absPathName).append(": ").append(_wordListEN);
		result.append("\n");

		result.append("Vocabulary NL - EN:\n");
		for (int i = 0; i < _wordListNL.size(); i++) {
			result.append(_wordListNL.get(i)).append(" : ")
					.append(_wordListEN.get(i)).append("\n");
		}
		result.append("\n");

		return result.toString();
	}

	/**
	 * Data object. Public access to items.
	 * 
	 * @author gbonnema
	 * 
	 */
	public class VocabEntry {

		public int		_index;
		public String	_wordNL;
		public String	_wordEN;

		public VocabEntry(final int index, final String wordNL, final String wordEN) {
			_wordNL = wordNL;
			_wordEN = wordEN;
			_index = index;
		}

	}
}
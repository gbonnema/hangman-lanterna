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
 * @author gbonnema
 * 
 */
public class Vocab {

	private String absPathName;
	private ArrayList<String> wordList;

	public Vocab(String fname) throws ExperimentException, IOException {
		wordList = new ArrayList<>();
		Utils.checkNotNull(fname, "fname");
		File file = new File(fname);
		Utils.checkNotNull(file, "vocabulary filename");
		absPathName = file.getAbsolutePath();
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
				wordList.add(word);
			}
			in.close();
		}
	}

	/**
	 * Print the vocabulary
	 */
	@Override
	public String toString() {
		return String.format("%s: %s\n", absPathName, wordList);
	}
}

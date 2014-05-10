/**
 * 
 */
package util;

/**
 * @author gbonnema
 * 
 */
public class ExperimentException extends Exception {

	/**
	 * serial number for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ExperimentException() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param msg
	 */
	public ExperimentException(String msg) {
		super(msg);
	}

}

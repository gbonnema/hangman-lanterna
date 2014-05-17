/**
 * Copyright 2014 Guus Bonnema, Dieren, The Netherlands.
 * 
 * This file is part of hangman-lanterna.
 * 
 * hangman-lanterna is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * hangman-lanterna is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * hangman-lanterna. If not, see <http://www.gnu.org/licenses/>.
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

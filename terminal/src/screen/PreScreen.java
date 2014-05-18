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
package screen;

import util.ExperimentException;
import util.Utils;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * @author gbonnema
 * 
 */
public class PreScreen implements TextDraw {

	private final int _padding = 2;

	public static enum ScreenType {
		UNKNOWN, GUI, SCREEN, TERM
	};

	private boolean _keepRunning;

	private Screen _screen;
	private Terminal _terminal;
	private ScreenWriter _screenWriter;
	private int _screenWidth;
	private int _screenHeight;
	private int _centerLine;
	private int _centerCol;

	private ScreenTypePanel _screenTypePanel;

	/**
	 * 
	 */
	public PreScreen() {
		_screen = TerminalFacade.createScreen();
		_terminal = _screen.getTerminal();
		_screen.startScreen();
		initializeScreen();
		initTerminalSize();

		rebuildScreen();
		refreshScreen();
	}

	/**********************************************************
	 * RUN LOOP: INPUT RETRIEVAL
	 **********************************************************/

	/**
	 * Runs the application until the user presses Escape
	 * 
	 * @throws ExperimentException
	 *           for failed readInput.
	 */
	public ScreenType run() {

		ScreenType screenType = ScreenType.UNKNOWN;
		_keepRunning = true;
		while (_keepRunning) {
			Key key = _screen.readInput();
			if (key != null) {
				screenType = handleInput(key);
			}
			if (screenType != ScreenType.UNKNOWN) {
				break;
			}
			if (_screen.resizePending()) {
				_screen.refresh();
				rebuildScreen();
				_screen.refresh();
			}
		}

		_screen.stopScreen();

		return screenType;
	}

	private ScreenType handleInput(Key key) {
		ScreenType screenType = ScreenType.UNKNOWN;
		Kind kind = key.getKind();
		if (kind == Key.Kind.Escape) {
			_keepRunning = false;
		} else {
			screenType = handleScreenInput(key);
			_screen.refresh();
		}
		return screenType;
	}

	private ScreenType handleScreenInput(Key key) {
		ScreenType screenType = ScreenType.UNKNOWN;
		Character ch = key.getCharacter();
		if (ch.equals("g") || ch.equals("G")) {
			screenType = ScreenType.GUI;
		} else if (ch.equals("s") || ch.equals("S")) {
			screenType = ScreenType.SCREEN;
		} else {
			screenType = ScreenType.UNKNOWN;
		}
		return screenType;
	}

	/**
	 * Initialize screen variables, only needed at startup. Do not call after
	 * initial creation.
	 */
	private void initializeScreen() {
		_terminal = _screen.getTerminal();
		_screenWriter = new ScreenWriter(_screen);
		_screenWriter.setForegroundColor(Terminal.Color.BLUE);
		_screenWriter.setBackgroundColor(Terminal.Color.WHITE);
	}

	public void stopScreen() {
		_screen.stopScreen();
	}

	/**
	 * Initialize the terminal sizes.
	 */
	private void initTerminalSize() {
		_screenWidth = _screen.getTerminalSize().getColumns();
		_screenHeight = _screen.getTerminalSize().getRows();
		_centerCol = _screenWidth / 2;
		_centerLine = _screenHeight / 2;
	}

	private void rebuildScreen() {

		_screenTypePanel = new ScreenTypePanel(this);

		int left = _centerCol + _padding;
		int y = _centerLine + _padding;
		TerminalPosition topLeft = new TerminalPosition(left, y);

		int width = _screenWidth - _padding - left;
		width = width > 80 ? 80 : width;
		int height = (int) ((_screenHeight - _padding - y) * 0.6);
		TerminalPosition panelSize = new TerminalPosition(width, height);

		_screenTypePanel.resetPanel(topLeft, panelSize);
		_screenTypePanel.refresh();
	}

	private String createHorLine(String ch) {
		int length = _screen.getTerminalSize().getColumns();
		return Constructor.createHorLine(ch, length);
	}

	/*
	 * screen.clear() uses default colors which is why we need to wipe the screen
	 * by writing empty lines.
	 */
	private void wipeScreen() {
		for (int i = 0; i < _screen.getTerminalSize().getRows(); i++) {
			drawHorLine(i, " ");
		}
	}

	@Override
	public void drawHorDashLine(int line) {
		drawHorLine(line, "-");
	}

	private void drawHorLine(int line, String ch) {
		String horLine = createHorLine(ch);
		_screenWriter.drawString(0, line, horLine);
	}

	@Override
	public void drawHorLine(int col, int line, int width, String ch)
			throws ExperimentException {
		String horLine = Constructor.createHorLine(ch, width);
		_screenWriter.drawString(col, line, horLine);
	}

	@Override
	public void drawBox(final TerminalPosition from, final TerminalPosition to) {

		String topStr, bottomStr, midStr;
		int left = from.getColumn();
		int right = to.getColumn();
		int top = from.getRow();
		int bottom = to.getRow();
		int width = right - left + 1;
		Utils.checkArg(width >= 5, "minimum width box = 5. specified: " + width
				+ "\n" + "\n" + "from = " + from + ", to = " + to + "\n");

		topStr = Constructor.fillLine('+', '+', '-', width);
		bottomStr = Constructor.fillLine('+', '+', '-', width);
		midStr = Constructor.fillLine('|', '|', ' ', width);
		/* Draw top and bottom first */
		_screenWriter.drawString(left, top, topStr);
		_screenWriter.drawString(left, bottom, bottomStr);
		/* Now draw the middle */
		int height = bottom - top + 1;
		int innerheight = height - 2;
		for (int i = 0; i < innerheight; i++) {
			int x = left;
			int y = top + 1 + i;
			_screenWriter.drawString(x, y, midStr);
		}
	}

	/**
	 * Return the absolute screen.
	 */
	@Override
	public ScreenWriter getAbsScreenWriter() {
		return _screenWriter;
	}

	@Override
	public void refreshScreen() {
		_screen.refresh();
	}
}

/**
 * 
 */
package window;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;

/**
 * @author gbonnema
 * 
 */
public class HangmanWindow extends Window {

	private static Panel.Orientation VERTICAL = Panel.Orientation.VERTICAL;
	private static Panel.Orientation HORIZONTAL = Panel.Orientation.HORISONTAL;

	private Panel _overallPanel;
	private Panel _leftPanel;
	private Panel _rightPanel;
	private Panel _docPanel;
	private Panel _figurePanel;
	private Panel _gameMessagePanel;
	private Panel _gameSolutionPanel;
	private Panel _wordProgressPanel;

	/**
	 * @param title
	 */
	public HangmanWindow(String title) {
		super(title);

		_overallPanel = new Panel(new Border.Invisible(), HORIZONTAL);

		boolean raised = true;
		Border raisedBorder = new Border.Bevel(raised);
		Border loweredBorder = new Border.Bevel(false);
		_leftPanel = new Panel(raisedBorder, VERTICAL);
		_rightPanel = new Panel(raisedBorder, VERTICAL);

		_docPanel = new Panel(loweredBorder, HORIZONTAL);
		_docPanel.addComponent(new Label("_docPanel"));
		_gameSolutionPanel = new Panel(loweredBorder, HORIZONTAL);
		_gameSolutionPanel.addComponent(new Label("_gameSolutionPanel"));
		_wordProgressPanel = new Panel(loweredBorder, HORIZONTAL);
		_wordProgressPanel.addComponent(new Label("_wordProgressPanel"));

		_leftPanel.addComponent(_figurePanel);
		_leftPanel.addComponent(_wordProgressPanel);
		_leftPanel.addComponent(_gameMessagePanel);
		_rightPanel.addComponent(_docPanel);
		_rightPanel.addComponent(_gameSolutionPanel);

		_overallPanel.addComponent(_leftPanel);
		_overallPanel.addComponent(_rightPanel);

		addComponent(_overallPanel);
	}
}

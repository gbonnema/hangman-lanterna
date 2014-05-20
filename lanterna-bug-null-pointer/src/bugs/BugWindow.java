/**
 * 
 */
package bugs;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;

/**
 * @author gbonnema
 * 
 */
public class BugWindow extends Window {

	private static Panel.Orientation _HORIZONTAL = Panel.Orientation.HORISONTAL;
	private static Panel.Orientation _VERTICAL = Panel.Orientation.VERTICAL;
	private static Border.Invisible _noborder = new Border.Invisible();
	private static Border.Bevel _bevelRaised = new Border.Bevel(true);

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
	public BugWindow(String title) {
		super(title);

		_overallPanel = new Panel(_noborder, _HORIZONTAL);

		boolean raised = true;
		Border raisedBorder = new Border.Bevel(raised);
		Border loweredBorder = new Border.Bevel(false);
		_leftPanel = new Panel(raisedBorder, _VERTICAL);
		_rightPanel = new Panel(raisedBorder, _VERTICAL);

		_docPanel = new Panel(loweredBorder, _HORIZONTAL);
		_docPanel.addComponent(new Label("_docPanel"));
		_gameSolutionPanel = new Panel(loweredBorder, _HORIZONTAL);
		_gameSolutionPanel.addComponent(new Label("_gameSolutionPanel"));
		_wordProgressPanel = new Panel(loweredBorder, _HORIZONTAL);
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

package jcurses.widgets;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.event.ItemEvent;
import jcurses.event.ItemListener;

import jcurses.system.InputChar;
import jcurses.system.Toolkit;

import jcurses.util.Message;

import java.io.File;
import java.io.FileFilter;

/**
 * This class implements a file select dialog.
 */
public class FileDialog extends ModalDialog implements WidgetsConstants,
		ItemListener, ActionListener {
	/**
	 * The reciproke of half a screen width.
	 */
	private static final double REL_WIDTH = 0.5;
	/**
	 * The reciproke of half a screen height.
	 */
	private static final double REL_HEIGHT = 0.5;
	/**
	 * The relative portion of margin left of the dialog.
	 */
	private static final double REL_LEFT_MARGIN = 0.25;
	/**
	 * The relative portion of margin above the dialog.
	 */
	private static final double REL_TOP_MARGIN = 0.25;
	/**
	 * The screen width.
	 */
	private static final int SCR_WIDTH = Toolkit.getScreenWidth();
	/**
	 * The screen height.
	 */
	private static final int SCR_HEIGHT = Toolkit.getScreenHeight();
	/**
	 * The top left x coordinate.
	 */
	private static final int TOP_LEFT_X = (int) (SCR_WIDTH * REL_LEFT_MARGIN);
	/**
	 * The top left y coordinate.
	 */
	private static final int TOP_LEFT_Y = (int) (SCR_WIDTH * REL_TOP_MARGIN);
	/**
	 * The width of the dialog.
	 */
	private static final int WIDTH = (int) (SCR_WIDTH * REL_WIDTH);
	/**
	 * the height of the dialog.
	 */
	private static final int HEIGHT = (int) (SCR_HEIGHT * REL_HEIGHT);
	/**
	 * 
	 */
	private static InputChar returnChar = new InputChar('\n');
	/**
	 * 
	 */
	private Button cancelButton = null;
	/**
	 * 
	 */
	private Button okButton = null;
	/**
	 * 
	 */
	private JCursesFileFilterFactory filterFactory =
			new DefaultFileFilterFactory();
	/**
	 * 
	 */
	private Label fileLabel = null;
	/**
	 * 
	 */
	private Label filterLabel = null;
	/**
	 * 
	 */
	private List directoryList = null;
	/**
	 * 
	 */
	private List fileList = null;
	/**
	 * 
	 */
	private String ioErrorText = "Input/Output Error occurred!";
	/**
	 * 
	 */
	private String ioErrorTitle = "I/O Error";
	/**
	 * 
	 */
	private TextField fileTextField = null;
	/**
	 * 
	 */
	private TextField filterTextField = null;
	/**
	 * 
	 */
	private String directory = null;
	/**
	 * 
	 */
	private String file = null;
	/**
	 * 
	 */
	private String filterString = null;
	/**
	 * The resulting choice of file or directory.
	 */
	private String fileChoice = null;
	/**
	 * 
	 */
	private boolean inRoots = false;

	/**
	 * A constructor accepting X and Y coordinates, height, width, and a title
	 * string.
	 * 
	 * @param x
	 *            the x coordinate of the dialog window's top left corner
	 * @param y
	 *            the y coordinate of the dialog window's top left corner
	 * @param width
	 *            .
	 * @param height
	 *            .
	 * @param title
	 *            dialog's title
	 */
	public FileDialog(final int x, final int y, final int width,
			final int height, final String title) {
		super(x, y, width, height, true, title);

		directoryList = new List();
		directoryList.setSelectable(false);
		directoryList.setTitle("Directories");
		directoryList.addListener(this);
		fileList = new List();
		fileList.setSelectable(false);
		fileList.setTitle("Files");
		fileList.addListener(this);
		fileLabel = new Label("File: ");
		filterLabel = new Label("Filter: ");
		fileTextField = new TextField();
		fileTextField.setText(getCurrentFileContent());
		filterTextField = new FilterTextField(this);
		okButton = new Button("OK");
		okButton.addListener(this);
		cancelButton = new Button("Cancel");
		cancelButton.addListener(this);

		Panel topPanel = new Panel();
		GridLayoutManager topManager = new GridLayoutManager(2, 1);
		topPanel.setLayoutManager(topManager);
		topManager.addWidget(directoryList, 0, 0, 1, 1, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);
		topManager.addWidget(fileList, 1, 0, 1, 1, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);

		// TODO: Clean up all the constants to understandable variables.
		Panel bottomPanel = new Panel();
		GridLayoutManager bottomManager = new GridLayoutManager(4, 4);
		bottomPanel.setLayoutManager(bottomManager);
		bottomManager.addWidget(fileLabel, 0, 0, 1, 1, ALIGNMENT_CENTER,
				ALIGNMENT_RIGHT);
		bottomManager.addWidget(filterLabel, 0, 2, 1, 1, ALIGNMENT_CENTER,
				ALIGNMENT_RIGHT);
		bottomManager.addWidget(fileTextField, 1, 0, 2, 1, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);
		bottomManager.addWidget(filterTextField, 1, 2, 2, 1, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);
		bottomManager.addWidget(okButton, 3, 0, 1, 1, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);
		bottomManager.addWidget(cancelButton, 3, 2, 1, 1, ALIGNMENT_CENTER,
				ALIGNMENT_CENTER);

		DefaultLayoutManager manager =
				(DefaultLayoutManager) getRootPanel().getLayoutManager();

		manager.addWidget(topPanel, 0, 0, width - 2, height - 6,
				ALIGNMENT_CENTER, ALIGNMENT_CENTER);
		manager.addWidget(bottomPanel, 0, height - 6, width - 2, 4,
				ALIGNMENT_CENTER, ALIGNMENT_CENTER);

		fillListWidgets(getCurrentDirectory());
	}

	/**
	 * The constructor.
	 * 
	 * @param title
	 *            dialog's title.
	 */
	public FileDialog(final String title) {
		this(TOP_LEFT_X, TOP_LEFT_Y, WIDTH, HEIGHT, title);
	}

	/**
	 * The default constructor.
	 * 
	 */
	public FileDialog() {
		this(null);
	}

	/**
	 * Returns the last selected file. Should be called after a return from the
	 * <code>show</code> method to read the result. If <code>null</code> is
	 * returned, no file was selected.
	 * 
	 * @return selected file
	 */
	public final File getChosenFile() {
		if (fileChoice == null) {
			return null;
		}

		return new File(fileChoice);
	}

	/**
	 * Sets the default selected file.
	 * 
	 * @param aFile
	 *            default selected file, if null, no file is selected per
	 *            default.
	 */
	public final void setDefaultFile(final String aFile) {
		file = aFile;
	}

	/**
	 * @return default selected file
	 */
	public final String getDefaultFile() {
		return file;
	}

	/**
	 * Sets the current directory.
	 * 
	 * @param aDirectory
	 *            current directory
	 */
	public final void setDirectory(final String aDirectory) {
		directory = aDirectory;
	}

	/**
	 * @return current directory
	 * 
	 */
	public final String getDirectory() {
		return directory;
	}

	/**
	 * 
	 * @param aFilterFactory
	 *            the filter factory.
	 */
	public final void setFilterFactory(
			final JCursesFileFilterFactory aFilterFactory) {
		filterFactory = aFilterFactory;
	}

	/**
	 * Sets a filter string
	 * 
	 * Sets a string used to filter the files, that are shown in selected
	 * directories. The filter string can be also modified by user. The filter
	 * string has to be in the form <prefix>* <posfix>, and matches all files,
	 * whose names start with <prefix>and end with <postfix>. Both <prefix> and
	 * <postfix> can be empty.
	 * 
	 * @param aFilterString
	 *            filter string
	 */
	public final void setFilterString(final String aFilterString) {
		filterString = aFilterString;
	}

	/**
	 * @return filter string
	 * 
	 */
	public final String getFilterString() {
		return filterString;
	}

	/**
	 * Sets the text of the message, that is shown, if an i/o error occurred
	 * while the dialog tries to open a directory.
	 * 
	 * @param message
	 *            i/o error message's text
	 */
	public final void setIOErrorMessageText(final String message) {
		ioErrorText = message;
	}

	/**
	 * Sets the title of the message, that is shown, if an i/o error occurred
	 * while the dialog tries to open a directory.
	 * 
	 * @param message
	 *            i/o error message's text
	 */
	public final void setIOErrorMessageTitle(final String message) {
		ioErrorTitle = message;
	}

	/**
	 * @param e
	 *            the action event that triggered this.
	 */
	public final void actionPerformed(final ActionEvent e) {
		if (e.getSource() == okButton) {
			saveResult();
			close();
		} else {
			close();
		}
	}

	/**
	 * @param event
	 *            the Item event that triggered this.
	 */
	public final void stateChanged(final ItemEvent event) {
		if (event.getSource() == directoryList) {
			file = null;
			String item = ((String) event.getItem()).trim();
			String backupDirectory = directory;

			if (item.equals("..")) {
				File directoryFile = new File(directory);

				if (directoryFile.getParentFile() == null) {
					// This can occur only by Win32
					inRoots = true;
					updateListWidgets(true);
					return;
				}

				directory =
						new File(directory).getParentFile().getAbsolutePath();
			} else {
				if (!inRoots) {
					directory = getCurrentDirectory() + event.getItem();
				} else {
					directory = (String) event.getItem();
				}
			}

			if (!checkDirectory(directory)) {
				directoryReadErrorMessage();
				directory = backupDirectory;
				return;
			}

			inRoots = false;

			updateListWidgets();
			updateFileField();
		} else if (event.getSource() == fileList) {
			file = getCurrentDirectory() + event.getItem();
			updateFileField();
		}
	}

	/**
	 * Shows the i/o error message occurring when reading a directory. In the
	 * default implementation uses texts set with
	 * <code>setIOErrorMessageTitle</code> and
	 * <code>setIOErrorMessageText</code>. can be modified in derived classes.
	 */
	protected final void directoryReadErrorMessage() {
		new Message(ioErrorTitle, ioErrorText, "OK").show();
	}

	/**
	 * @param inp
	 *            the input character
	 */
	protected final void onChar(final InputChar inp) {
		if (inp.equals(returnChar)) {
			if (filterTextField.hasFocus()) {
				setFilterString(filterTextField.getText());
				updateListWidgets();
			} else {
				saveResult();
				close();
			}
		}
	}

	/**
	 * 
	 * @return the current directory.
	 */
	private String getCurrentDirectory() {
		if (directory == null) {
			directory = System.getProperty("user.dir");
		}

		File directoryFile = new File(directory);
		String directoryPath = directoryFile.getAbsolutePath().trim();

		if (!directoryPath.endsWith(File.separator)) {
			directoryPath = directoryPath + File.separator;
		}

		return directoryPath;
	}

	/**
	 * 
	 * @return the current file content or the current directory, if no file is
	 *         current.
	 */
	private String getCurrentFileContent() {
		String content;
		if (file != null) {
			content = file;
		} else {
			content = getCurrentDirectory();
		}

		return content;
	}

	/**
	 * 
	 * @param file
	 *            the specified file
	 * @param dirPath
	 *            the directory path to the file
	 * @return the absolute path.
	 */
	private String getRelativePath(final File file, final String dirPath) {
		String path = file.getAbsolutePath().trim();

		if (path.startsWith(dirPath)) {
			path = path.substring(dirPath.length(), path.length());
		}

		if (path.endsWith(File.separator)) {
			path = path.substring(0, (path.length() - 1));
		}

		return path;
	}
	/**
	 * check whether this is MS Windows OS.
	 * @return true if file separator is retarded (literally).
	 */
	private boolean isWindows() {
		return (File.separatorChar == '\\');
	}
	/**
	 * Check whether the a specified file is a directory and readable.
	 * @param directory the specified directory
	 * @return true if "directory" is a directory and readable.
	 */
	private boolean checkDirectory(final String directory) {
		File file = new File(directory);
		return (file.exists() && file.isDirectory() && file.canRead());
	}
	/**
	 * Method description needed.
	 */
	private void fillDirectoriesWidgetWithRoots() {
		File[] roots = File.listRoots();

		for (int i = 0; i < roots.length; i++) {
			directoryList.add(roots[i].getAbsolutePath());
		}
	}

	/**
	 * Method description needed.
	 * @param directory the specified directory.
	 */
	private void fillListWidgets(final String directory) {
		File directoryFile = new File(directory);

		if (directoryFile.isDirectory()) {
			File[] files =
					directoryFile.listFiles(new FileDialogFileFilter(
							filterFactory.generateFileFilter(filterString)));

			if ((directoryFile.getParentFile() != null) || (isWindows())) {
				directoryList.add("..");
			}

			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					directoryList.add(getRelativePath(files[i], directory));
				} else {
					fileList.add(getRelativePath(files[i], directory));
				}
			}
		} else {
			// Should not happen.
			new Message("Error",
					"An error occurred trying to read the directory\n "
							+ directory, "OK");
		}
	}

	/**
	 * save the chosen file.
	 */
	private void saveResult() {
		fileChoice = fileTextField.getText();
	}

	/**
	 * update the file field.
	 */
	protected final void updateFileField() {
		fileTextField.setText(getCurrentFileContent());
		fileTextField.paint();
	}
	/**
	 * Update the filter field.
	 */
	protected final void updateFilterField() {
		filterTextField.setText(filterString);
		filterTextField.paint();
	}
	/**
	 * update the list of widgets from the current directory.
	 */
	private void updateListWidgets() {
		final boolean withRoots = false;
		updateListWidgets(withRoots);
	}
	/**
	 * Updates the list of widgets either from roots
	 * or from the current directory.
	 * 
	 * @param roots  the roots.
	 */
	private void updateListWidgets(final boolean roots) {
		directoryList.clear();
		fileList.clear();

		if (!roots) {
			fillListWidgets(getCurrentDirectory());
		} else {
			fillDirectoriesWidgetWithRoots();
		}

		directoryList.paint();
		fileList.paint();
	}
}
/**
 * This class implements the FileFilter for the File Dialog. 
 *
 */
class FileDialogFileFilter implements FileFilter {
	/**
	 * 
	 */
	private FileFilter filter = null;

	/**
	 * Constructor.
	 * @param aFilter the specified filter.
	 */
	public FileDialogFileFilter(final FileFilter aFilter) {
		filter = aFilter;
	}
	/**
	 * 
	 * @param file the specified file.
	 * @return true if the file is a directory or the filter
	 * accepts the file.
	 * 
	 */
	public final boolean accept(final File file) {
		return (file.isDirectory()) || (filter.accept(file));
	}
}

/**
 * This class extends TextField.
 *
 */
class FilterTextField extends TextField {
	/**
	 * 
	 */
	private FileDialog dialogParent = null;

	/**
	 * Constructor of the FilterTextField.
	 * @param parent the parent dialog.
	 */
	public FilterTextField(final FileDialog parent) {
		dialogParent = parent;
	}
	/**
	 * remove focus from the filter string.
	 */
	public void unfocus() {
		setText(dialogParent.getFilterString());
		super.unfocus();
	}
}
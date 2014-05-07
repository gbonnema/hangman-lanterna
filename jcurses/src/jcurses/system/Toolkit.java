package jcurses.system;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jcurses.util.Rectangle;
import jcurses.util.TextUtils;

/**
 * This class is the 'work factory' of the jcurses library. It contains methods
 * for primitive input and output operations and is the only interface to
 * platform dependent libraries. A developer must not usually call methods of
 * this class. These methods are used in implementing widgets and in jcurses
 * core.
 * 
 */
public final class Toolkit {

	/**
	 * Default constructor private: no instance permitted.
	 */
	private Toolkit() {
	}

	/**
	 * Effectively an enum.
	 */
	public static final int VERTICAL = 0;
	/**
	 * Effectively an enum.
	 */
	public static final int HORIZONTAL = 1;
	/**
	 * Effectively an enum.
	 */
	public static final short LL_CORNER = 2;
	/**
	 * Effectively an enum.
	 */
	public static final short LR_CORNER = 3;
	/**
	 * Effectively an enum.
	 */
	public static final short UL_CORNER = 4;
	/**
	 * Effectively an enum.
	 */
	public static final short UR_CORNER = 5;
	/**
	 * attributes.
	 */
	private static long[] attributes = { 0, 0, 0 };
	/**
	 * 
	 */
	private static short[] basicColors = { 0, 0, 0, 0, 0, 0, 0, 0 };
	/**
	 * 
	 */
	private static final int NR_COLOR_PAIRS = 8;
	/**
	 * An array of colors stored per colorPair.
	 * 
	 * So colorPairs[1][2] is the color for background color 1 and foreground
	 * color 2.
	 */
	private static short[][] colorPairs =
			new short[NR_COLOR_PAIRS][NR_COLOR_PAIRS];
	/**
	 * clips is a hash from the thread id to an ArrayList of Rectangles.
	 */
	private static Hashtable<Thread, ArrayList<Rectangle>> clips =
			new Hashtable<>();
	/**
	 * 
	 */
	private static short maxColorPairNumber = -1;
	/**
	 * 
	 */
	private static String encoding;
	// private static final String JAR_RESOURCE = "jar:file:";
	// private static final String FILE_RESOURCE = "file:";
	/**
	 * 
	 */
	private static final String LIBRARY_NAME = "jcurses";

	static {
		System.loadLibrary(LIBRARY_NAME);
		// loadLibrary();
		fillBasicColors(basicColors);
		fillAttributes(attributes);
		fillColorPairs();
		initEncoding();
		init();
	}

	/**
	 * The method sets the clipping rectangle for the current thread. All the
	 * output operations, that are performed by this thread after a call of this
	 * method, will paint only within the clip rectangle. If other clips were
	 * set before this, then the used clip rectangle is the intersection of all
	 * clip rectangles set by current thread.
	 * 
	 * 
	 * @param clipRect
	 *            clip rectangle to be set
	 */
	public static void setClipRectangle(final Rectangle clipRect) {
		ArrayList<Rectangle> aClips = clips.get(Thread.currentThread());

		if (aClips == null) {
			aClips = new ArrayList<>();
			clips.put(Thread.currentThread(), aClips);
		}

		aClips.add(clipRect);
	}

	/**
	 * The method sets java encoding for string input and output operations.
	 * 
	 * 
	 * @param anEncoding
	 *            DOCUMENT ME!
	 */
	public static void setEncoding(final String anEncoding) {
		encoding = anEncoding;
	}

	/**
	 * Gets java encoding for string input and output operations.
	 * 
	 * 
	 * @return the java encoding used by sring input and output operations
	 */
	public static String getEncoding() {
		return encoding;
	}

	/**
	 * The method returns the screen height.
	 * 
	 * 
	 * @return the screen height
	 */
	public static synchronized native int getScreenHeight();

	/**
	 * The method returns the screen width.
	 * 
	 * 
	 * @return the screen height
	 */
	public static synchronized native int getScreenWidth();

	/**
	 * The method to make an audio alert. Works only with terminals, that
	 * support 'beeps', under windows currenty does nothing.
	 */
	public static synchronized native void beep();

	/**
	 * The method changes the background and the foreground colors of an given
	 * rectangle on the screen.
	 * 
	 * 
	 * @param aRect
	 *            rectangle, whose colors are to be changed
	 * @param aColor
	 *            new colors
	 */
	public static void changeColors(final Rectangle aRect,
			final CharColor aColor) {

		Rectangle clipRect = getCurrentClipRectangle();
		Rectangle rect = aRect;

		if (clipRect != null) {
			rect = rect.intersection(clipRect);
		}

		if (!rect.isEmpty()) {
			changeColors(rect.getX(), rect.getY(), rect.getWidth(), rect
					.getHeight(), aColor.getPairNo(), aColor.getAttribute());

		}
	}

	/**
	 * The method clears the screen and fills it with the background color of
	 * color.
	 * 
	 * 
	 * @param color
	 *            color the color to fill the screen, only background part is
	 *            used
	 */
	public static void clearScreen(final CharColor color) {
		clearScreen(color.getPairNo(), color.getAttribute());
	}

	/**
	 * The method draws a border ( empty rectangle ).
	 * 
	 * 
	 * @param rect
	 *            bounds of the border to be painted
	 * @param color
	 *            color attributes of the border
	 */
	public static void drawBorder(final Rectangle rect, final CharColor color) {
		drawBorder(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(),
				color);

	}

	/**
	 * The method draws a border on the screen.
	 * 
	 * We break down the logic of line drawing here so we can properly take
	 * advantage of the clipping functionality at this level, rather than using
	 * the underlying curses border routines. We pass anything we can to
	 * underlying primitives however.
	 * 
	 * 
	 * @param aX
	 *            the x coordinate of the top left corner of the border to be
	 *            painted
	 * @param aY
	 *            the y coordinate of the top left corner of the border to be
	 *            painted
	 * @param aWidth
	 *            the width of the border to be painted
	 * @param aHeight
	 *            the height of the border to be painted
	 * @param aColor
	 *            color attributes of the border
	 */
	public static void drawBorder(final int aX, final int aY, final int aWidth,
			final int aHeight, final CharColor aColor) {

		short mPair = aColor.getPairNo();
		long mAttr = aColor.getAttribute();

		drawCornerClip(aX, aY, UL_CORNER, mPair, mAttr);
		drawVLineClip(aX, aY + 1, aY + aHeight - 2, aColor);
		drawHLineClip(aX + 1, aY, aX + aWidth - 2, aColor);
		drawCornerClip(aX + aWidth - 1, aY, UR_CORNER, mPair, mAttr);
		drawVLineClip(aX + aWidth - 1, aY + 1, aY + aHeight - 2, aColor);
		drawCornerClip(aX, aY + aHeight - 1, LL_CORNER, mPair, mAttr);
		drawHLineClip(aX + 1, aY + aHeight - 1, aX + aWidth - 1, aColor);
		drawCornerClip(aX + aWidth - 1, aY + aHeight - 1, LR_CORNER, mPair,
				mAttr);

	}

	/**
	 * The method draws a corner that is constrained within a rectangle.
	 * 
	 * @param aX
	 *            the x coordinate of the top left corner of the clip to be
	 *            painted
	 * @param aY
	 *            the y coordinate of the top left corner of the clip to be
	 *            painted
	 * @param aPos
	 *            Position enum (UL,LL,UR,LR)
	 * @param mPair
	 *            Color pair number
	 * @param mAttr
	 *            Attribute
	 */
	private static void drawCornerClip(final int aX, final int aY,
			final int aPos, final short mPair, final long mAttr) {

		Rectangle mClip = getCurrentClipRectangle();
		/* formatter:off */
		if (mClip == null
				|| (isBetween(aX, mClip.getLeft(), mClip.getRight()) 
				&& isBetween(aY, mClip.getTop(), mClip.getBottom()))) {
		

			drawCorner(aX, aY, aPos, mPair, mAttr);
		}
		/* formatter:on */
	}

	/**
	 * The method draws a corner.
	 * 
	 * @param aX
	 *            the x coordinate of the top left corner of the clip to be
	 *            painted
	 * @param aY
	 *            the y coordinate of the top left corner of the clip to be
	 *            painted
	 * @param aPos
	 *            Position enum (UL,LL,UR,LR)
	 * @param colorPairNumber
	 *            Color pair number
	 * @param attr
	 *            Attribute
	 */
	private static synchronized native void drawCorner(final int aX,
			final int aY, final int aPos, final short colorPairNumber,
			final long attr);

	/**
	 * The method draws a horizontal line.
	 * 
	 * 
	 * @param aStartX
	 *            the x coordinate of the start point
	 * @param aStartY
	 *            the y coordinate of the start point
	 * @param anEndX
	 *            the x coordinate of the end point
	 * @param aColor
	 *            DOCUMENT ME!
	 */
	public static void drawHLineClip(final int aStartX, final int aStartY,
			final int anEndX, final CharColor aColor) {

		int startX = aStartX;
		int startY = aStartY;
		int endX = anEndX;

		CharColor color = aColor;

		Rectangle mClip = getCurrentClipRectangle();

		if (mClip != null) {
			startX = Math.max(startX, mClip.getLeft());
			endX = Math.min(endX, mClip.getRight());
		}

		if (mClip == null
				|| (isBetween(startY, mClip.getTop(), mClip.getBottom())
						&& startX <= mClip.getRight() && endX >= mClip
						.getLeft())) {
			drawHorizontalLine(startX, startY, endX, color.getPairNo(), color
					.getAttribute());
		}
	}

	/**
	 * The method draws a horizontal thick line.
	 * 
	 * 
	 * @param aStartX
	 *            the x coordinate of the start point
	 * @param aStartY
	 *            the y coordinate of the start point
	 * @param anEndX
	 *            the x coordinate of the end point
	 * @param aColor
	 *            DOCUMENT ME!
	 */
	public static void drawHorizontalThickLine(final int aStartX,
			final int aStartY, final int anEndX, final CharColor aColor) {

		int startX = aStartX;
		int startY = aStartY;
		int endX = anEndX;

		CharColor color = aColor;

		Rectangle mClip = getCurrentClipRectangle();

		if (mClip != null) {
			startX = Math.max(startX, mClip.getLeft());
			endX = Math.min(endX, mClip.getRight());
		}

		if (mClip == null
				|| (isBetween(startY, mClip.getTop(), mClip.getBottom())
						&& startX <= mClip.getRight() && endX >= mClip
						.getLeft())) {

			drawHorizontalThickLine(startX, startY, endX, color.getPairNo(),
					color.getAttribute());

		}
	}

	/**
	 * The method draws a rectangle on the screen, filled with background part
	 * of <code>color</code>.
	 * 
	 * 
	 * @param aRect
	 *            rectangle ( that is, bounds of rectangle) to be painted
	 * @param aColor
	 *            color to fill the rectangle, only background part is used
	 */
	public static void drawRectangle(final Rectangle aRect,
			final CharColor aColor) {
		drawRectangle(aRect.getX(), aRect.getY(), aRect.getWidth(), aRect
				.getHeight(), aColor);

	}

	/**
	 * The method draws a rectangle on the screen, filled with background part
	 * of <code>color</code>.
	 * 
	 * @param aX
	 *            the x coordinate of the top left corner of the rectangle to be
	 *            painted
	 * @param aY
	 *            the y coordinate of the top left corner of the rectangle to be
	 *            painted
	 * @param aWide
	 *            the width of the rectangle to be painted
	 * @param aHigh
	 *            the height of the rectangle to be painted
	 * @param aColor
	 *            color to fill the rectangle, only background part is used
	 */
	public static void drawRectangle(final int aX, final int aY,
			final int aWide, final int aHigh, final CharColor aColor) {
		drawRectangle(aX, aY, aWide, aHigh, getCurrentClipRectangle(), aColor);
	}

	/**
	 * The method draws a rectangle on the screen constrained within a clipping
	 * rectangle and filled with background part of <code>color</code>.
	 * 
	 * @param aX
	 *            the x coordinate of the top left corner of the rectangle to be
	 *            painted
	 * @param aY
	 *            the y coordinate of the top left corner of the rectangle to be
	 *            painted
	 * @param aWide
	 *            the width of the rectangle to be painted
	 * @param aHigh
	 *            the height of the rectangle to be painted
	 * @param aClip
	 *            clipping rect
	 * @param aColor
	 *            color to fill the rectangle, only background part is used
	 */
	public static void drawRectangle(final int aX, final int aY,
			final int aWide, final int aHigh, final Rectangle aClip,
			final CharColor aColor) {

		if (aClip == null) {
			drawRectangle(aX, aY, aWide, aHigh, aColor.getPairNo(), aColor
					.getAttribute());

		} else {
			int mX = Math.max(aX, aClip.getLeft());
			int mY = Math.max(aY, aClip.getTop());
			int mWide = Math.min(aWide, aClip.getRight() - mX + 1);
			int mHigh = Math.min(aHigh, aClip.getBottom() - mY + 1);

			if (mWide > 0 && mHigh > 0) {
				drawRectangle(mX, mY, mWide, mHigh, aColor.getPairNo(), aColor
						.getAttribute());

			}
		}
	}

	/**
	 * The method draws a vertical line.
	 * 
	 * @param aStartX
	 *            the x coordinate of the start point
	 * @param aStartY
	 *            the y coordinate of the start point
	 * @param anEndY
	 *            the y coordinate of the end point
	 * @param aColor
	 *            color to draw line
	 */
	public static void drawVLineClip(final int aStartX, final int aStartY,
			final int anEndY, final CharColor aColor) {

		int startX = aStartX;
		int startY = aStartY;
		int endY = anEndY;

		CharColor color = aColor;
		Rectangle mClip = getCurrentClipRectangle();

		if (mClip != null) {
			startY = Math.max(startY, mClip.getTop());
			endY = Math.min(endY, mClip.getBottom());
		}

		if (mClip == null
				|| (isBetween(startX, mClip.getLeft(), mClip.getRight())
						&& startY <= mClip.getBottom() && endY >= mClip
						.getTop())) {

			drawVerticalLine(startX, startY, endY, color.getPairNo(), color
					.getAttribute());

		}
	}

	/**
	 * The method draws a vertical thick line.
	 * 
	 * 
	 * @param aStartX
	 *            the x coordinate of the start point
	 * @param aStartY
	 *            the y coordinate of the start point
	 * @param anEndY
	 *            the y coordinate of the end point
	 * @param aColor
	 *            color to draw line
	 */
	public static void drawVerticalThickLine(final int aStartX,
			final int aStartY, final int anEndY, final CharColor aColor) {

		int startX = aStartX;
		int startY = aStartY;
		int endY = anEndY;

		CharColor color = aColor;
		Rectangle mClip = getCurrentClipRectangle();

		if (mClip != null) {
			startY = Math.max(startY, mClip.getTop());
			endY = Math.min(endY, mClip.getBottom());
		}

		if (mClip == null
				|| (isBetween(startX, mClip.getLeft(), mClip.getRight())
						&& startY <= mClip.getBottom() && endY >= mClip
						.getTop())) {
			drawVerticalThickLine(startX, startY, endY, color.getPairNo(),
					color.getAttribute());
		}
	}

	/**
	 * The method ends a new painting action, containing possible many painting
	 * operations The call of this method must already follow a call of
	 * <code>startPainting</code>.
	 */
	public static synchronized native void endPainting();

	/**
	 * The method tells whether a terminal is color-capable.
	 * 
	 * 
	 * @return <code>true</code> if the terminal can do color painting,
	 *         <code>false</code> otherwise.
	 */
	public static boolean hasColors() {
		return (hasColorsAsInteger() != 0);
	}

	/**
	 * The method initializes the jcurses library, must be called only one time
	 * before all painting and input operations.
	 */
	public static synchronized native void init();

	/**
	 * The method prints a string on the screen.
	 * 
	 * 
	 * @param aText
	 *            string to be printed
	 * @param aTextBox
	 *            the rectangle, within which the string must lie. If the string
	 *            doesn't fit within the rectangle it will be broken.
	 * @param aColor
	 *            attributes of the string
	 */
	public static void printString(final String aText,
			final Rectangle aTextBox, final CharColor aColor) {

		printString(aText, aTextBox.getLeft(), aTextBox.getTop(), aTextBox
				.getWidth(), aTextBox.getHeight(), aColor);

	}

	/**
	 * The method prints a string on the screen. If the string doesn't fit
	 * within the rectangle bounds, it wiil be broken.
	 * 
	 * 
	 * @param aText
	 *            string to be printed
	 * @param aX
	 *            the x coordinate of the string start point
	 * @param aY
	 *            the y coordinate of the string start point
	 * @param aWide
	 *            the width of bounds rectangle
	 * @param aHigh
	 *            the width of bounds rectangle
	 * @param aColor
	 *            color attributes of the string
	 */
	public static void printString(final String aText, final int aX,
			final int aY, final int aWide, final int aHigh,
			final CharColor aColor) {

		printString(aText, aX, aY, aWide, aHigh, getCurrentClipRectangle(),
				aColor);

	}

	/**
	 * Description of the Method
	 * 
	 * The method prints a string on the screen constrained within a clipping
	 * rectangle. If the string doesn't fit within the rectangle bounds, it wiil
	 * be line-broken.
	 * 
	 * 
	 * @param aText
	 *            string to be printed
	 * @param aX
	 *            the x coordinate of the string start point
	 * @param aY
	 *            the y coordinate of the string start point
	 * @param aWide
	 *            the width of bounds rectangle
	 * @param aHigh
	 *            the width of bounds rectangle
	 * @param aClip
	 *            clipping rectangle
	 * @param aColor
	 *            color attributes of the string
	 */
	public static void printString(final String aText, final int aX,
			final int aY, final int aWide, final int aHigh,
			final Rectangle aClip, final CharColor aColor) {

		int mX = aX;
		int mY = aY;
		if (aClip != null) {
			mX = Math.max(aX, aClip.getLeft());
			mY = Math.max(aY, aClip.getTop());
		}

		List<String> mLines = TextUtils.breakLines(aText, aWide);
		/* clip this many lines at the top */
		int mFirstLine = mY - aY;

		int mWide = aWide;
		int mHigh = aHigh;
		if (aClip != null) {
			mWide = Math.min(aWide, aClip.getRight() - mX + 1);
			mHigh = Math.min(aHigh, aClip.getBottom() - mY + 1);
		}

		// adjust height of box to max number of lines
		mHigh = Math.min(mHigh, mLines.size() - mFirstLine);

		if (mWide > 0 && mHigh > 0) {
			// clip this many leftmost characters per line
			int mOffset = mX - aX;
			for (int mIdx = 0; mIdx < mHigh; mIdx++) {
				String mLine = (String) mLines.get(mIdx + mFirstLine);
				if (mLine.length() > mOffset) {
					printStringNoClip(mLine.substring(mOffset), mX, mY + mIdx,
							mWide, 1, aColor);

				}
			}
		}
	}

	/**
	 * The method prints a string on the screen.
	 * 
	 * 
	 * @param text
	 *            string to be printed
	 * @param x
	 *            the x coordinate of the string start point
	 * @param y
	 *            the y coordinate of the string start point
	 * @param color
	 *            color attributes of the string
	 */
	public static void printString(final String text, final int x, final int y,
			final CharColor color) {
		printString(text, x, y, text.length(), 1, color);
	}

	/**
	 * Target for character read synchronizing.
	 */
	private static Integer readSync = new Integer(0);

	/**
	 * The method reads the next code (ASCII or control ) from an input stream
	 * and wraps it into an instance of {@link jcurses.system.InputChar}
	 * 
	 * This method is synchronized on readSync.
	 * 
	 * 
	 * @return the next read code
	 */
	public static InputChar readCharacter() {
		synchronized (readSync) {
			int mChar = readByte();

			final int escape = 0x1b;
			final int addition2EscapedChar = 1000;

			// handle escape sequences
			if (mChar == escape) {
				mChar = readByte();
				if (mChar == -1) {
					mChar = escape;
				} else {
					mChar += addition2EscapedChar;
				}
			}

			if (mChar == -1) {
				return null;
			}

			return new InputChar(mChar);
		}

	}

	/**
	 * The method shuts down the jcurses library and recovers the terminal to
	 * the state before jcurses application start.
	 */
	public static synchronized native void shutdown();

	/**
	 * The method starts a new painting action, containing possible many
	 * painting operations After a call of this method endPainting must be
	 * already called, to refresh the screen.
	 */
	public static synchronized native void startPainting();

	/**
	 * Removes the previously set clip rectangle.
	 */
	public static void unsetClipRectangle() {
		ArrayList<Rectangle> aClips = clips.get(Thread.currentThread());

		if (aClips == null) {
			return;
		}

		if (aClips.size() > 0) {
			aClips.remove(clips.size() - 1);
		}

		if (aClips.size() == 0) {
			clips.remove(Thread.currentThread());
		}
	}

	/**
	 * Gets the basicColors attribute of the Toolkit class.
	 * 
	 * @return The basicColors value
	 */
	static short[] getBasicColors() {
		return basicColors;
	}

	/**
	 * Gets the specialKeyCode attribute of the Toolkit class.
	 * 
	 * @param code
	 *            Description of the Parameter
	 * @return The specialKeyCode value
	 */
	static synchronized native int getSpecialKeyCode(final int code);

	/**
	 * Gets the colorPairNo attribute of the Toolkit class.
	 * 
	 * @param aColor
	 *            Description of the Parameter
	 * @return The colorPairNo value
	 */
	static short getColorPairNo(final CharColor aColor) {
		short number =
				colorPairs[aColor.getBackground()][aColor.getForeground()];
		if (number == -1) {
			number = ++maxColorPairNumber;
			colorPairs[aColor.getBackground()][aColor.getForeground()] = number;
			initColorPair(mapBasicColor(aColor.getBackground()),
					mapBasicColor(aColor.getForeground()), number);

			// System.err.println("NEW COLOR: [" + aColor.toString() + "] = [" +
			// number + "] {" + mapBasicColor(aColor.getBackground()) + "} {" +
			// mapBasicColor(aColor.getForeground()) + "}");
		}

		// System.err.println("USE COLOR: [" + aColor.toString() + "] = [" +
		// number + "]");

		return number;
	}

	/**
	 * Description of the Method.
	 * 
	 * @param aColor
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	static short mapBasicColor(final short aColor) {
		return basicColors[aColor];
	}

	/**
	 * Description of the Method.
	 * 
	 * @param aAttr
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	static long mapAttribute(final short aAttr) {
		return attributes[aAttr];
	}

	/**
	 * Gets the currentClipRectangle attribute of the Toolkit class.
	 * 
	 * @return The currentClipRectangle value
	 */
	private static Rectangle getCurrentClipRectangle() {
		ArrayList<Rectangle> aClips = clips.get(Thread.currentThread());

		if ((aClips == null) || (aClips.size() == 0)) {
			return null;
		}

		Rectangle result = aClips.get(0);

		for (int i = 1; i < aClips.size(); i++) {
			Rectangle temp = aClips.get(i);
			result = result.intersection(temp);

			if (result.isEmpty()) {
				return result;
			}
		}

		return result;
	}

	/**
	 * Gets the windows attribute of the Toolkit class.
	 * 
	 * @return The windows value
	 */
	private static boolean isWindows() {
		return (java.io.File.separatorChar == '\\');
	}

	/**
	 * Gets the between attribute of the Toolkit class.
	 * 
	 * @param aValue
	 *            Description of the Parameter
	 * @param aStart
	 *            Description of the Parameter
	 * @param aEnd
	 *            Description of the Parameter
	 * @return The between value
	 */
	private static boolean isBetween(final int aValue, final int aStart,
			final int aEnd) {

		int start = aStart;
		int end = aEnd;
		int value = aValue;

		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		return ((start <= value) && (end >= value));
	}

	/**
	 * Description of the Method.
	 * 
	 * @param x
	 *            Description of the Parameter
	 * @param y
	 *            Description of the Parameter
	 * @param width
	 *            Description of the Parameter
	 * @param height
	 *            Description of the Parameter
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attr
	 *            Description of the Parameter
	 */
	private static synchronized native void changeColors(final int x,
			final int y, final int width, final int height,
			final short colorPairNumber, final long attr);

	/**
	 * Description of the Method.
	 * 
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attrs
	 *            Description of the Parameter
	 */
	private static synchronized native void clearScreen(
			final short colorPairNumber, final long attrs);

	// private static native int computeChtype(short number);

	/**
	 * Description of the Method.
	 * 
	 * @param startX
	 *            Description of the Parameter
	 * @param startY
	 *            Description of the Parameter
	 * @param endY
	 *            Description of the Parameter
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attr
	 *            Description of the Parameter
	 */
	private static synchronized native void drawHorizontalLine(
			final int startX, final int startY, final int endY,
			final short colorPairNumber, final long attr);

	/**
	 * Description of the Method.
	 * 
	 * @param startX
	 *            Description of the Parameter
	 * @param startY
	 *            Description of the Parameter
	 * @param endX
	 *            Description of the Parameter
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attr
	 *            Description of the Parameter
	 */
	private static synchronized native void drawHorizontalThickLine(
			final int startX, final int startY, final int endX,
			final short colorPairNumber, final long attr);

	/**
	 * Description of the Method.
	 * 
	 * @param x
	 *            Description of the Parameter
	 * @param y
	 *            Description of the Parameter
	 * @param width
	 *            Description of the Parameter
	 * @param height
	 *            Description of the Parameter
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attribute
	 *            Description of the Parameter
	 */
	private static synchronized native void drawRectangle(final int x,
			final int y, final int width, final int height,
			final short colorPairNumber, final long attribute);

	/**
	 * Description of the Method.
	 * 
	 * @param startX
	 *            Description of the Parameter
	 * @param startY
	 *            Description of the Parameter
	 * @param endX
	 *            Description of the Parameter
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attr
	 *            Description of the Parameter
	 */
	private static synchronized native void drawVerticalLine(final int startX,
			final int startY, final int endX, final short colorPairNumber,
			final long attr);

	/**
	 * Description of the Method.
	 * 
	 * @param startX
	 *            Description of the Parameter
	 * @param startY
	 *            Description of the Parameter
	 * @param endY
	 *            Description of the Parameter
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attr
	 *            Description of the Parameter
	 */
	private static synchronized native void drawVerticalThickLine(
			final int startX, final int startY, final int endY,
			final short colorPairNumber, final long attr);

	/**
	 * fills attributes with NORMAL, REVERSE and BOLD (C constants).
	 * 
	 * @param attrs
	 *            Description of the Parameter
	 */
	private static synchronized native void fillAttributes(final long[] attrs);

	/**
	 * Description of the Method.
	 * 
	 * @param someBasicColors
	 *            Description of the Parameter
	 */
	private static synchronized native void fillBasicColors(
			final short[] someBasicColors);

	/**
	 * Initialize the colorPairs entries to -1.
	 */
	private static void fillColorPairs() {
		for (int i = 0; i < NR_COLOR_PAIRS; i++) {
			for (int j = 0; j < NR_COLOR_PAIRS; j++) {
				colorPairs[i][j] = -1;
			}
		}
	}

	/**
	 * Description of the Method.
	 * 
	 * @return Description of the Return Value
	 */
	private static synchronized native int hasColorsAsInteger();

	/**
	 * Description of the Method.
	 * 
	 * @param aNumber
	 *            Description of the Parameter
	 * @param aRed
	 *            Description of the Parameter
	 * @param aGreen
	 *            Description of the Parameter
	 * @param aBlue
	 *            Description of the Parameter
	 */
	public static void adjustBaseColor(final short aNumber, final int aRed,
			final int aGreen, final int aBlue) {

		adjustColor(basicColors[aNumber], (short) aRed, (short) aGreen,
				(short) aBlue);

	}

	/**
	 * Description of the Method.
	 * 
	 * @param aNumber
	 *            Description of the Parameter
	 * @param aRed
	 *            Description of the Parameter
	 * @param aGreen
	 *            Description of the Parameter
	 * @param aBlue
	 *            Description of the Parameter
	 */
	private static synchronized native void adjustColor(final short aNumber,
			final short aRed, final short aGreen, final short aBlue);

	/**
	 * calls the curses init_pair(number+1, foreground, background) to initilize
	 * the color pair.
	 * 
	 * @param background
	 *            Description of the Parameter
	 * @param foreground
	 *            Description of the Parameter
	 * @param number
	 *            Description of the Parameter
	 */
	private static synchronized native void initColorPair(
			final short background, final short foreground, final short number);

	/**
	 * Description of the Method.
	 */
	private static void initEncoding() {
		if (isWindows()) {
			setEncoding("CP850");
		}
	}

	/**
	 * Description of the Method.
	 * 
	 * @param chars
	 *            Description of the Parameter
	 * @param x
	 *            Description of the Parameter
	 * @param y
	 *            Description of the Parameter
	 * @param width
	 *            Description of the Parameter
	 * @param height
	 *            Description of the Parameter
	 * @param colorPairNumber
	 *            Description of the Parameter
	 * @param attr
	 *            Description of the Parameter
	 */
	private static synchronized native void printString(final byte[] chars,
			final int x, final int y, final int width, final int height,
			final short colorPairNumber, final long attr);

	/**
	 * Description of the Method.
	 * 
	 * @param aText
	 *            Description of the Parameter
	 * @param aX
	 *            Description of the Parameter
	 * @param aY
	 *            Description of the Parameter
	 * @param aWidth
	 *            Description of the Parameter
	 * @param aHeight
	 *            Description of the Parameter
	 * @param aColor
	 *            Description of the Parameter
	 */
	private static void printStringNoClip(final String aText, final int aX,
			final int aY, final int aWidth, final int aHeight,
			final CharColor aColor) {

		printString(encodeChars(aText), aX, aY, aWidth, aHeight, aColor
				.getPairNo(), aColor.getAttribute());

	}

	/**
	 * Description of the Method.
	 * 
	 * @param aText
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	private static byte[] encodeChars(final String aText) {
		try {
			if (encoding != null) {
				return aText.getBytes(encoding);
			}
		} catch (UnsupportedEncodingException e) {
			encoding = null;
		}
		return aText.getBytes();
	}

	/**
	 * Description of the Method.
	 * 
	 * @return Description of the Return Value
	 */
	private static native int readByte();

	/**
	 * Gets the screen attribute of the Toolkit class.
	 * 
	 * @return The screen value
	 */
	public static Rectangle getScreen() {
		return new Rectangle(0, 0, getScreenWidth(), getScreenHeight());
	}
}
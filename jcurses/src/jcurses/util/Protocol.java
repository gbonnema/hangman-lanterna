/**
 * For Logging during a debugging session of a jcurses application.
 */
package jcurses.util;

import java.io.FileOutputStream;
import java.io.PrintStream;

import java.util.Calendar;
import java.util.HashSet;

/**
 * 
 * <h3>Protocol</h3>
 * 
 * <p>
 * This class implements the debugging for jcurses applications. Developing a
 * jcurses application you can't use <code>System.out.println</code> calls,
 * because the console is used for painting. Instead of this you have to use
 * <code>system</code> or <code>debug</code> methods of this class. These made
 * nothing, if the system property "jcurses.protocol.filename" isn't defined, if
 * these is defined, log messages are written to the file, whose name is defined
 * in this property.
 * </p>
 * 
 * <p>
 * There are two standard debug channels <code>SYSTEM</code> and <DEBUG> for
 * <code>system</code> and <code>debug</code> methods respectively. You can
 * define your own channels. To activate a channel the method activateChannnel
 * must be used with cnannels'name as argument. Thereafter this name are to use
 * as second argument in <code>log</code> calls, to write messages to the
 * channel.
 * </p>
 * 
 */
public final class Protocol {

	/**
	 * Contains the system property name for the logging filename.
	 * This property had better be set correctly or logging
	 * will not work and throw an exception when it tries
	 * to open the logging file.
	 * 
	 */
	public static final String SYSTEM_PROPERTY_NAME =
			"jcurses.protocol.filename";

	/**
	 * The name of the standard <code>DEBUG</code> channel.
	 */
	public static final String DEBUG = "debug";

	/**
	 * The name of the standard <code>SYSTEM</code> channel.
	 */
	public static final String SYSTEM = "system";

	/**
	 * activated channels. TODO: why was this a raw type? changed to String:
	 * problem?
	 */
	private static HashSet<String> activatedChannels = new HashSet<>();
	/**
	 * the print stream for logging.
	 */
	private static PrintStream logStream = null;

	static {
		initLogStreamIfPossible();
		initLogging();
	}

	/**
	 * The constructor is private to prevent instantiation.
	 */
	private Protocol() {
	}

	/**
	 * The method activates a channel with given name.
	 * 
	 * @param channel
	 *            the name of the channel to activate
	 */
	public static void activateChannel(final String channel) {
		activatedChannels.add(channel);
	}

	/**
	 * Writes a message to <code>DEBUG</code> channel.
	 * 
	 * @param message the message to be logged.
	 */
	public static void debug(final String message) {
		log(message, DEBUG);
	}

	/**
	 * The method writes a log message.
	 * 
	 * @param message
	 *            the messsage's text
	 * @param channel
	 *            name of the channel to write on.
	 */
	public static void log(final String message, final String channel) {
		if (isLoggingActivated() && isChannelActivated(channel)) {
			String outputMessage = getPrefix(channel) + " " + message;
			logStream.println(outputMessage);
			logStream.flush();
		}
	}

	/**
	 * Writes a message to <code>SYSTEM</code> channel.
	 * 
	 * @param message the message to be logged.
	 */
	public static void system(final String message) {
		log(message, SYSTEM);
	}
	/**
	 * Return whether channel is activated.
	 * @param channel the channel
	 * @return true if the channel is activated.
	 */
	private static boolean isChannelActivated(final String channel) {
		return true;
	}
	/**
	 * Return whether logging is activated.
	 * @return true if logging is activated.
	 */
	private static boolean isLoggingActivated() {
		if (logStream == null) {
			initLogStreamIfPossible();
		}

		return logStream != null;
	}
	/**
	 * Prefix for the log messages.
	 * @param channel the channel.
	 * @return prefix for the log messages.
	 */
	private static String getPrefix(final String channel) {
		Calendar cal = Calendar.getInstance();
		String prefix = "[" + cal.get(Calendar.DATE) + "."
				+ (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.YEAR)
				+ "  " + cal.get(Calendar.HOUR) + ":"
				+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
				+ "  channel=" + channel + "]";
		return prefix;
	}
	/**
	 * Initializes the log stream.
	 */
	private static void initLogStreamIfPossible() {
		try {
			String fileName = System.getProperty(SYSTEM_PROPERTY_NAME);

			if (fileName != null) {
				logStream = new PrintStream(new FileOutputStream(fileName,
						true));
				System.setErr(logStream);
			}
		} catch (Exception e) {
			// Etwas nicht geklappt. Also kein Logging
			/* TODO: test exception: not sure this will be visible */
			System.err.println(e.getStackTrace());
		}
	}
	/**
	 * Initialize logging.
	 */
	private static void initLogging() {
		activateChannel(SYSTEM);
	}
}

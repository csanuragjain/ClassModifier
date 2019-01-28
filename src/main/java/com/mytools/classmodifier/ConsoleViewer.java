package com.mytools.classmodifier;

/**
 * Helps make changes to console in GUI
 * @author csanuragjain
 * https://cooltrickshome.blogspot.com
 */
public class ConsoleViewer {

	/**
	 * Clears the console
	 */
	public static void cleanConsole() {
		ClassModifier.consoleArea.setText("");
	}

	/**
	 * Writes on console
	 * 
	 * @param text
	 *            Text to be written
	 */
	public static void setText(String text) {
		ClassModifier.consoleArea.append(text + "\n");
		ClassModifier.consoleArea.setCaretPosition(ClassModifier.consoleArea
				.getDocument().getLength());
	}

}

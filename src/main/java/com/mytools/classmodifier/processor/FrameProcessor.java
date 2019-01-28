package com.mytools.classmodifier.processor;

import java.awt.Font;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public class FrameProcessor {

	public static RSyntaxTextArea getHtmlPane() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
		textArea.setFont(font);
		return textArea;
	}
}

package com.mytools.classmodifier.processor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.mytools.classmodifier.ClassModifier;

public class FontProcessor {

	/**
	 * @param args
	 */
	public static void addFontAction() {
		// TODO Auto-generated method stub
		ClassModifier.incFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					int chosenIndex = TabsProcessor.getCurrentIndex();
					RTextScrollPane htmlScroll = TabsProcessor
							.getComponent(chosenIndex);
					RSyntaxTextArea htmlPane = (RSyntaxTextArea) htmlScroll
							.getViewport().getView();
					Font font = new Font(Font.MONOSPACED, Font.PLAIN, htmlPane
							.getFont().getSize() + 1);
					htmlPane.setFont(font);
				} catch (Exception e) {

				}
			}
		});

		ClassModifier.decFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					int chosenIndex = TabsProcessor.getCurrentIndex();
					RTextScrollPane htmlScroll = TabsProcessor
							.getComponent(chosenIndex);
					RSyntaxTextArea htmlPane = (RSyntaxTextArea) htmlScroll
							.getViewport().getView();
					Font font = new Font(Font.MONOSPACED, Font.PLAIN, htmlPane
							.getFont().getSize() - 1);
					htmlPane.setFont(font);
				} catch (Exception e) {

				}
			}
		});

	}

}

package com.mytools.classmodifier.processor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.Utility;

public class ConverterProcessor {

	/**
	 * @param args
	 */
	public static void addSampleConverter() {
		ClassModifier.converter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					FileUtils.copyFile(
							new File(Utility.getPractiseFile()),
							new File(Utility
									.getRoughFilePath(Utility.practiseFile)));
					Utility.openFile(
							Utility.getRoughFilePath(Utility.practiseFile),
							".java");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Exception occured."
							+ e.getMessage());
				}
			}
		});

		ClassModifier.converterSmali.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					FileUtils.copyFile(
							new File(Utility.getPractiseSmaliFile()),
							new File(
									Utility.getRoughFilePath(Utility.practiseSmaliFile)));
					Utility.openFile(
							Utility.getRoughFilePath(Utility.practiseSmaliFile),
							".smali");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Exception occured."
							+ e.getMessage());
				}
			}
		});

	}

}

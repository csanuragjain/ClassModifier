package com.mytools.classmodifier.processor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.Utility;

public class ToolBarProcessor {

	/**
	 * @param args
	 */
	public static void addToolBarAction() {

		ClassModifier.findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Utility.searchText("find", ClassModifier.regexCB.isSelected(),
						ClassModifier.matchCaseCB.isSelected(),
						ClassModifier.wholeWord.isSelected(),
						ClassModifier.markAll.isSelected(),
						ClassModifier.forSearch.isSelected());
			}
		});
		ClassModifier.toolBar.add(ClassModifier.findButton);
		ClassModifier.findClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				TreeProcessor.searchClass(ClassModifier.searchField.getText());
			}
		});
		ClassModifier.toolBar.add(ClassModifier.findClass);
		ClassModifier.searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClassModifier.findButton.doClick(0);
			}
		});
		ClassModifier.repWithButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Utility.searchText("replace",
						ClassModifier.regexCB.isSelected(),
						ClassModifier.matchCaseCB.isSelected(),
						ClassModifier.wholeWord.isSelected(),
						ClassModifier.markAll.isSelected(),
						ClassModifier.forSearch.isSelected());
			}
		});
		ClassModifier.toolBar.add(ClassModifier.repWithButton);
		ClassModifier.repAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Utility.searchText("replaceAll",
						ClassModifier.regexCB.isSelected(),
						ClassModifier.matchCaseCB.isSelected(),
						ClassModifier.wholeWord.isSelected(),
						ClassModifier.markAll.isSelected(),
						ClassModifier.forSearch.isSelected());
			}
		});

	}

}

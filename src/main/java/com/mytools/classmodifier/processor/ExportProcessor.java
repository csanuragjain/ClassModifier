package com.mytools.classmodifier.processor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.Utility;

public class ExportProcessor {

	/**
	 * @param args
	 */
	public static void addExportAction() {

		ClassModifier.export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File(Utility.getBasePath()));
					chooser.setDialogTitle("Choose Export Path");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int choice = chooser.showOpenDialog(null);
					if (choice != JFileChooser.APPROVE_OPTION)
						return;
					File chosenFile = chooser.getSelectedFile();
					File exportPath = new File(chosenFile.getAbsolutePath());
					FileUtils.copyDirectory(
							new File(Utility.getModifiedClass()), exportPath);
					JOptionPane.showMessageDialog(null, "Export Completed.");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Exception while exporting.");
				}
			}
		});

	}

}

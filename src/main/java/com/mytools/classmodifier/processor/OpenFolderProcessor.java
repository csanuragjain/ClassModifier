package com.mytools.classmodifier.processor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.ConsoleViewer;
import com.mytools.classmodifier.Utility;

public class OpenFolderProcessor {

	public static void openFolderAction() {
		ClassModifier.openProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						try {
							ConsoleViewer.cleanConsole();
							JFileChooser chooser = new JFileChooser();
							chooser.setCurrentDirectory(new File(Utility
									.getBasePath()));
							chooser.setDialogTitle("Open Class Modifier Project");
							chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							int choice = chooser.showOpenDialog(null);
							if (choice != JFileChooser.APPROVE_OPTION)
								return null;
							File chosenFile = chooser.getSelectedFile();
							Utility.className = chosenFile.getName();
							TabsProcessor.clearAllTabs();
							ClassModifier.fileRoot = new File(Utility
									.getToBeProcessedFolder());
							new TreeProcessor()
									.intializeTree(ClassModifier.fileRoot);
							ClassModifier.splitPane.setDividerLocation(0.15);
							JScrollPane treeView = new JScrollPane(
									TreeProcessor.tree);
							ClassModifier.splitPane.setLeftComponent(treeView);
							treeView.repaint();
							ClassModifier.splitPane.repaint();
							ClassModifier.splitPane.setDividerLocation(0.15);
							ClassModifier.frame.setTitle(ClassModifier.title
									+ " - " + Utility.className);
							Utility.openFile(Utility.getToBeProcessedFolder(),
									".java");
							Utility.openFile(Utility.getToBeProcessedFolder(),
									".smali");
						} catch (Exception e) {
							JOptionPane.showMessageDialog(
									null,
									"Issue while opening project "
											+ e.getMessage());
						}
						return null;
					}

				};
				worker.execute();
				/*
				 * Thread t = new Thread() { public void run() {
				 * 
				 * } }; t.start();
				 */
			}
		});
	}

}

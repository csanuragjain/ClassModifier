package com.mytools.classmodifier.processor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.ConsoleViewer;
import com.mytools.classmodifier.Utility;

public class OpenFileProcessor {

	public static void openFileAction() {
		ClassModifier.open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						try {
							ConsoleViewer.cleanConsole();
							FileNameExtensionFilter filter = new FileNameExtensionFilter(
									"Class Files", "class");
							JFileChooser chooser = new JFileChooser();
							chooser.setFileFilter(filter);
							chooser.setDialogTitle("Open Class File");
							chooser.setCurrentDirectory(new File(Utility
									.getBasePath()));
							chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
							int choice = chooser.showOpenDialog(null);
							if (choice != JFileChooser.APPROVE_OPTION)
								return null;
							File chosenFile = chooser.getSelectedFile();
							if (!chosenFile.getName().contains(".class")) {
								JOptionPane
										.showMessageDialog(null,
												"Invalid file type. Please provide class file");
							}
							TabsProcessor.clearAllTabs();
							Utility.className = chosenFile.getName();
							Utility.prepareProject();
							FileUtils.copyFile(
									chosenFile,
									new File(Utility.getClassFile()
											+ File.separator
											+ chosenFile.getName()));
							Utility.processClassFiles(new File(Utility
									.getClassFile()
									+ File.separator
									+ chosenFile.getName()));
							ClassModifier.fileRoot = new File(Utility
									.getToBeProcessedFolder());
							new TreeProcessor()
									.intializeTree(ClassModifier.fileRoot);
							JScrollPane treeView = new JScrollPane(
									TreeProcessor.tree);
							ClassModifier.splitPane.setDividerLocation(0.15);
							ClassModifier.splitPane.setLeftComponent(treeView);
							treeView.repaint();
							ClassModifier.splitPane.repaint();
							ClassModifier.splitPane.setDividerLocation(0.15);
							JOptionPane.showMessageDialog(null,
									"Project Created");
							Utility.openFile(Utility.getToBeProcessedFolder(),
									".java");
							Utility.openFile(Utility.getToBeProcessedFolder(),
									".smali");
							ClassModifier.frame.setTitle(ClassModifier.title
									+ " - " + Utility.className);
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null,
									"Something went wrong...");
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

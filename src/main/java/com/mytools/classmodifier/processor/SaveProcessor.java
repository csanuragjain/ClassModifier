package com.mytools.classmodifier.processor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.ConsoleViewer;
import com.mytools.classmodifier.Utility;

public class SaveProcessor {

	/**
	 * @param args
	 */
	public static void addSaveAction() {
		// TODO Auto-generated method stub
		ClassModifier.save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						try {
							ConsoleViewer.cleanConsole();
							int chosenIndex = TabsProcessor.getCurrentIndex();
							String fileName = TabsProcessor
									.getToolTipText(chosenIndex);
							if (!fileName.contains(".smali")
									&& !fileName.contains(Utility.practiseFile)) {
								JOptionPane.showMessageDialog(null,
										"Only Smali files can be changed");
								return null;
							}
							RTextScrollPane htmlScroll = TabsProcessor
									.getComponent(chosenIndex);
							RSyntaxTextArea htmlPane = (RSyntaxTextArea) htmlScroll
									.getViewport().getView();
							File modifiedClass;
							if (fileName.contains("TestEnv.smali")) {
								File rough = new File(Utility
										.getRoughFolderPath());
								Utility.deleteFile(rough);
								rough.mkdirs();
								if (Utility.className.equals("doNotDelete")) {
									Utility.prepareProject();
								}
								Utility.writeFile(
										Utility.getRoughFilePath(Utility.practiseSmaliFile),
										htmlPane.getText());
								Utility.processSmaliPractiseFiles(new File(
										Utility.getRoughFilePath(Utility.practiseSmaliFile)));
								TabsProcessor
										.refreshPractiseTabs(Utility.practiseFile);
							} else if (fileName.contains("TestEnv.java")) {
								File rough = new File(Utility
										.getRoughFolderPath());
								Utility.deleteFile(rough);
								rough.mkdirs();
								if (Utility.className.equals("doNotDelete")) {
									Utility.prepareProject();
								}
								Utility.writeFile(
										Utility.getRoughFilePath(Utility.practiseFile),
										htmlPane.getText());
								Utility.compileFile(Utility.practiseFile,
										new File(Utility.getRoughFolderPath()));
								File output = Utility.findFile(
										new File(Utility.getRoughFolderPath()),
										".class");
								Utility.processPractiseClassFiles(output);
								TabsProcessor
										.refreshPractiseTabs(Utility.practiseSmaliFile);
							} else {
								ConsoleViewer
										.setText("Cleaning previously generated files...");
								Utility.rebuildProject();
								ConsoleViewer
										.setText("Cleaned previously generated files...");
								String smaliPath = Utility.getSmaliPath()
										+ File.separator
										+ Utility.className.replace(".class",
												".smali");
								Utility.writeFile(smaliPath, htmlPane.getText());
								modifiedClass = Utility
										.processSmaliFiles(new File(smaliPath));
								Utility.processClassFiles(modifiedClass);
								TabsProcessor.refreshTabs();
							}

							String title = TabsProcessor
									.getToolTitle(chosenIndex);
							if (!title.startsWith("\\*")) {
								title = title.replaceFirst("\\*", "");
							}
							TabsProcessor.setTitle(chosenIndex, title);
							JOptionPane.showMessageDialog(null,
									"Convertion Successful");
						} catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(
									null,
									"Issue while converting.Please retry "
											+ e.getMessage());
						}
						return null;
					}

				};
				worker.execute();
			}
		});
	}

}

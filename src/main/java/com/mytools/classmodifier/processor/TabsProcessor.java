package com.mytools.classmodifier.processor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.ClosableTabbedPane;
import com.mytools.classmodifier.Utility;

public class TabsProcessor {

	static Map<String, Integer> openedTabs = new HashMap<String, Integer>();
	public static ClosableTabbedPane tabPane = null;
	static int currentTabIndex = -1, previousTabIndex = -1;

	public static int getCurrentIndex() {
		return tabPane.getSelectedIndex();
	}

	public static String getToolTipText(int chosenIndex) {
		return tabPane.getToolTipTextAt(chosenIndex);
	}

	public static String getToolTitle(int chosenIndex) {
		return tabPane.getTitleAt(chosenIndex);
	}

	public static void setTitle(int chosenIndex, String title) {
		tabPane.setTitleAt(chosenIndex, title);
	}

	public static RTextScrollPane getComponent(int chosenIndex) {
		return (RTextScrollPane) tabPane.getComponentAt(chosenIndex);
	}

	public static void clearAllTabs() {
		tabPane.removeAll();
		openedTabs.clear();
	}

	public static void addTabListener() {
		ClassModifier.removeAllTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					tabPane.removeAll();
					openedTabs.clear();
				} catch (Exception e) {

				}
			}
		});
		tabPane = new ClosableTabbedPane() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean tabAboutToClose(int tabIndex) {
				// int currSel=currentTabIndex;
				if (tabIndex == previousTabIndex) {
					openedTabs.remove(tabPane.getToolTipTextAt(tabIndex));
				} else {
					openedTabs.remove(tabPane.getToolTipTextAt(tabIndex));
					try {
						tabPane.setSelectedIndex(previousTabIndex);
					} catch (Exception e) {
					}
				}

				return true;
			}
		};
		tabPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent ce) {
				previousTabIndex = currentTabIndex;
				currentTabIndex = tabPane.getSelectedIndex();
			}
		});

		setFontOnTab();
	}

	public static void setFontOnTab() {
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);
		tabPane.setFont(font);
		tabPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
	}

	public static void refreshTabs() {
		try {
			String refreshFile = Utility.className.replace(".class", ".java");
			for (int i = 0; i < tabPane.getTabCount(); i++) {
				if (tabPane.getToolTipTextAt(i).contains(refreshFile)) {
					tabPane.remove(i);
					break;
				}
			}
			String key = "";
			for (Map.Entry<String, Integer> entry : openedTabs.entrySet()) {
				if (entry.getKey().contains(refreshFile)) {
					key = entry.getKey();
					break;
				}
			}
			if (!key.equals("")) {
				openedTabs.remove(key);
			}
			Utility.openFile(Utility.getToBeProcessedFolder(), refreshFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void refreshPractiseTabs(String refreshFile) {
		try {
			for (int i = 0; i < tabPane.getTabCount(); i++) {
				if (tabPane.getToolTipTextAt(i).contains(refreshFile)) {
					tabPane.remove(i);
					break;
				}
			}
			String key = "";
			for (Map.Entry<String, Integer> entry : openedTabs.entrySet()) {
				if (entry.getKey().contains(refreshFile)) {
					key = entry.getKey();
					break;
				}
			}
			if (!key.equals("")) {
				openedTabs.remove(key);
			}
			Utility.openFile(Utility.getRoughFolderPath(), refreshFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createTab(String filePath) {
		try {
			if (filePath.endsWith("java") || filePath.endsWith("smali")) {
				if (!openedTabs.containsKey(filePath)) {
					if (tabPane.getTabCount() < 200) {
						RSyntaxTextArea htmlPane = FrameProcessor.getHtmlPane();
						RTextScrollPane htmlView = new RTextScrollPane(htmlPane);
						if (!filePath.endsWith("smali")
								&& !filePath.contains(Utility.practiseFile)) {
							htmlPane.setEditable(false);
						}
						File file = new File(filePath);
						htmlPane.setText(FileUtils.readFileToString(file,
								"UTF-8"));
						String tabName = filePath.substring(
								filePath.lastIndexOf(File.separator) + 1,
								filePath.length());
						// tabPane.setBackground(Color.RED);
						tabPane.addTab(tabName, htmlView);
						int tabIndex = tabPane.getTabCount() - 1;
						tabPane.setSelectedIndex(tabIndex);
						tabPane.setToolTipTextAt(tabIndex, filePath);
						openedTabs.put(filePath, tabIndex);
						// currentTabIndex=tabIndex;
						htmlPane.setCaretPosition(0);
						htmlPane.getDocument().addDocumentListener(
								new DocumentListener() {

									@Override
									public void removeUpdate(DocumentEvent e) {
										int ind = tabPane.getSelectedIndex();
										String title = tabPane.getTitleAt(ind);
										if (title.contains(".java")
												&& !title
														.contains(Utility.practiseFile)) {
											return;
										}
										if (!title.startsWith("*")) {
											title = "*" + title;
										}
										tabPane.setTitleAt(ind, title);
									}

									@Override
									public void insertUpdate(DocumentEvent e) {
										int ind = tabPane.getSelectedIndex();
										String title = tabPane.getTitleAt(ind);
										if (title.contains(".java")
												&& !title
														.contains(Utility.practiseFile)) {
											return;
										}
										if (!title.startsWith("*")) {
											title = "*" + title;
										}
										tabPane.setTitleAt(ind, title);
									}

									@Override
									public void changedUpdate(DocumentEvent arg0) {

									}
								});
					} else {
						JOptionPane
								.showMessageDialog(null,
										"Excedded maximum limit of 200 tabs. Please close some tabs");
					}
				} else {
					for (int i = 0; i < tabPane.getTabCount(); i++) {
						if (tabPane.getToolTipTextAt(i).equals(filePath)
								|| tabPane.getToolTipTextAt(i).equals(
										"*" + filePath)) {
							tabPane.setSelectedIndex(i);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

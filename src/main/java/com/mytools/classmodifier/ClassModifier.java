package com.mytools.classmodifier;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import com.mytools.classmodifier.processor.*;

/**
 * Shows GUI with options
 * 
 * @author csanuragjain https://cooltrickshome.blogspot.com
 */
public class ClassModifier implements Runnable {

	public static JMenuItem removeAllTab;
	public static JTextArea consoleArea;
	public static JToolBar toolBar;
	public static JTextField searchField, repWithField;
	public static JCheckBox forSearch, backSearch, regexCB, matchCaseCB,
			wholeWord, markAll;
	public static File fileRoot = null;
	public static JEditorPane versionChecker;
	public static JMenuItem updateSoft;
	public static JSplitPane splitPane = null, splitPaneInternal;
	public static JButton updateAvail, findButton, findClass, repWithButton,
			repAll;
	public static JMenuItem open, openProj, save, decompiler, export,
			converter, converterSmali, howToUse, incFont, decFont;
	public JLabel memLabel;
	public String filePath = null;
	public static JFrame frame = null;
	public static String title = "Class Modifier by csanuragjain (https://cooltrickshome.blogspot.com)";

	@Override
	public void run() {
		frame = new JFrame(title);
		ImageIcon imgIcon = new ImageIcon(Utility.getIconPath());
		frame.setIconImage(imgIcon.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu settings = new JMenu("Decompiler");
		JMenu help = new JMenu("Help");

		decompiler = new JMenuItem("Change Decompiler");
		settings.add(decompiler);
		open = new JMenuItem("Open Class");
		open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		file.add(open);
		file.addSeparator();
		openProj = new JMenuItem("Open Project");
		openProj.setAccelerator(KeyStroke.getKeyStroke('P', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		file.add(openProj);
		file.addSeparator();
		save = new JMenuItem("Save & Convert", KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		file.add(save);
		file.addSeparator();
		export = new JMenuItem("Export Class", KeyEvent.VK_E);
		export.setAccelerator(KeyStroke.getKeyStroke('E', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		file.add(export);
		file.addSeparator();
		converter = new JMenuItem("Java 2 Smali Helper", KeyEvent.VK_H);
		converter.setAccelerator(KeyStroke.getKeyStroke('H', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		file.add(converter);
		file.addSeparator();
		converterSmali = new JMenuItem("Smali 2 Java Helper");
		file.add(converterSmali);
		file.addSeparator();
		// findDialog = new FindDialog(this, this);
		// edit.add(new JMenuItem(new ShowFindDialogAction()));
		incFont = new JMenuItem("Increase Code Fontsize");
		incFont.setAccelerator(KeyStroke.getKeyStroke('I', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		edit.add(incFont);
		edit.addSeparator();
		decFont = new JMenuItem("Decrease Code Fontsize");
		decFont.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		edit.add(decFont);
		edit.addSeparator();
		removeAllTab = new JMenuItem("Remove all tabs");
		TabsProcessor.addTabListener();
		edit.add(removeAllTab);

		updateSoft = new JMenuItem("Update Software");
		help.add(updateSoft);
		howToUse = new JMenuItem("How to use Class Modifier");
		help.add(howToUse);

		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(settings);
		menuBar.add(help);
		// htmlPane.setBackground(Color.WHITE);
		// htmlPane.setForeground(Color.BLACK);
		consoleArea = new JTextArea("Console...\n");
		consoleArea.setBackground(Color.BLACK);
		consoleArea.setForeground(Color.WHITE);
		DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		// Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		Font font2 = new Font(Font.MONOSPACED, Font.PLAIN, 16);
		// htmlPane.setFont(font);
		consoleArea.setFont(font2);
		// JScrollPane htmlView = new JScrollPane(htmlPane);
		JScrollPane consoleView = new JScrollPane(consoleArea);
		consoleView.scrollRectToVisible(new Rectangle(0, consoleArea
				.getBounds(null).height, 1, 1));
		splitPaneInternal = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		// splitPaneInternal.setTopComponent(htmlView);
		splitPaneInternal.setTopComponent(TabsProcessor.tabPane);
		splitPaneInternal.setBottomComponent(consoleView);
		splitPaneInternal.setResizeWeight(0.85);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(250);
		splitPane.setLeftComponent(new JLabel());
		splitPane.setRightComponent(splitPaneInternal);
		frame.setJMenuBar(menuBar);

		toolBar = new JToolBar();
		JLabel fi = new JLabel("Find (ALT+F) ");
		toolBar.add(fi);
		searchField = new JTextField(15);
		fi.setLabelFor(searchField);
		fi.setDisplayedMnemonic(KeyEvent.VK_F);
		toolBar.add(searchField);
		JLabel rep = new JLabel("Replace with (ALT+R) ");
		toolBar.add(rep);
		repWithField = new JTextField(15);
		rep.setLabelFor(repWithField);
		rep.setDisplayedMnemonic(KeyEvent.VK_R);
		toolBar.add(repWithField);
		findButton = new JButton("Find Text");
		findClass = new JButton("Find Class");
		repWithButton = new JButton("Replace With");
		repAll = new JButton("Replace All");
		ToolBarProcessor.addToolBarAction();
		toolBar.add(repAll);
		forSearch = new JCheckBox("Forward Search");
		forSearch.setSelected(true);
		toolBar.add(forSearch);
		backSearch = new JCheckBox("Backward Search");
		toolBar.add(backSearch);
		regexCB = new JCheckBox("Regex");
		toolBar.add(regexCB);
		matchCaseCB = new JCheckBox("Match Case");
		toolBar.add(matchCaseCB);
		wholeWord = new JCheckBox("Whole Word");
		toolBar.add(wholeWord);
		markAll = new JCheckBox("MarkAll");
		markAll.setSelected(true);
		toolBar.add(markAll);
		updateAvail = new JButton();
		updateAvail.setHorizontalAlignment(SwingConstants.LEFT);
		updateAvail.setBorderPainted(false);
		updateAvail.setOpaque(false);
		updateAvail.setBackground(Color.WHITE);
		updateAvail.setForeground(Color.BLUE);
		updateAvail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSoft.doClick(0);
			}
		});
		toolBar.add(updateAvail);
		versionChecker = new JEditorPane();
		VersionProcessor.addVersionCheck();
		JSplitPane splitPaneOuter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		toolBar.setLayout(new FlowLayout(0));
		// splitPaneInternal.setTopComponent(htmlView);
		splitPaneOuter.setTopComponent(toolBar);
		splitPaneOuter.setBottomComponent(splitPane);
		splitPaneOuter.setResizeWeight(0.0001);

		DecompilerProcessor.addDecompilerAction();
		ExportProcessor.addExportAction();

		ConverterProcessor.addSampleConverter();
		HowToUseProcessor.addHowToUseAction();
		UpdateSoftwareProcessor.addUpdateAction();
		FontProcessor.addFontAction();

		SaveProcessor.addSaveAction();
		OpenFolderProcessor.openFolderAction();
		OpenFileProcessor.openFileAction();

		frame.add(splitPaneOuter);
		// frame.add(splitPane);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				Utility.destroyDanglingProcess();
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ClassModifier());
	}

}

package com.mytools.classmodifier;

import java.awt.Desktop;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileDeleteStrategy;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import com.mytools.classmodifier.processor.TabsProcessor;

/**
 * Contains methods used for performing conversions
 * 
 * @author csanuragjain https://cooltrickshome.blogspot.com
 */
public class Utility {

	public static String pathSeparator = File.pathSeparator;
	public static String className = "doNotDelete";
	public static int compilerToUse = 0; // 0 for jadx and 1 for jd
	public static List<Process> processList = new ArrayList<>();
	public final static String practiseFile = "TestEnv.java";
	public final static String practiseSmaliFile = "TestEnv.smali";

	public static void searchText(String operation, boolean regex,
			boolean matchCase, boolean wholeWord, boolean markAll,
			boolean forSearch) {
		int chosenIndex = TabsProcessor.getCurrentIndex();
		if (chosenIndex < 0) {
			JOptionPane.showMessageDialog(null, "No files opened to search on");
			return;
		}
		RTextScrollPane htmlScroll = TabsProcessor.getComponent(chosenIndex);
		RSyntaxTextArea searchtextArea = (RSyntaxTextArea) htmlScroll
				.getViewport().getView();
		SearchContext context = new SearchContext();
		String text = ClassModifier.searchField.getText();
		if (text.length() == 0) {
			return;
		}
		context.setSearchFor(text);
		context.setMatchCase(matchCase);
		context.setRegularExpression(regex);
		context.setSearchForward(forSearch);
		context.setWholeWord(wholeWord);
		context.setMarkAll(markAll);
		boolean found = false;
		if (operation.equals("replace")) {
			context.setReplaceWith(ClassModifier.repWithField.getText());
			found = SearchEngine.replace(searchtextArea, context).wasFound();
		} else if (operation.equals("replaceAll")) {
			context.setReplaceWith(ClassModifier.repWithField.getText());
			found = SearchEngine.replaceAll(searchtextArea, context).wasFound();
		} else {
			found = SearchEngine.find(searchtextArea, context).wasFound();
		}
		if (!found) {
			JOptionPane.showMessageDialog(null, "Text not found");
		}
	}

	/**
	 * Converts given class file to Smali
	 * 
	 * @param f
	 *            File to be converted to Smali
	 * @return
	 * @throws Exception
	 */
	public static File processClassFiles(File f) throws Exception {
		File classPath = new File(f.getAbsolutePath());
		File outputFolder = new File(getToBeProcessedFolder());
		File outputDex = new File(outputFolder.getAbsolutePath()
				+ File.separator + "temp.dex");

		Utility.changeClass2Dex(classPath, outputDex);
		Utility.changeDex2Smali(outputDex, outputFolder);
		if (Utility.compilerToUse == 0) {
			Utility.decompileJar2Java(outputDex, outputFolder);
		} else {
			Utility.decompileJdCli(classPath, outputFolder);
		}
		File output = Utility.findFile(outputFolder, ".smali");

		outputDex.delete();
		return output;
	}

	/**
	 * Converts given Practise class file to Smali
	 * 
	 * @param f
	 *            File to be converted to Smali
	 * @return
	 * @throws Exception
	 */
	public static File processPractiseClassFiles(File f) throws Exception {
		File classPath = new File(f.getAbsolutePath());
		File outputFolder = new File(getRoughFolderPath());
		File outputDex = new File(outputFolder.getAbsolutePath()
				+ File.separator + "temp.dex");

		Utility.changeClass2Dex(classPath, outputDex);
		Utility.changeDex2Smali(outputDex, outputFolder);
		File output = Utility.findFile(outputFolder, ".smali");

		outputDex.delete();
		return output;
	}

	/**
	 * Opens the pattern file from given folder
	 * 
	 * @param f
	 *            File to be converted to Smali
	 * @return
	 * @throws Exception
	 */
	public static void openFile(String f, String pattern) throws Exception {
		File folder = new File(f);
		File output;
		if (folder.isDirectory()) {
			output = Utility.findFile(folder, pattern);
		} else {
			output = folder;
		}
		TabsProcessor.createTab(output.getAbsolutePath());
	}

	/**
	 * Compiles the java file at provided filepath
	 * 
	 * @param file
	 *            File path relative to javaCode folder
	 * @param compileDir
	 *            Defines the File directory from which compilation would happen
	 * @throws Exception
	 */
	public static void compileFile(String file, File compileDir)
			throws Exception {
		String[] commands = { "javac", file };
		String compileCommand = "";
		for (String command : commands) {
			compileCommand += command + " ";
		}
		ConsoleViewer.setText("Compiling: " + compileCommand);
		// Runtime.getRuntime().exec(commands,null,compileDir);
		runProgram(commands, compileDir.getAbsolutePath());
	}

	/**
	 * Converts given smali file to class
	 * 
	 * @param f
	 *            File to be converted to class
	 * @return
	 * @throws Exception
	 */
	public static File processSmaliFiles(File f) throws Exception {
		File smaliPath = new File(f.getAbsolutePath());
		File outputDex = new File(getToBeProcessedFolder() + File.separator
				+ "temp.dex");
		File outputFolder = new File(getModifiedClass());// new
															// File(f.getParentFile().getAbsolutePath()+File.separator+"output");
		Utility.changeSmali2Dex(smaliPath, outputDex);
		Utility.changeDex2Class(outputDex, outputFolder);
		File output = Utility.findFile(outputFolder, ".class");

		outputDex.delete();
		return output;
	}

	/**
	 * Converts given smali file to class
	 * 
	 * @param f
	 *            File to be converted to class
	 * @return
	 * @throws Exception
	 */
	public static File processSmaliPractiseFiles(File f) throws Exception {
		File smaliPath = new File(f.getAbsolutePath());
		File outputDex = new File(getRoughFolderPath() + File.separator
				+ "temp.dex");
		File outputFolder = new File(getRoughFolderPath());// new
															// File(f.getParentFile().getAbsolutePath()+File.separator+"output");
		Utility.changeSmali2Dex(smaliPath, outputDex);
		Utility.decompileJar2Java(outputDex, outputFolder);
		File output = Utility.findFile(outputFolder, ".java");

		outputDex.delete();
		return output;
	}

	/**
	 * Retrieves the pattern files from the input directory
	 * 
	 * @param inputFolder
	 *            Folder from which pattern files will be searched
	 * @return File containing the pattern files
	 */
	public static File findFile(File f, String pattern) {
		File[] listFile = f.listFiles();
		for (File f2 : listFile) {
			if (f2.isDirectory()) {
				File f3 = findFile(f2, pattern);
				if (f3 != null) {
					return f3;
				}
			}
			if (f2.getName().contains(pattern)) {
				return f2;
			}
		}
		return null;
	}

	/**
	 * It runs the program from the provided working directory
	 * 
	 * @param program
	 *            Program which would be run
	 * @param workingDir
	 *            Directory from which program would be run
	 * @return returns 0 if success else 1
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static int runProgram(String[] program, String workingDir)
			throws InterruptedException, IOException {
		int errorFound = 0;
		String commandRun = "";
		for (String command : program) {
			commandRun += command + " ";
		}
		ConsoleViewer.setText(commandRun);
		Process proc = Runtime.getRuntime().exec(program, null,
				new File(workingDir));
		processList.add(proc);
		ProcessHandler inputStream = new ProcessHandler(proc.getInputStream(),
				"INPUT");
		ProcessHandler errorStream = new ProcessHandler(proc.getErrorStream(),
				"ERROR");
		/* start the stream threads */
		inputStream.start();
		errorStream.start();

		if (0 == proc.waitFor()) {
			ConsoleViewer.setText("Process completed successfully");
		} else {
			if (commandRun.contains("2Dex")) {
				JOptionPane.showMessageDialog(null,
						"Conversion failed. Kindly check logs");
			}
			ConsoleViewer
					.setText("Encountered errors/warnings while running this program");
		}
		processList.remove(proc);
		return errorFound;
	}

	/**
	 * Extra function Converts dex file to Jar
	 * 
	 * @param dexFile
	 *            Input dex file to be converted to Jar
	 * @param outputFile
	 *            Resulting jar file
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JadxException
	 */
	public static void changeDex2Class(File dexFile, File outputFile)
			throws InterruptedException, IOException {
		// Dex2jarCmd.main(new
		// String[]{"--force","--output",outputFile.getAbsolutePath(),dexFile.getAbsolutePath()});
		String[] commands = {
				"java",
				"-cp",
				"." + pathSeparator + getDexLibraryPath() + File.separator
						+ "*", getDexHelperClassName(), "dex2Jar",
				dexFile.getAbsolutePath(), outputFile.getAbsolutePath() };
		runProgram(commands, getHelperPath());
	}

	/**
	 * Converts dex format file to smali
	 * 
	 * @param dexFile
	 *            input dex file
	 * @param outputFile
	 *            output smali file
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void changeDex2Smali(File dexFile, File outputFile)
			throws InterruptedException, IOException {
		// BaksmaliCmd.main(new
		// String[]{"--force","--output",outputFile.getAbsolutePath(),dexFile.getAbsolutePath()});
		String[] commands = {
				"java",
				"-cp",
				"." + pathSeparator + getDexLibraryPath() + File.separator
						+ "*", getDexHelperClassName(), "dex2Smali",
				dexFile.getAbsolutePath(), outputFile.getAbsolutePath() };
		runProgram(commands, getHelperPath());
	}

	/**
	 * Change smali format file to dex
	 * 
	 * @param smaliFile
	 *            input smali file
	 * @param outputFile
	 *            output dex file
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void changeSmali2Dex(File smaliFile, File outputFile)
			throws InterruptedException, IOException {
		// SmaliCmd.main(new
		// String[]{"--output",outputFile.getAbsolutePath(),dexFile.getAbsolutePath()});
		String[] commands = {
				"java",
				"-cp",
				"." + pathSeparator + getDexLibraryPath() + File.separator
						+ "*", getDexHelperClassName(), "smali2Dex",
				smaliFile.getAbsolutePath(), outputFile.getAbsolutePath() };
		runProgram(commands, getHelperPath());
	}

	/**
	 * Decompiles the jar file to source java into the outputSourceDirectory
	 * 
	 * @param jarPath
	 *            Path of the dex file to be decompiled
	 * @param outputSourceDirectory
	 *            Output directory where decompiled files would be kept
	 * @throws Exception
	 */
	public static void decompileJar2Java(File jarPath,
			File outputSourceDirectory) throws Exception {
		/*
		 * JadxDecompiler jadx = new JadxDecompiler(); jadx.setOutputDir(new
		 * File(outputSourceDirectory)); jadx.loadFile(jarPath); jadx.save(); if
		 * (jadx.getErrorsCount() != 0) { jadx.printErrorsReport(); } else {
		 * ConsoleViewer.setText("Completed"); }
		 */
		String[] commands = {
				"java",
				"-cp",
				"." + pathSeparator + getJadLibraryPath() + File.separator
						+ "*", getJadHelperClassName(), "jadx",
				jarPath.getAbsolutePath(),
				outputSourceDirectory.getAbsolutePath() };
		runProgram(commands, getHelperPath());
	}

	/**
	 * Decompiles the class file to source java into the outputSourceDirectory
	 * 
	 * @param jarPath
	 *            Path of the dex file to be decompiled
	 * @param outputSourceDirectory
	 *            Output directory where decompiled files would be kept
	 * @throws Exception
	 */
	public static void decompileJdCli(File jarPath, File outputSourceDirectory)
			throws Exception {
		String[] commands = { "java", "-jar", getJdLibraryPath(), "-od",
				outputSourceDirectory.getAbsolutePath(),
				jarPath.getAbsolutePath() };
		runProgram(commands, getHelperPath());
	}

	public static void destroyDanglingProcess() {
		for (Process p : processList) {
			try {
				p.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sleeps and free up memory
	 * 
	 * @param sec
	 */
	public static void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
			System.gc();
		} catch (Exception e) {

		}
	}

	/**
	 * Converts the passes class to dex format
	 * 
	 * @param classPath
	 *            class file
	 * @param dexPath
	 *            output file path
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void changeClass2Dex(File classPath, File dexPath)
			throws InterruptedException, IOException {
		// Jar2Dex.main(new String[]{"--force","--output",dexPath,classPath});
		String[] commands = {
				"java",
				"-cp",
				"." + pathSeparator + getDexLibraryPath() + File.separator
						+ "*", getDexHelperClassName(), "class2Dex",
				classPath.getAbsolutePath(), dexPath.getAbsolutePath() };
		runProgram(commands, getHelperPath());
	}

	/**
	 * Writes the filecontent to the provided file
	 * 
	 * @param newFilePath
	 *            File on which content would be written
	 * @param contentToBeWritten
	 *            Content to be written to file
	 * @throws IOException
	 */
	public static void writeFile(String newFilePath, String contentToBeWritten)
			throws IOException {
		File f = new File(newFilePath);
		f.getParentFile().mkdirs();
		FileWriter fw = new FileWriter(newFilePath);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(contentToBeWritten);
		bw.flush();
		bw.close();
		fw.close();
	}

	/**
	 * Used to open the url passed
	 * 
	 * @param uri
	 *            URL to be opened in browser
	 */
	public static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				Font font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
				JFrame f = new JFrame("URL to open");
				JEditorPane ed = new JEditorPane();
				ed.setFont(font);
				ed.setText("Java is not able to launch links on your computer.\nRequest you to kindly open below link manually \n"
						+ uri.toString());
				f.add(ed);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				f.setSize(500, 200);
				f.setLocation(dim.width / 2 - f.getSize().width / 2, dim.height
						/ 2 - f.getSize().height / 2);
				f.setVisible(true);
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		} else {
			Font font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
			JFrame f = new JFrame("URL to open");
			JEditorPane e = new JEditorPane();
			e.setFont(font);
			e.setText("Java is not able to launch links on your computer.\nRequest you to kindly open below link manually \n"
					+ uri.toString());
			f.add(e);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			f.setSize(500, 200);
			f.setLocation(dim.width / 2 - f.getSize().width / 2, dim.height / 2
					- f.getSize().height / 2);
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}

	/**
	 * Contains the base path where everything lies
	 * 
	 * @return
	 */
	public static String getBasePath() {
		File f = new File("");
		return f.getAbsolutePath();
	}

	/**
	 * Returns the project path to do not delete
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		return getBasePath() + File.separator + "Projects" + File.separator
				+ className;
	}

	/**
	 * Contains jad library
	 * 
	 * @return
	 */
	public static String getJadLibraryPath() {
		return getBasePath() + File.separator + "ClassModifier_lib"
				+ File.separator + "jadx";
	}

	/**
	 * Contains jd library
	 * 
	 * @return
	 */
	public static String getJdLibraryPath() {
		return getBasePath() + File.separator + "ClassModifier_lib"
				+ File.separator + "jd" + File.separator + "jd-cli.jar";
	}

	/**
	 * Contains the software icon
	 * 
	 * @return
	 */
	public static String getIconPath() {
		return getBasePath() + File.separator + "ClassModifier_lib"
				+ File.separator + "icon" + File.separator + "icon.png";
	}

	/**
	 * Contains the dex library for conversion
	 * 
	 * @return
	 */
	public static String getDexLibraryPath() {
		return getBasePath() + File.separator + "ClassModifier_lib"
				+ File.separator + "dex";
	}

	/**
	 * Program for utilizing jad
	 * 
	 * @return
	 */
	public static String getJadHelperClassName() {
		return "com.cooltrickshome.helper.RunProgramJad";
	}

	/**
	 * Program for utilizing dex library
	 * 
	 * @return
	 */
	public static String getDexHelperClassName() {
		return "com.cooltrickshome.helper.RunProgramDex";
	}

	/**
	 * Contains the Practise file path
	 * 
	 * @return
	 */
	public static String getRoughFilePath(String fileName) {
		return getProjectPath() + File.separator + "roughFile" + File.separator
				+ fileName;
	}

	/**
	 * Contains the practise folder
	 * 
	 * @return
	 */
	public static String getRoughFolderPath() {
		return getProjectPath() + File.separator + "roughFile";
	}

	/**
	 * Contains the compiled class files of apk
	 * 
	 * @return
	 */
	public static String getClassFile() {
		return getProjectPath() + File.separator + "classFile";
	}

	/**
	 * Contains the source code of apk
	 * 
	 * @return
	 */
	public static String getToBeProcessedFolder() {
		return getProjectPath() + File.separator + "tobeProcessed";
	}

	/**
	 * Contains apk source with no decoding, used to extract dex
	 * 
	 * @return
	 */
	public static String getPractiseFile() {
		return getBasePath() + File.separator + "ClassModifier_lib"
				+ File.separator + "TestFile" + File.separator + practiseFile;
	}

	/**
	 * Contains apk source with no decoding, used to extract dex
	 * 
	 * @return
	 */
	public static String getPractiseSmaliFile() {
		return getBasePath() + File.separator + "ClassModifier_lib"
				+ File.separator + "TestFile" + File.separator
				+ practiseSmaliFile;
	}

	/**
	 * Contains the modified jar files
	 * 
	 * @return
	 */
	public static String getModifiedClass() {
		return getProjectPath() + File.separator + "modifiedClass";
	}

	/**
	 * Used when smali editing is used
	 * 
	 * @return
	 */
	public static String getSmaliPath() {
		return getProjectPath() + File.separator + "modifiedSmali";
	}

	/**
	 * Path of helper class containing conversion logic
	 * 
	 * @return
	 */
	public static String getHelperPath() {
		return getBasePath() + File.separator + "ClassModifier_lib"
				+ File.separator + "helper";
	}

	/**
	 * Makes the folder required by project
	 */
	public static void prepareProject() {
		File fi = new File(getProjectPath());
		ConsoleViewer.setText("Checking if project already exist");
		File fi2 = new File(fi.getParent() + File.separator + className + "."
				+ System.currentTimeMillis());
		if (fi.exists() && !Utility.className.equals("doNotDelete")) {
			ConsoleViewer.setText("Backup existing project");
			fi.renameTo(fi2);
		}
		ConsoleViewer.setText("Creating fresh project");
		fi.mkdirs();
		File f = new File(getProjectPath() + File.separator + "roughFile");
		f.mkdirs();
		File f2 = new File(getProjectPath() + File.separator + "tobeProcessed");
		f2.mkdirs();
		File f4 = new File(getProjectPath() + File.separator + "classFile");
		f4.mkdirs();
		File f5 = new File(getProjectPath() + File.separator + "modifiedClass");
		f5.mkdirs();
		File f7 = new File(getSmaliPath());
		f7.mkdirs();
	}

	/**
	 * remove and remake the folder required by project
	 */
	public static void rebuildProject() {
		File f2 = new File(getProjectPath() + File.separator + "tobeProcessed");
		deleteFile(f2);
		f2.mkdirs();
		File f5 = new File(getProjectPath() + File.separator + "modifiedClass");
		deleteFile(f5);
		f5.mkdirs();
		File f7 = new File(getSmaliPath());
		deleteFile(f7);
		f7.mkdirs();
	}

	/**
	 * force delete a file
	 */
	public static void deleteFile(File f) {
		try {
			FileDeleteStrategy.FORCE.delete(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

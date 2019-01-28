package com.mytools.classmodifier.processor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.ConsoleViewer;
import com.mytools.classmodifier.Utility;

public class HowToUseProcessor {

	static String howToUseURL = "https://cooltrickshome.blogspot.com/2019/01/classmodifier-utility-to-easily-modify.html";

	public static void addHowToUseAction() {
		ClassModifier.howToUse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						try {
							ConsoleViewer.cleanConsole();
							ConsoleViewer
									.setText("Redirecting to help manual at "
											+ howToUseURL);
							java.net.URI uri = new java.net.URI(howToUseURL);
							Utility.open(uri);
						} catch (Exception e) {
							ConsoleViewer
									.setText("Issue while opening help website "
											+ howToUseURL
											+ ".Please open it manually. "
											+ e.getMessage());
							Font font = new Font(Font.MONOSPACED, Font.PLAIN,
									16);
							JFrame f = new JFrame("URL to open");
							JEditorPane ed = new JEditorPane();
							ed.setFont(font);
							ed.setText("Java is not able to launch links on your computer.\nRequest you to kindly open below link manually \n"
									+ howToUseURL);
							f.add(ed);
							Dimension dim = Toolkit.getDefaultToolkit()
									.getScreenSize();
							f.setSize(500, 200);
							f.setLocation(
									dim.width / 2 - f.getSize().width / 2,
									dim.height / 2 - f.getSize().height / 2);
							f.setVisible(true);
							f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

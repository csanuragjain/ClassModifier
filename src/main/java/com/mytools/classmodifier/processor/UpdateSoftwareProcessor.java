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

public class UpdateSoftwareProcessor {

	static String gitURL = "https://github.com/csanuragjain/ClassModifier/releases";

	public static void addUpdateAction() {
		ClassModifier.updateSoft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						try {
							ConsoleViewer.cleanConsole();
							ConsoleViewer.setText("Redirecting to update site "
									+ gitURL);
							java.net.URI uri = new java.net.URI(gitURL);
							Utility.open(uri);
						} catch (Exception e) {
							ConsoleViewer
									.setText("Issue while opening update website "
											+ gitURL
											+ ".Please open it manually. "
											+ e.getMessage());
							Font font = new Font(Font.MONOSPACED, Font.PLAIN,
									16);
							JFrame f = new JFrame("URL to open");
							JEditorPane ed = new JEditorPane();
							ed.setFont(font);
							ed.setText("Java is not able to launch links on your computer.\nRequest you to kindly open below link manually \n"
									+ gitURL);
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

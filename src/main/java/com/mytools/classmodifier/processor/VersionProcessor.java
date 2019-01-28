package com.mytools.classmodifier.processor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

import com.mytools.classmodifier.ClassModifier;

public class VersionProcessor {

	static String currVersion = "1.0.0";
	static String checkVersion = "https://raw.githubusercontent.com/csanuragjain/ClassModifier/master/VERSION";

	public static void addVersionCheck() {
		try {
			ClassModifier.versionChecker
					.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							if (!evt.getPropertyName().equals("page")) {
								return;
							}
							String newVersion = ClassModifier.versionChecker
									.getText().replaceAll("\n", "");
							if (!(newVersion).equals(currVersion)) {
								ClassModifier.updateAvail
										.setText("<html><u>Update Class Modifier to version "
												+ newVersion + "</u></html>");
							}
						}
					});
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					// TODO Auto-generated method stub
					try {
						ClassModifier.versionChecker.setPage(checkVersion);
					} catch (Exception e) {
					}
					return null;
				}

			};
			worker.execute();
			/*
			 * new Thread() { public void run() {
			 * 
			 * } }.start();
			 */
		} catch (Exception e) {
		}
	}

}

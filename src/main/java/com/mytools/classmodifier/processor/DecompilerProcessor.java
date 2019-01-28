package com.mytools.classmodifier.processor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.mytools.classmodifier.ClassModifier;
import com.mytools.classmodifier.Utility;

public class DecompilerProcessor {

	static JFrame decompilerDecider;

	public static void addDecompilerAction() {

		ClassModifier.decompiler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				decompilerDecider = new JFrame("Change Decompiler");
				JLabel l = new JLabel("Change decompiler ");
				String chosen = "";
				if (Utility.compilerToUse == 0) {
					chosen = " (Chosen)";
				}
				JCheckBox jadx = new JCheckBox("Jadx" + chosen, false);
				String chosen2 = "";
				if (Utility.compilerToUse == 1) {
					chosen2 = " (Chosen)";
				}
				JCheckBox jd = new JCheckBox("Jd-cli" + chosen2, false);
				jadx.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						try {
							Utility.compilerToUse = 0;
							JOptionPane
									.showMessageDialog(null,
											"Using Jadx Decompiler for further decompilation");
							decompilerDecider.dispose();
						} catch (Exception e) {
							JOptionPane
									.showMessageDialog(null,
											"Exception while saving decompiler preference.");
						}
					}
				});
				jd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						try {
							Utility.compilerToUse = 1;
							JOptionPane
									.showMessageDialog(null,
											"Using Jd-Cli Decompiler for further decompilation");
							decompilerDecider.dispose();
						} catch (Exception e) {
							JOptionPane
									.showMessageDialog(null,
											"Exception while saving decompiler preference.");
						}
					}
				});
				decompilerDecider.add(l);
				decompilerDecider.add(jadx);
				decompilerDecider.add(jd);
				decompilerDecider.setLayout(new FlowLayout(0));
				decompilerDecider.setVisible(true);
				decompilerDecider.setSize(500, 100);
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				decompilerDecider.setLocation(
						dim.width / 2 - decompilerDecider.getSize().width / 2,
						dim.height / 2 - decompilerDecider.getSize().height / 2);
				decompilerDecider
						.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

	}

}

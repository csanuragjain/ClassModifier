package com.mytools.classmodifier.processor;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mytools.classmodifier.ConsoleViewer;
import com.mytools.classmodifier.Utility;

public class TreeProcessor {

	public static DefaultMutableTreeNode root;
	public static DefaultTreeModel treeModel;
	public static JTree tree;

	public TreeProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void intializeTree(File fileRoot) {
		root = new DefaultMutableTreeNode();
		treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
		tree.setFont(font);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				doMouseClicked(me);
			}
		});
		CreateChildNodes ccn = new CreateChildNodes(fileRoot, root);
		new Thread(ccn).start();
	}

	public static void changePath(TreePath path) {
		tree.setSelectionPath(path);
		tree.scrollPathToVisible(path);
	}

	public static String createFilePath(TreePath treePath) {
		StringBuilder sb = new StringBuilder();
		Object[] nodes = treePath.getPath();
		for (int i = 0; i < nodes.length; i++) {
			sb.append(File.separatorChar).append(nodes[i].toString());
		}
		return sb.toString();
	}

	static void doMouseClicked(MouseEvent me) {
		TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
		if (tp != null) {
			try {
				String filePath = createFilePath(tp);
				File file = new File(Utility.getToBeProcessedFolder()
						+ File.separator + filePath);
				TabsProcessor.createTab(file.getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"File Not Found " + e.getMessage());
			}
		}
	}

	public static void searchClass(String className) {
		final String searchClass = className;
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
				try {
					ConsoleViewer.cleanConsole();
					@SuppressWarnings("unchecked")
					Enumeration<DefaultMutableTreeNode> e = root
							.depthFirstEnumeration();
					while (e.hasMoreElements()) {
						DefaultMutableTreeNode node = e.nextElement();
						if (node.toString().toLowerCase()
								.contains(searchClass.toLowerCase())) {
							TreePath path = new TreePath(node.getPath());
							String pathFinal = "";
							for (Object o : path.getPath()) {
								pathFinal += (o + File.separator);
							}
							ConsoleViewer.setText(pathFinal);
							TreeProcessor.changePath(path);
							// return new TreePath(node.getPath());
						}
					}
					JOptionPane.showMessageDialog(null,
							"Search complete, result shown in console.");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Issue while searching class " + e.getMessage());
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

	public class CreateChildNodes implements Runnable {

		private DefaultMutableTreeNode root;

		private File fileRoot;

		public CreateChildNodes(File fileRoot, DefaultMutableTreeNode root) {
			this.fileRoot = fileRoot;
			this.root = root;
		}

		@Override
		public void run() {
			createChildren(fileRoot, root);
			DefaultMutableTreeNode currentNode = root.getNextNode();
			if (currentNode.getLevel() == 1)
				tree.expandPath(new TreePath(currentNode.getPath()));
		}

		private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
			File[] files = fileRoot.listFiles();
			if (files == null)
				return;

			for (File file : files) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
						new FileNode(file));
				node.add(childNode);
				if (file.isDirectory()) {
					createChildren(file, childNode);
				}
			}
		}

	}

	public class FileNode {

		private File file;

		public FileNode(File file) {
			this.file = file;
		}

		@Override
		public String toString() {
			String name = file.getName();
			if (name.equals("")) {
				return file.getAbsolutePath();
			} else {
				return name;
			}
		}
	}

}

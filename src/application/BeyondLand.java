package application;


import java.awt.BorderLayout;

import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;

import gui.GLFrame;
import gui.ListPanel;
import gui.UIMenu;

/**
 * Executor class for "Beyondland" application
 */
public class BeyondLand {
	/**
	 * Main method for the execution
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GLFrame fr = new GLFrame(500, 500, "Beyondland");
		ListPanel lpn = new ListPanel();
		SpaceModel model = new SpaceModel(fr, lpn);
		lpn.attachModel(model);
		UIMenu menu = new UIMenu(fr, model);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		fr.add(menu, BorderLayout.NORTH);
		fr.add(lpn, BorderLayout.EAST);
		fr.setVisible(true);
		fr.setResizable(true);
	}
}

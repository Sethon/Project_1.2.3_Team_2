package application;


import java.awt.BorderLayout;

import gui.GLFrame;
import gui.ListPanel;
import gui.UIMenu;


public class BeyondLand {
	public static void main(String[] args) {
		GLFrame fr = new GLFrame(500, 500, "Beyondland");
		ListPanel lpn = new ListPanel();
		SpaceModel model = new SpaceModel(fr, lpn);
		lpn.attachModel(model);
		UIMenu menu = new UIMenu(fr, model);
		fr.add(menu, BorderLayout.NORTH);
		fr.add(lpn, BorderLayout.EAST);
		fr.setVisible(true);
		fr.setResizable(false);
	}
}

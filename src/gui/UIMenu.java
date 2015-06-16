package gui;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import application.SpaceModel;
import plyer.PLYReader;
import plyer.PLYWriter;
import surfaces.FVPolygonMesh;


public class UIMenu extends JMenuBar implements ActionListener {
	private static final long 	  serialVersionUID = 1L;
	private final String      	  FILE			   = "File";
	private final String      	  LOADING    	   = "Load file";
	private final String      	  SAVING    	   = "Save file";
	private final String	  	  NEW              = "Add new";
	private final String	  	  PARSURF          = "Parametric surface";
	public final static String	  TORUS            = "Torus";
	public final static String	  SPIRAL           = "Spiral";
	private final String	  	  POLYMESH         = "Polygon mesh";
	public final static String	  FV               = "FV mesh";
	public final static String	  NURBS            = "NURBS";
	private final String      	  INFO             = "Info";
	private final String      	  CREDITS          = "Credits";
	private final int      	  	  FACTOR           = 1;
	
	private JMenuItem         	  menuItem;
	private GLFrame           	  frame;
	private SpaceModel		  	  model;


	public UIMenu(GLFrame frame, SpaceModel model) {
		this.frame = frame;
		this.model = model;
		
		JMenu subMenu = new JMenu(FILE);
		menuItem = new JMenuItem(LOADING);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(200, 50));
		subMenu.add(menuItem);
		
		menuItem = new JMenuItem(SAVING);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(200, 50));
		subMenu.add(menuItem);
		
		this.add(subMenu);
		
		subMenu = new JMenu(NEW);
		JMenu subSubMenu = new JMenu(PARSURF);
		menuItem = new JMenuItem(TORUS);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(200, 50));
		subSubMenu.add(menuItem);
		
		menuItem = new JMenuItem(SPIRAL);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(200, 50));
		subSubMenu.add(menuItem);
		
		subMenu.add(subSubMenu);
		
		subSubMenu = new JMenu(POLYMESH);
		menuItem = new JMenuItem(FV);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(200, 50));
		subSubMenu.add(menuItem);

		subMenu.add(subSubMenu);
		
		menuItem = new JMenuItem(NURBS);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(200, 50));
		subMenu.add(menuItem);
		
		this.add(subMenu);
		
		menuItem = new JMenuItem(INFO);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(80, 50));
		this.add(menuItem);
		
		menuItem = new JMenuItem(CREDITS);
		menuItem.addActionListener(this);
		menuItem.setMaximumSize(new Dimension(80, 50));
		this.add(menuItem);
	}

	/*
	 * action handling
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem mItem = (JMenuItem) e.getSource();
		switch (mItem.getText()) {
		case LOADING:
			 FileDialog fdL = new FileDialog(frame, "Load .ply file", FileDialog.LOAD);
			 fdL.setFilenameFilter(new PLYFilter());
             fdL.setVisible(true);
             String meshModel1 = fdL.getFile();
             if (meshModel1 != null) {
            	 meshModel1 = fdL.getDirectory() + meshModel1;
            	 PLYReader plyR = new PLYReader(FACTOR);
            	 model.addSurface(plyR.getFVMesh(meshModel1));
             }
			break;
		case SAVING:
			FileDialog fdS = new FileDialog(frame, "Save .ply file", FileDialog.SAVE);
            fdS.setVisible(true);
            String meshModel2 = fdS.getFile();
            if (meshModel2 != null) {
            	meshModel2 = fdS.getDirectory() + meshModel2;
            	PLYWriter plyW = new PLYWriter();
            	ArrayList<FVPolygonMesh> tmp = model.getMeshList();
            	for (int i = 0; i < tmp.size(); i++) {
            		if (i > 0) {
            			plyW.writeFVMesh(meshModel2 + i, tmp.get(i));
            		} else {
            			plyW.writeFVMesh(meshModel2, tmp.get(i));
            		}	
            	}
            }
			break;
		case TORUS: 
		case SPIRAL:
		case FV:
		case NURBS:
			model.addNewSurface(mItem.getText());
			break;
		case INFO:
			JOptionPane.showMessageDialog(frame, "Controls:" + "\n\tArrows: rotation" + "\n\tW, A, S, D: camera movement" + 
					"\n\tQ, E: zooming",
					"Info",  JOptionPane.PLAIN_MESSAGE);
			break;
		case CREDITS:
			JOptionPane.showMessageDialog(frame, "Project 1-2-3 for Maastricht University, DKE, by Group 2:"
					+ "\n\nBastian Bertram\nCallum Clark\nTim Loderhose\nRicards Marcinkevics\nJoshua Scheidt",
					"Credits",  JOptionPane.PLAIN_MESSAGE);
			break;
		
		default:
			break;
		}
	}
	
	private class PLYFilter implements FilenameFilter {
		 public boolean accept(File dir, String name) {
			 return name.contains(".ply");
		 }
	 }
}
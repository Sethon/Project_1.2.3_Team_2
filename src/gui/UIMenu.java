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
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;

import application.SpaceModel;
import plyer.NRBReader;
import plyer.NRBWriter;
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
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		
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
			 FileDialog fdL = new FileDialog(frame, "Load File", FileDialog.LOAD);
			 fdL.setFilenameFilter(new PLYFilter());
             fdL.setVisible(true);
             String model1 = fdL.getFile();
             if (model1 != null) {
            	 if (model1.contains(".ply")) {
            		 model1 = fdL.getDirectory() + model1;
                	 PLYReader plyR = new PLYReader(FACTOR);
                	 model.addSurface(plyR.getFVMesh(model1)); 
            	 }
            	 else if (model1.contains(".nrb")) {
            		 model1 = fdL.getDirectory() + model1;
                	 NRBReader nrbR = new NRBReader(FACTOR);
                	 model.addSurface(nrbR.getNURBS(model1)); 
            	 }
             }
			break;
		case SAVING:
			FileDialog fdS = new FileDialog(frame, "Save File", FileDialog.SAVE);
            fdS.setVisible(true);
            String model2 = fdS.getFile();
            if (model2 != null) {
            	model2 = fdS.getDirectory() + model2;
            	PLYWriter plyW = new PLYWriter();
            	ArrayList<FVPolygonMesh> tmp = model.getMeshList();
            	for (int i = 0; i < tmp.size(); i++) {
            		if (i > 0) {
            			plyW.writeFVMesh(model2 + i, tmp.get(i));
            		} else {
            			plyW.writeFVMesh(model2, tmp.get(i));
            		}	
            	}
            	
            	NRBWriter nrbW = new NRBWriter();
            	ArrayList<surfaces.NURBS> tmpN = model.getNURBSList();
            	for (int i = 0; i < tmpN.size(); i++) {
            		if (i > 0) {
            			nrbW.writeNURBS(model2 + i, tmpN.get(i));
            		} else {
            			nrbW.writeNURBS(model2, tmpN.get(i));
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
			 return name.contains(".ply") || name.contains(".nrb");
		 }
	 }
}
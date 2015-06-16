package application;

import gui.GLFrame;
import gui.ListPanel;
import gui.UIMenu;

import java.util.ArrayList;

import surfaces.EditableSurface;
import surfaces.FVPolygonMesh;
import surfaces.NURBS;
import surfaces.Point3D;
import surfaces.Spiral;
import surfaces.Surface3D;
import surfaces.SurfaceUtilities;
import surfaces.Torus;

public class SpaceModel {
	private static final int STEP_SURF 			= 100;
	private static final int U_DEGREE_NURBS 	= 3;
	private static final int V_DEGREE_NURBS 	= 3;
	
	private ArrayList<Surface3D> 	surfaces;
	private GLFrame 				frame;
	private ListPanel				list;
	private Surface3D				highlight;
	
	public SpaceModel(GLFrame frame, ListPanel panel) {
		this.frame = frame;
		surfaces = new ArrayList<>();
		list = panel;
	}
	
	public void addSurface(Surface3D s) {
		surfaces.add(s);
		list.addSurface(surfaces.get(surfaces.size() - 1));
		frame.pushStructure(s.triangulate());
	}
	
	public void removeSurface(String label) {
		int index = 0;
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				if (surfaces.get(i).equals(highlight)) {
					frame.highlight(null);
				}
				surfaces.remove(i);
				index = i;
				break;
			}
		}
		frame.removeStructure(index);
	}
	
	public void addNewSurface(String label) {
		boolean held = false;
		switch (label) {
		case UIMenu.FV:
			held = true;
			surfaces.add(new FVPolygonMesh());
			break;
		case UIMenu.NURBS:
			held = true;
			surfaces.add(new NURBS(U_DEGREE_NURBS, V_DEGREE_NURBS));
			break;
		case UIMenu.SPIRAL:
			held = true;
			surfaces.add(new Spiral(1.0, 2.0, 0.0, 1.0, STEP_SURF, STEP_SURF));
			break;
		case UIMenu.TORUS:
			held = true;
			surfaces.add(new Torus(0.0, 2 * Math.PI, 0.0, 2*Math.PI, 
					STEP_SURF, STEP_SURF, 30, 10));
			break;
		}
		if (held) {
			list.addSurface(surfaces.get(surfaces.size() - 1));
			frame.pushStructure(surfaces.get(surfaces.size() - 1).triangulate());	
		}
	}
	
	public ArrayList<FVPolygonMesh> getMeshList() {
		ArrayList<FVPolygonMesh> tmp = new ArrayList<>();
		for (Surface3D s : surfaces) {
			if (s instanceof FVPolygonMesh) {
				tmp.add((FVPolygonMesh) s);
			}
		}
		return tmp;
	}
	
	public String getSurfaceInfo(String label) {
		String info = "";
		for (Surface3D s : surfaces) {
			if (s.getLabel().equals(label)) {
				info = s.toString();
				break;
			}
		}
		return info;
	}
	
	public boolean isEditable(String label) {
		boolean ans = false;
		for (Surface3D s : surfaces) {
			if (s.getLabel().equals(label) && s instanceof EditableSurface) {
				ans = true;
				break;
			}
		}
		return ans;
	}
	
	public void rotate(String label, String flag, double phi) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				if (flag.contains("X")) {
					SurfaceUtilities.rotateX(sE, phi);
				}
				else if (flag.contains("Y")) {
					SurfaceUtilities.rotateY(sE, phi);
				}
				else if (flag.contains("Z")) {
					SurfaceUtilities.rotateZ(sE, phi);
				}
				frame.replaceStructure(i , surfaces.get(i).triangulate());
			}
		}
	}
	
	public void translate(String label, String flag, double delta) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				if (flag.contains("X")) {
					SurfaceUtilities.translateX(sE, delta);
				}
				else if (flag.contains("Y")) {
					SurfaceUtilities.translateY(sE, delta);
				}
				else if (flag.contains("Z")) {
					SurfaceUtilities.translateZ(sE, delta);
				}
				frame.replaceStructure(i , surfaces.get(i).triangulate());
			}
		}
	}
	
	public void addVertex(double x, double y, double z, String label) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				sE.addVertex(new Point3D(x, y, z));
				frame.replaceStructure(i , surfaces.get(i).triangulate());
			}
		}
	}
	
	public void higlightStructure(String label) {
		if (label != null) {
			for (int i = 0; i < surfaces.size(); i++) {
				if (surfaces.get(i).getLabel().equals(label)) {
					highlight = surfaces.get(i);
					frame.highlight(surfaces.get(i).vertices());
				}
			}
		} else {
			frame.highlight(null);
		}
	}
	
	public boolean isNURBS(String label) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				if (surfaces.get(i) instanceof NURBS) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
}

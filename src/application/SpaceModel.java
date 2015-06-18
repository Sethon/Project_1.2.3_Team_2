package application;

import gui.GLFrame;
import gui.ListPanel;
import gui.UIMenu;

import java.util.ArrayList;

import surfaces.*;

public class SpaceModel {

	private static final int STEP_SURF 			= 100;
	private static final int U_DEGREE_NURBS 	= 3;
	private static final int V_DEGREE_NURBS 	= 3;
	private static final int CLARK_ITERATIONS 	= 1;

	private ArrayList<Surface3D> surfaces;
	private GLFrame frame;
	private ListPanel list;
	private Surface3D highlight;

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
//			WPoint3D[][] p = new WPoint3D[4][4];
//			p[0][0] = new WPoint3D(0, 0, 1, 1);
//			p[0][1] = new WPoint3D(0, 0, 1, 1 / 3.0);
//			p[0][2] = new WPoint3D(0, 0, 1, 1 / 3.0);
//			p[0][3] = new WPoint3D(0, 0, 1, 1);
//			p[1][0] = new WPoint3D(2, 0, 1, 1 / 3.0);
//			p[1][1] = new WPoint3D(2, 4, 1, 1 / 9.0);
//			p[1][2] = new WPoint3D(-2, 4, 1, 1 / 9.0);
//			p[1][3] = new WPoint3D(-2, 0, 1, 1 / 3.0);
//			p[2][0] = new WPoint3D(2, 0, -1, 1 / 3.0);
//			p[2][1] = new WPoint3D(2, 4, -1, 1 / 9.0);
//			p[2][2] = new WPoint3D(-2, 4, -1, 1 / 9.0);
//			p[2][3] = new WPoint3D(-2, 0, -1, 1 / 3.0);
//			p[3][0] = new WPoint3D(0, 0, -1, 1);
//			p[3][1] = new WPoint3D(0, 0, -1, 1 / 3.0);
//			p[3][2] = new WPoint3D(0, 0, -1, 1 / 3.0);
//			p[3][3] = new WPoint3D(0, 0, -1, 1);
//			double[] knots = {0, 0, 0, 0, 1, 1, 1, 1};
//			surfaces.add(new NURBS(3, 3, p, knots, knots));
               surfaces.add(new NURBS(3,3));
			break;
		case UIMenu.SPIRAL:
			held = true;
			surfaces.add(new Spiral(1.0, 2.0, 0.0, 1.0, STEP_SURF, STEP_SURF));
			break;
		case UIMenu.TORUS:
			held = true;
			surfaces.add(new Torus(0.0, 2 * Math.PI, 0.0, 2 * Math.PI,
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
	
	public ArrayList<NURBS> getNURBSList() {
		ArrayList<NURBS> tmp = new ArrayList<>();
		for (Surface3D s : surfaces) {
			if (s instanceof NURBS) {
				tmp.add((NURBS) s);
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
				} else if (flag.contains("Y")) {
					SurfaceUtilities.rotateY(sE, phi);
				} else if (flag.contains("Z")) {
					SurfaceUtilities.rotateZ(sE, phi);
				}
				frame.replaceStructure(i, surfaces.get(i).triangulate());
			}
		}
	}

	public void translate(String label, String flag, double delta) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				if (flag.contains("X")) {
					SurfaceUtilities.translateX(sE, delta);
				} else if (flag.contains("Y")) {
					SurfaceUtilities.translateY(sE, delta);
				} else if (flag.contains("Z")) {
					SurfaceUtilities.translateZ(sE, delta);
				}
				frame.replaceStructure(i, surfaces.get(i).triangulate());
			}
		}
	}
	
	public String clarkinate(String label) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				FVPolygonMesh sE = (FVPolygonMesh) surfaces.get(i);
				FVPolygonMesh tmp = SurfaceUtilities.applyCCsubDivision(sE, CLARK_ITERATIONS);
				surfaces.remove(i);
				surfaces.add(i, tmp);
				frame.replaceStructure(i, surfaces.get(i).triangulate());
				return tmp.getLabel();
			}
		}
		return null;
	}

	public void addVertex(double x, double y, double z, String label, String instruct) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				sE.addVertex(new Point3D(x, y, z), instruct);
				frame.replaceStructure(i, surfaces.get(i).triangulate());
			}
		}
	}

	public void add3Vertices(double x1, double y1, double z1, double x2, double y2, 
			double z2, double x3, double y3, double z3, 
			String label, String instruct, double w) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				sE.add3Vertices(new Point3D(x1, y1, z1), new Point3D(x2, y2, z2),
						new Point3D(x3, y3, z3), instruct, w);
				frame.replaceStructure(i, surfaces.get(i).triangulate());
			}
		}
	}

	public void add2Vertices(double x1, double y1, double z1, double x2, double y2, 
			double z2, String label, String instruct, double w) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				sE.add2Vertices(new Point3D(x1, y1, z1), new Point3D(x2, y2, z2), instruct, w);
				frame.replaceStructure(i, surfaces.get(i).triangulate());
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
	
	public boolean isFVPolyMesh(String label) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				if (surfaces.get(i) instanceof FVPolygonMesh) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
}

package application;

import gui.GLFrame;
import gui.ListPanel;
import gui.UIMenu;

import java.util.ArrayList;

import surfaces.*;

/**
 * Class for representing the model of the 3D space containing various
 * surfaces
 */
public class SpaceModel {

	private static final int STEP_SURF 			= 100;
	private static final int U_DEGREE_NURBS 	= 3;
	private static final int V_DEGREE_NURBS 	= 3;
	private static final int CLARK_ITERATIONS 	= 1;

	private ArrayList<Surface3D> surfaces;
	private GLFrame frame;
	private ListPanel list;
	private Surface3D highlight;

	/**
	 * Constructs a new spatial model
	 * 
	 * @param frame Frame containing GLCanvas
	 * @param panel Panel controlling the list of surfaces
	 */
	public SpaceModel(GLFrame frame, ListPanel panel) {
		this.frame = frame;
		surfaces = new ArrayList<>();
		list = panel;
	}
	
	/**
	 * Adds a new surface to the model
	 * 
	 * @param s Surface
	 */
	public void addSurface(Surface3D s) {
		surfaces.add(s);
		list.addSurface(surfaces.get(surfaces.size() - 1));
		frame.pushStructure(s.triangulate());
	}

	/**
	 * Removes surface specified by a label
	 * 
	 * @param label Surface label
	 */
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

	/**
	 * Adds new surface specified the type of which is specified by a label
	 * 
	 * @param label Surface type label
	 */
	public void addNewSurface(String label) {
		boolean held = false;
		switch (label) {
		case UIMenu.FV:
			held = true;
			surfaces.add(new FVPolygonMesh());
			break;
		case UIMenu.NURBS:
			held = true;
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

	/**
	 * Getter for the list of FV Polygon Meshes currently contained in the model
	 * 
	 * @return Returns the list of meshes
	 */
	public ArrayList<FVPolygonMesh> getMeshList() {
		ArrayList<FVPolygonMesh> tmp = new ArrayList<>();
		for (Surface3D s : surfaces) {
			if (s instanceof FVPolygonMesh) {
				tmp.add((FVPolygonMesh) s);
			}
		}
		return tmp;
	}
	
	/**
	 * Getter for the list of NURBS surfaces currently contained in the model
	 * 
	 * @return Returns the list of NURBS
	 */
	public ArrayList<NURBS> getNURBSList() {
		ArrayList<NURBS> tmp = new ArrayList<>();
		for (Surface3D s : surfaces) {
			if (s instanceof NURBS) {
				tmp.add((NURBS) s);
			}
		}
		return tmp;
	}

	/**
	 * Method for accessing the information on a surface specified by the label
	 * 
	 * @param label Surface label
	 * 
	 * @return Returns a string with information on the surface
	 */
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

	/**
	 * Method for identifying if a surface specified by the label is editable 
	 * 
	 * @param label Surface label
	 * 
	 * @return Returns true if surface is editable and false otherwise
	 */
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

	/**
	 * Method for rotating surface specified by the label
	 * 
	 * @param label Surface label
	 * @param flag Rotation axis
	 * @param phi Rotation angle
	 */
	public void rotate(String label, String flag, double phi) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				Surface3D sE = surfaces.get(i);
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

	/**
	 * Method for translating surface specified by the label
	 * 
	 * @param label Surface label
	 * @param flag Translation axis
	 * @param delta Translation distance
	 */
	public void translate(String label, String flag, double delta) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				Surface3D sE = surfaces.get(i);
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
	
	/**
	 * Method for applying Catmull-Clark algorithm to mesh specified by the
	 * label
	 * 
	 * @param label Mesh label
	 * 
	 * @return Returns the label of a newly formed mesh
	 */
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

	/**
	 * Method for adding a new vertex to editable surface specified by the label
	 * 
	 * @param x X-coordinate of a vertex
	 * @param y Y-coordinate of a vertex
	 * @param z Z-coordinate of a vertex
	 * @param label Surface label
	 * @param instruct Specifications for the insertion
	 */
	public void addVertex(double x, double y, double z, String label, String instruct) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				sE.addVertex(new Point3D(x, y, z), instruct);
				frame.replaceStructure(i, surfaces.get(i).triangulate());
			}
		}
	}

	/**
	 * Method for adding three new vertex to editable surface specified by the label
	 * 
	 * @param x1 X-coordinate of the first vertex
	 * @param y1 Y-coordinate of the first vertex
	 * @param z1 Z-coordinate of the first vertex
	 * @param x2 X-coordinate of the second vertex
	 * @param y2 Y-coordinate of the second vertex
	 * @param z2 Z-coordinate of the second vertex
	 * @param x3 X-coordinate of the third vertex
	 * @param y3 Y-coordinate of the third vertex
	 * @param z3 Z-coordinate of the third vertex
	 * @param label Surface label
	 * @param instruct Specifications for the insertion
	 * @param w Weight
	 */
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

	/**
	 * Method for adding three new vertex to editable surface specified by the label
	 * 
	 * @param x1 X-coordinate of the first vertex
	 * @param y1 Y-coordinate of the first vertex
	 * @param z1 Z-coordinate of the first vertex
	 * @param x2 X-coordinate of the second vertex
	 * @param y2 Y-coordinate of the second vertex
	 * @param z2 Z-coordinate of the second vertex
	 * @param label Surface label
	 * @param instruct Specifications for the insertion
	 * @param w Weight
	 */
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

	/**
	 * Method for highlighting surface specified by the label 
	 * 
	 * @param label Surface label
	 */
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

	/**
	 * Method for identifying if a surface specified by the label is a NURBS 
	 * 
	 * @param label Surface label
	 * 
	 * @return Returns true if surface is a NURBS and false otherwise
	 */
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
	
	/**
	 * Method for identifying if a surface specified by the label is an FV mesh 
	 * 
	 * @param label Surface label
	 * 
	 * @return Returns true if surface is an FV Mesh and false otherwise
	 */
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
	
	/**
	 * Method for retrieving an editable surface specified by the label 
	 * 
	 * @param label Surface label
	 * 
	 * @return Returns specified editable surface (potentially null)
	 */
	public EditableSurface getEditableSurface(String label) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				if (surfaces.get(i) instanceof EditableSurface) {
					return (EditableSurface) surfaces.get(i);
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * Method for updating surface specified by the label 
	 * 
	 * @param label Surface label
	 */
	public void updateStructure(String label) {
		for (int i = 0; i < surfaces.size(); i++) {
			if (surfaces.get(i).getLabel().equals(label)) {
				EditableSurface sE = (EditableSurface) surfaces.get(i);
				if (sE instanceof NURBS) {
					frame.replaceStructure(i, surfaces.get(i).triangulate());
					return;
				}
			}
		}
	}
}

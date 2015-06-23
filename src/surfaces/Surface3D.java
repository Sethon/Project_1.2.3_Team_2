package surfaces;

import java.util.ArrayList;
/**
 * Generic class for editing surfaces in 3D.
 */
public abstract class Surface3D implements Labelizable{
	protected static int 	instCnt = 0;
	private String 			labelID;
	
	/**
	 * Returns the vertices of the surface.
	 * @return arraylist containing the vertices.
	 */
	public abstract ArrayList<Point3D> vertices();
	
	/**
	 * Make and return the faces.
	 * @return arraylist containing the faces.
	 */
	public abstract ArrayList<Triangle3D> triangulate();
	
	/**
	 * Calculate the surface area of the surface.
	 * @return the surface area.
	 */
	public abstract double surfaceArea();
	
	/**
	 * Assign a label to a surface model.
	 */
	public void assignLabel() {
		labelID = this.getClass().getName() + "_" + instCnt;
	}
	
	/**
	 * Get the label of a surface model.
	 * @return the label of the model.
	 */
	public String getLabel() {
		return labelID;
	}
}

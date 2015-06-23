package surfaces;

import java.util.ArrayList;

/**
 *Abstract class which is needed for all of the different Polygon Meshes.
 */
public abstract class PolygonMesh extends Surface3D implements EditableSurface {
	protected ArrayList<Point3D> vertices;
	
	/**
	 * Returns all the vertices of the Polygon Meshes.
	 * @return the vertices.
	 */
	@Override
	public ArrayList<Point3D> vertices() {
		return vertices;
	}
}

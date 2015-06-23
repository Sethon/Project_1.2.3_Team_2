package surfaces;

/**
 * Interface which defines the methods for dynamically changeable surfaces.
 */
public interface EditableSurface {
	
	/**
	 * Adds one vertex to the current model.
	 * @param p Point to add to the vertices.
	 * @param instruct instruction for the vertex
	 */
	public void addVertex(Point3D p, String instruct);
	
	/**
	 * Adds two vertices to the current model.
	 * @param p1 First point to add to the vertices.
	 * @param p2 Second point to add to the vertices.
	 * @param instruct instruction for the vertex
	 * @param w Weight for the vertices.
	 */
	public void add2Vertices(Point3D p1, Point3D p2, String instruct, double w);
	
	/**
	 * Adds two vertices to the current model.
	 * @param p1 First point to add to the vertices.
	 * @param p2 Second point to add to the vertices.
	 * @param p3 Third point to add to the vertices.
	 * @param instruct instruction for the vertex
	 * @param w Weight for the vertices.
	 */
	public void add3Vertices(Point3D p1, Point3D p2, Point3D p3, 
			String instruct, double w);
}

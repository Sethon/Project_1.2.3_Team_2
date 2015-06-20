package surfaces;

public interface EditableSurface {
	public void addVertex(Point3D p, String instruct);
	public void add2Vertices(Point3D p1, Point3D p2, String instruct, double w);
	public void add3Vertices(Point3D p1, Point3D p2, Point3D p3, 
			String instruct, double w);
}

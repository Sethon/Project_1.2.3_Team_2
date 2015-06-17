package surfaces;

public abstract class EditableSurface extends Surface3D {
	public abstract void addVertex(Point3D p, String instruct);
	public abstract void add2Vertices(Point3D p1, Point3D p2, String instruct, double w);
	public abstract void add3Vertices(Point3D p1, Point3D p2, Point3D p3, 
			String instruct, double w);
}

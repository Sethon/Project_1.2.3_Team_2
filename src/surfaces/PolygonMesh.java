package surfaces;

import java.util.ArrayList;

public abstract class PolygonMesh extends EditableSurface {
	protected ArrayList<Point3D> vertices;
	
	@Override
	public ArrayList<Point3D> vertices() {
		return vertices;
	}

	@Override
	public abstract ArrayList<Triangle3D> triangulate();
	
	@Override
	public abstract void addVertex(Point3D p, String instruct);
}

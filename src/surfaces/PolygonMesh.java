package surfaces;

import java.util.ArrayList;

public abstract class PolygonMesh extends Surface3D implements EditableSurface {
	protected ArrayList<Point3D> vertices;
	
	@Override
	public ArrayList<Point3D> vertices() {
		return vertices;
	}
}

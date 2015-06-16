package surfaces;

import java.util.ArrayList;

public abstract class Surface3D implements Labelizable{
	protected static int 	instCnt = 0;
	private String 			labelID;
	
	public abstract ArrayList<Point3D> vertices();
	public abstract ArrayList<Triangle3D> triangulate();
	public abstract double surfaceArea();
	
	public void assignLabel() {
		labelID = this.getClass().getName() + "_" + instCnt;
	}
	
	public String getLabel() {
		return labelID;
	}
}

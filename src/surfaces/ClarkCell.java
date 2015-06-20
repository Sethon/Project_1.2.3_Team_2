package surfaces;

public class ClarkCell {

	public Point3D facePoint;
	public Point3D v1;
	public Point3D v2;
	public Point3D v3;
	
	public int v1Val;
	public int v2Val;
	public int v3Val;
	
	public Point3D edgePoint12;
	public Point3D edgePoint23;
	public Point3D edgePoint13;
	
	public ClarkCell() {
		
	}
	
	public ClarkCell(Point3D p1, Point3D p2, Point3D p3, int i1, int i2, int i3) {
		v1 = p1;
		v2 = p2;
		v3 = p3;
		v1Val = i1;
		v2Val = i2;
		v3Val = i3;
	}
	
	public void setFacePoint(Point3D p) {
		facePoint = p;
	}
	
	public void setV1(Point3D p) {
		v1 = p;
	}
	
	public void setV2(Point3D p) {
		v2 = p;
	}
	
	public void setV3(Point3D p) {
		v3 = p;
	}
	
	public void setv1Val(int i) {
		v1Val = i;
	}
	
	public void setv2Val(int i) {
		v2Val = i;	
	}

	public void setv3Val(int i) {
		v3Val = i;
	}

	public void setEdgePoint12(Point3D p) {
		edgePoint12 = p;
	}
	
	public void setEdgePoint23(Point3D p) {
		edgePoint23 = p;
	}
	
	public void setEdgePoint13(Point3D p) {
		edgePoint13 = p;
	}
	
	public Point3D getFacePoint() {
		return facePoint;
	}
	
	public Point3D getV1() {
		return v1;
	}
	
	public Point3D getV2() {
		return v2;
	}
	
	public Point3D getV3() {
		return v3;
	}
	
	public int getv1Val() {
		return v1Val;
	}
	
	public int getv2Val() {
		return v2Val;	
	}

	public int getv3Val() {
		return v3Val;
	}

	public Point3D getEdgePoint12() {
		return edgePoint12;
	}
	
	public Point3D getEdgePoint23() {
		return edgePoint23;
	}
	
	public Point3D getEdgePoint13() {
		return edgePoint13;
	}
}

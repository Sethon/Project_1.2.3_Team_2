package surfaces;

import java.util.ArrayList;


public class Triangle3D extends Surface3D {
	private Point3D p1;
	private Point3D p2;
	private Point3D p3;
	
	public Triangle3D(Point3D p1, Point3D p2, Point3D p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	@Override
	public ArrayList<Point3D> vertices() {
		ArrayList<Point3D> tmp = new ArrayList<>();
		tmp.add(p1);
		tmp.add(p2);
		tmp.add(p3);
		return tmp;
	}

	@Override
	public ArrayList<Triangle3D> triangulate() {
		ArrayList<Triangle3D> tmp = new ArrayList<>();
		tmp.add(this);
		return tmp;
	}
	
	public double surfaceArea(){
		
		double u = p1.dist(p2);
		double v = p1.dist(p3);
		double w = p2.dist(p3);
		
		double s = 0.5*(u+v+w);
		
		return Math.sqrt(s*(s-u)*(s-v)*(s-w));
	}
	
	@Override
	public String toString() {
		return "[" + p1.toString() + "; " + p2.toString() + "; " + p3.toString() + "]";
	}
}


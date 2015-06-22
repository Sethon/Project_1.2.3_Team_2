package surfaces;

import java.util.ArrayList;
import java.util.Collections;


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
		ArrayList<Double> edges = new ArrayList<>();
		edges.add(p1.dist(p2));
		edges.add(p1.dist(p3));
		edges.add(p2.dist(p3));
		Collections.sort(edges);
	
		double u = edges.get(2);
		double v = edges.get(1);
		double w = edges.get(0);
		
		return 0.25 * Math.sqrt((u + (v + w))*(w - (u - v))*(w + (u - v))*(u + (v - w)));
	}
	
	@Override
	public String toString() {
		return "[" + p1.toString() + "; " + p2.toString() + "; " + p3.toString() + "]";
	}
}


package surfaces;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Creates 3D triangle objects which consist out of 3 points.
 */
public class Triangle3D extends Surface3D {
	private Point3D p1;
	private Point3D p2;
	private Point3D p3;
	
	/**
	 * Instantiator which assigns the three inputted points and creates a new triangle.
	 * @param p1 The first point of the triangle.
	 * @param p2 The second point of the triangle.
	 * @param p3 The third point of the triangle.
	 */
	public Triangle3D(Point3D p1, Point3D p2, Point3D p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	/**
	 * Returns an arraylist containing all three the points.
	 * @return the arraylist containing the points.
	 */
	@Override
	public ArrayList<Point3D> vertices() {
		ArrayList<Point3D> tmp = new ArrayList<>();
		tmp.add(p1);
		tmp.add(p2);
		tmp.add(p3);
		return tmp;
	}

	/**
	 * Returns an arraylist containing the current triangle.
	 * @return the arraylist containing the triangle.
	 */
	@Override
	public ArrayList<Triangle3D> triangulate() {
		ArrayList<Triangle3D> tmp = new ArrayList<>();
		tmp.add(this);
		return tmp;
	}
	
	/**
	 * Calculates the surface area of the current triangle.
	 * @return the surface area.
	 */
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
	
	/**
	 * Returns a string containing the coordinates of the current points of the triangle.
	 * @return string containing coordinates.
	 */
	@Override
	public String toString() {
		return "[" + p1.toString() + "; " + p2.toString() + "; " + p3.toString() + "]";
	}
}


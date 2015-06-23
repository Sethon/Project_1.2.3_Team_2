package surfaces;

import java.util.ArrayList;

/**
 * Creates tetrahedron objects containing 4 points.
 */
public class Tetrahedron extends Surface3D {
	private Point3D p1;
	private Point3D p2;
	private Point3D p3;
	private Point3D p4;
	
	/**
	 * Instantiator which creates the new tetrahedron object with the given points.
	 * @param v1 The first point of the tetrahedron.
	 * @param v2 The second point of the tetrahedron.
	 * @param v3 The third point of the tetrahedron.
	 * @param v4 The fourth point of the tetrahedron.
	 */
	public Tetrahedron(Point3D v1, Point3D v2, Point3D v3, Point3D v4) {
		p1 = v1;
		p2 = v2;
		p3 = v3;
		p4 = v4;
	}
	
	/**
	 * Returns an arraylist containing the points of the tetrahedron.
	 * @return arraylist containing the points.
	 */
	@Override
	public ArrayList<Point3D> vertices() {
		ArrayList<Point3D> tetra = new ArrayList<>();
		tetra.add(p1);
		tetra.add(p2);
		tetra.add(p3);
		tetra.add(p4);
		
		return tetra;
	}

	/**
	 * Returns an arraylist containing the four triangles the tetrahedron consists of.
	 * @return arraylist containing the triangles.
	 */
	@Override
	public ArrayList<Triangle3D> triangulate() {
		Triangle3D t1 = new Triangle3D(p1,p2,p3);
		Triangle3D t2 = new Triangle3D(p1,p2,p4);
		Triangle3D t3 = new Triangle3D(p1,p3,p4);
		Triangle3D t4 = new Triangle3D(p2,p3,p4);
		ArrayList<Triangle3D> tetra = new ArrayList<>();
		tetra.add(t1);
		tetra.add(t2);
		tetra.add(t3);
		tetra.add(t4);
		return tetra;
	}
	
	/**
	 * Calculates the surface area of the tetrahedron.
	 * @return the surface area.
	 */
	public double surfaceArea(){
		Triangle3D t1 = new Triangle3D(p1,p2,p3);
		Triangle3D t2 = new Triangle3D(p1,p2,p4);
		Triangle3D t3 = new Triangle3D(p1,p3,p4);
		Triangle3D t4 = new Triangle3D(p2,p3,p4);
		
		return (t1.surfaceArea() + t2.surfaceArea() + t3.surfaceArea() + t4.surfaceArea());
	}
	
	/**
	 * Calculates the volume of the tetrahedron.
	 * @param signed boolean which depicts the state of being signed or not.
	 * @return the volume of the tetrahedron.
	 */
	public double volume(boolean signed){
		double a = p1.getX();
		double b = p1.getY();
		double c = p1.getZ();
		
		double e = p2.getX();
		double f = p2.getY();
		double g = p2.getZ();
		
		double i = p3.getX();
		double j = p3.getY();
		double k = p3.getZ();
		
		double o = p4.getX();
		double m = p4.getY();
		double n = p4.getZ();
		
		double signedVol = (1.0/6.0) * (n * (-a * f + (a - e) * j + i * (f - b) + e * b)
				+ k * (a * f + o * (b - f) - e * b) + m * (a * g + (e - a) * k + i * (c - g) - e * c)
				+ j * (e * c - a * g) + i * (b * g - c * f) + o * (-b * g + c * f + j * (g - c)));
		
		if (signed) {
			return signedVol;
		} else {
			return Math.abs(signedVol);
		}
	}
}

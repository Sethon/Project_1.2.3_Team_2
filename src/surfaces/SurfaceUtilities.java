package surfaces;

import java.util.ArrayList;


public abstract class SurfaceUtilities {
	public static void rotateX(Surface3D s, double phi) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			double x = v.getX();
			double y = Math.cos(phi)*v.getY() +  Math.sin(phi)*v.getZ();
			double z = -Math.sin(phi)*v.getY() + Math.cos(phi)*v.getZ();
			v.setX(x);
			v.setY(y);
			v.setZ(z);
		}
	}
	
	public static void rotateY(Surface3D s, double phi) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			double x = Math.cos(phi)*v.getX() - Math.sin(phi)*v.getZ();
			double y = v.getY();
			double z = Math.sin(phi)*v.getX() + Math.cos(phi)*v.getZ();
			v.setX(x);
			v.setY(y);
			v.setZ(z);
		}
	}
	
	public static void rotateZ(Surface3D s, double phi) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			double x = Math.cos(phi)*v.getX() + Math.sin(phi)*v.getY();
			double y = -Math.sin(phi)*v.getX() + Math.cos(phi)*v.getY();
			double z = v.getZ();
			v.setX(x);
			v.setY(y);
			v.setZ(z);
		}
	}
	
	public static void translateX(Surface3D s, double delta) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			v.setX(v.getX() + delta);
		}
	}
	
	public static void translateY(Surface3D s, double delta) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			v.setY(v.getY() + delta);
		}
	}
	
	public static void translateZ(Surface3D s, double delta) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			v.setZ(v.getZ() + delta);
		}
	}
}

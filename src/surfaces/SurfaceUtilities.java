package surfaces;

import java.util.ArrayList;

/**
 * Special pure fabrication class for spatial manipulating for surfaces. 
 */
public abstract class SurfaceUtilities {
	
	/**
	 * Rotates the model around the x-axis.
	 * @param s the model.
	 * @param phi degree the model should be rotated.
	 */
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

	/**
	 * Rotates the model around the y-axis.
	 * @param s the model.
	 * @param phi degree the model should be rotated.
	 */
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

	/**
	 * Rotates the model around the z-axis.
	 * @param s the model.
	 * @param phi degree the model should be rotated.
	 */
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

	/**
	 * Translate the model over the x-axis.
	 * @param s the model.
	 * @param delta amount of translation in the x-direction.
	 */
	public static void translateX(Surface3D s, double delta) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			v.setX(v.getX() + delta);
		}
	}

	/**
	 * Translate the model over the y-axis.
	 * @param s the model.
	 * @param delta amount of translation in the y-direction.
	 */
	public static void translateY(Surface3D s, double delta) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			v.setY(v.getY() + delta);
		}
	}

	/**
	 * Translate the model over the z-axis.
	 * @param s the model.
	 * @param delta amount of translation in the z-direction.
	 */
	public static void translateZ(Surface3D s, double delta) {
		ArrayList<Point3D> vertices = s.vertices();
		for (Point3D v : vertices) {
			v.setZ(v.getZ() + delta);
		}
	}
	
	/**
	 * Applies the Catmull-Clark subdivision algorithm to the given 
	 * FVPolygonMesh with a certain amount of iterations.
	 * @param pm The FVPolygonMesh.
	 * @param iterations Amount of iterations.
	 * @return Newly formed FVPolygonMesh.
	 */
	public static FVPolygonMesh applyCCsubDivision(FVPolygonMesh pm, int iterations) {
		Clarkinator callumClark = new Clarkinator(pm);
		callumClark.subdivide(iterations);
		FVPolygonMesh ans = callumClark.getMesh();
		return ans;
	}
}

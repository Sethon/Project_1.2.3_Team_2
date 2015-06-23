package surfaces;

/**
 * Creates 3D coordinate objects which are used as vertices in the models
 */
public class Point3D {
	private double x;
	private double y;
	private double z;
	
	/**
	 * Instantiator which creates a new 3D point.
	 * @param x x-coordinate of the point.
	 * @param y y-coordinate of the point.
	 * @param z z-coordinate of the point.
	 */
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Returns the x-coordinate of the point.
	 * @return the x-coordinate.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y-coordinate of the point.
	 * @return the y-coordinate.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the z-coordinate of the point.
	 * @return the z-coordinate.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns the coordinates of the point.
	 * @return the set of coordinates.
	 */
	public double[] coordinates() {
		return new double[] {x, y, z};
	}
	
	/**
	 * Sets the current x-coordinate to the new given 
	 * x-coordinate.
	 * @param x the new x-coordinate.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the current y-coordinate to the new given 
	 * y-coordinate.
	 * @param y the new y-coordinate.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets the current z-coordinate to the new given 
	 * z-coordinate.
	 * @param z the new z-coordinate.
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Returns the coordinates in a String form.
	 * @return String containing the coordinates.
	 */
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
	}
	
	/**
	 * Checks if two points are equal to each other
	 * with a difference of maximally 10E-14.
	 * @return True if the points are equal to eachother, else return false.
	 */
	public boolean equals(Object o) {
		Point3D p3d = (Point3D) o;
		return (p3d.getX() <= x + 10E-14) && (p3d.getX() >= x - 10E-14) && 
				(p3d.getY() <= y + 10E-14) && (p3d.getY() >= y - 10E-14) && 
				(p3d.getZ() <= z + 10E-14) && (p3d.getZ() >= z - 10E-14);
	}

	/**
	 * Subtracts the given coordinates from the current coordinates.
	 * @param x the x-coordinate which needs to be subtracted.
	 * @param y the y-coordinate which needs to be subtracted.
	 * @param z the z-coordinate which needs to be subtracted.
	 * @return the newly generated point with subtracted coordinates.
	 */
	public Point3D subtract(double x, double y, double z) {
		return new Point3D(
							    getX() - x,
							    getY() - y,
							    getZ() - z);
	}

	/**
	 * Uses another points coordinates to subtract these from the current coordinates.
	 * @param point the point which needs to be subtracted from the current point.
	 * @return the newly generated point with subtracted coordinates.
	 */
	public Point3D subtract(Point3D point) {
		return subtract(point.getX(), point.getY(), point.getZ());
	}

	/**
	 * Produces a new point with the same coordinates as the current point.
	 * @return the newly cloned point.
	 */
	public Point3D clone() {
		return new Point3D(x, y, z);
	}
	
	/**
	 * Calculates the distance between the current point and
	 * the given point.
	 * @param p the point which needs the distance to the current.
	 * @return the distance to the given point.
	 */
	public double dist(Point3D p) {
		return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2) + Math.pow(p.getZ() - z, 2));
	}
}

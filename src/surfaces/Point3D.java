package surfaces;


public class Point3D {
	private double x;
	private double y;
	private double z;
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public double[] coordinates() {
		return new double[] {x, y, z};
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public String toString() {
		return "(" + x + "; " + y + "; " + z + ")";
	}
	
	public boolean equals(Object o) {
		Point3D p3d = (Point3D) o;
		return (p3d.getX() <= x + 10E-14) && (p3d.getX() >= x - 10E-14) && 
				(p3d.getY() <= y + 10E-14) && (p3d.getY() >= y - 10E-14) && 
				(p3d.getZ() <= z + 10E-14) && (p3d.getZ() >= z - 10E-14);
	}
	
	public Point3D clone() {
		return new Point3D(x, y, z);
	}
	
	public double dist(Point3D p) {
		return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2) + Math.pow(p.getZ() - z, 2));
	}
}

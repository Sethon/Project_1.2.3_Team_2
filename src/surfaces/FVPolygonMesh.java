package surfaces;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class FVPolygonMesh extends PolygonMesh {

	private static boolean 						simplicityAssumption 	= false;
	
	private ArrayList<Triangle3D> 				faces;
	private ArrayList<ArrayList<Triangle3D>> 	verticesToFaces;
	
	public FVPolygonMesh(ArrayList<Point3D> vertices, ArrayList<Triangle3D> faces,
			ArrayList<ArrayList<Triangle3D>> verticesToFaces) {
		instCnt++;
		assignLabel();
		super.vertices = vertices;
		this.faces = faces;
		this.verticesToFaces = verticesToFaces;
	}
	
	public FVPolygonMesh() {
		instCnt++;
		assignLabel();
		vertices = new ArrayList<>();
		faces = new ArrayList<>();
		verticesToFaces = new ArrayList<ArrayList<Triangle3D>>();
	}
	
	@Override
	public ArrayList<Triangle3D> triangulate() {
		ArrayList<Triangle3D> tmp = new ArrayList<>();
		if (vertices.size() == 1) {
			tmp.add(new Triangle3D(vertices.get(0), vertices.get(0), vertices.get(0)));
			return tmp;
		}
		else if (vertices.size() == 2) {
			tmp.add(new Triangle3D(vertices.get(0), vertices.get(1), vertices.get(0)));
			return tmp;
		} else {
			return faces;
		}
	}

	@Override
	public void addVertex(Point3D p, String instruct) {
		if (vertices.size() <= 1) {
			vertices.add(p);
			verticesToFaces.add(new ArrayList<>());
		} else {
			int[] tmp = find2ClosestPs(p);
			vertices.add(p);
			Triangle3D newFace = new Triangle3D(vertices.get(tmp[0]), 
					 p, vertices.get(tmp[1]));
			faces.add(newFace);
			verticesToFaces.get(tmp[0]).add(newFace);
			verticesToFaces.get(tmp[1]).add(newFace);
			verticesToFaces.add(new ArrayList<>());
			verticesToFaces.get(verticesToFaces.size() - 1).add(newFace);
		}
	}
	
	private int[] find2ClosestPs(Point3D p) {
		Point3D v1 = vertices.get(0);
		Point3D v2 = vertices.get(1);
		int v1Ind = 0;
		int v2Ind = 1;
		
		double diffV1 = Integer.MAX_VALUE;
		double diffV2 = Integer.MAX_VALUE;
		
		for (int i = 0; i < vertices.size(); i++) {
			double tmp = p.dist(vertices.get(i));
			if (tmp <= diffV1) {
				diffV1 = tmp;
				v1 = vertices.get(i);
				v1Ind = i;
			}
		}
		
		for (int i = 0; i < vertices.size(); i++) {
			double tmp = p.dist(vertices.get(i));
			Triangle3D t = new Triangle3D(p, v1, vertices.get(i));
			if (tmp <= diffV2 && !vertices.get(i).equals(v1) && (t.surfaceArea() != 0)) {
				diffV2 = tmp;
				v2 = vertices.get(i);
				v2Ind = i;
			}
		}
		
		ArrayList<Point3D> res1 = new ArrayList<Point3D>();
		res1.add(v1);
		res1.add(v2);
		
		
		return new int[] {v1Ind, v2Ind};
	}
	
	@Override
	public String toString() {
		return "Face-Vertex Polygon Mesh" + "\n" + "Vertices: " + vertices.size() + 
				"\n" + "Faces: " + faces.size() + "\n" + "Surface area: " + surfaceArea() + " sq. u." +
				"\n" + "Volume: " + volume() + " cub. u.";
	}

	@Override
	public double surfaceArea() {
		double sum = 0;
		for (Triangle3D t : faces) {
			sum += t.surfaceArea();
		}
		
		return sum;
	}

	public double volume() {
		if (simplicityAssumption) {
			double x0 = 0;
			double y0 = 0;
			double z0 = 0;
			double sumX = 0;
			double sumY = 0;
			double sumZ = 0;
			for (Point3D p : vertices) {
				sumX += p.getX();
				sumY += p.getY();
				sumZ += p.getZ();
			}
			x0 = sumX/vertices.size();
			y0 = sumY/vertices.size();
			z0 = sumZ/vertices.size();
			
			double sum = 0;
			for (Triangle3D t : faces) {
				ArrayList<Point3D> ps = t.vertices();
				Vector3D v1 = new Vector3D(ps.get(0).getX() - x0, ps.get(0).getY() - y0, ps.get(0).getZ() - z0);
				Vector3D v2 = new Vector3D(ps.get(1).getX() - x0, ps.get(1).getY() - y0, ps.get(1).getZ() - z0);
				Vector3D v3 = new Vector3D(ps.get(2).getX() - x0, ps.get(2).getY() - y0, ps.get(2).getZ() - z0);
				double tmp = Math.abs((v1.crossProduct(v2)).dotProduct(v3)) * (1.0/6.0);
				sum += tmp;
			}
			
			return Math.abs(sum);
		} else {
			double sum = 0;
			for (Triangle3D t : faces) {
				ArrayList<Point3D> ps = t.vertices();
				Vector3D v1 = new Vector3D(ps.get(0).getX(), ps.get(0).getY(), ps.get(0).getZ());
				Vector3D v2 = new Vector3D(ps.get(1).getX(), ps.get(1).getY(), ps.get(1).getZ());
				Vector3D v3 = new Vector3D(ps.get(2).getX(), ps.get(2).getY(), ps.get(2).getZ());
				double tmp = Math.abs((v1.crossProduct(v2)).dotProduct(v3)) * (1.0/6.0);
				double sign = v1.dotProduct((v2.subtract(v1)).crossProduct(v3.subtract(v1)));
				if (sign != 0) {
					sign = sign/Math.abs(sign);
				} else {
					sign = 0;
				}
				sum += tmp * sign;
			}
			
			return Math.abs(sum);
		}
	
	}
}

package plyer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import surfaces.FVPolygonMesh;
import surfaces.Point3D;
import surfaces.Triangle3D;

public class PLYWriter {
	public void writeFVMesh(String filename, FVPolygonMesh fvpm) {
		try {
			ArrayList<Point3D> vertices = fvpm.vertices();
			ArrayList<Triangle3D> faces = fvpm.triangulate();
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".ply"));
			writer.write("ply" + "\n" 
					+ "format ascii 1.0" + "\n"
					+ "element vertex " + vertices.size() + "\n"
					+ "property double64 x" + "\n"
					+ "property double64 y" + "\n"
					+ "property double64 z" + "\n"
					+ "element face " + faces.size() + "\n"
					+ "property list int16 int16 vertex_indices" + "\n"
					+ "end_header");
			for (Point3D v : vertices) {
				writer.newLine();
				writer.write(v.getX() + " " + v.getY() + " " + v.getZ());
			}
			
			for (Triangle3D t : faces) {
				ArrayList<Point3D> ps = t.vertices();
				Point3D p1 = ps.get(0);
				Point3D p2 = ps.get(1);
				Point3D p3 = ps.get(2);
				int i1 = 0;
				int i2 = 0;
				int i3 = 0;
				for (int i = 0; i < vertices.size(); i++) {
					if (p1.equals(vertices.get(i))) {
						i1 = i;
					}
					else if (p2.equals(vertices.get(i))) {
						i2 = i;
					}
					else if (p3.equals(vertices.get(i))) {
						i3 = i;
					}
				}
				writer.newLine();
				writer.write("3 " + i1 + " " + i2 + " " + i3);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

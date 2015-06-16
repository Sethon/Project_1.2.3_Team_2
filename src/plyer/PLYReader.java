package plyer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import surfaces.FVPolygonMesh;
import surfaces.Point3D;
import surfaces.Triangle3D;

public class PLYReader {
	private double magnFactor;
	
	public PLYReader(double magnFactor) {
		this.magnFactor = magnFactor;
	}
	
	public FVPolygonMesh getFVMesh(String filename) {
		ArrayList<Point3D> vertices = new ArrayList<>();
		ArrayList<Triangle3D> faces = new ArrayList<>();
		ArrayList<ArrayList<Triangle3D>> verticesToFaces = new ArrayList<ArrayList<Triangle3D>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			int vNum = 0;
			int fNum = 0;
			while (!(line = br.readLine()).equals("end_header")) {
				//System.out.println(line);
				if (line.contains("element vertex")) {
					String[] split = line.split(" ");
					vNum = Integer.parseInt(split[2]);
				}
				else if (line.contains("element face")) {
					String[] split = line.split(" ");
					fNum = Integer.parseInt(split[2]);
				}
			}
			int cnt = 0;
			while (cnt < vNum) {
				line = br.readLine();
				cnt++;
				String[] split = line.split(" ");
				vertices.add(new Point3D(magnFactor * Double.parseDouble(split[0]), magnFactor * Double.parseDouble(split[1]),
						magnFactor * Double.parseDouble(split[2])));
				verticesToFaces.add(new ArrayList<Triangle3D>());
			}
			
			cnt = 0;
			while (cnt < fNum) {
				line = br.readLine();
				cnt++;
				String[] split = line.split(" ");
				if (Integer.parseInt(split[0]) == 3) {
					Point3D p1 = vertices.get(Integer.parseInt(split[1]));
					Point3D p2 = vertices.get(Integer.parseInt(split[2]));
					Point3D p3 = vertices.get(Integer.parseInt(split[3]));
					Triangle3D f = new Triangle3D(p1, p2, p3);
					faces.add(f);
					verticesToFaces.get(Integer.parseInt(split[1])).add(f);
					verticesToFaces.get(Integer.parseInt(split[2])).add(f);
					verticesToFaces.get(Integer.parseInt(split[3])).add(f);
				}
			}
			br.close();
		} catch (Exception e) {
		}
		return new FVPolygonMesh(vertices, faces, verticesToFaces);
	}
}

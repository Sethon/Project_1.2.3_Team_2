package testing;

import plyer.PLYReader;
import surfaces.FVPolygonMesh;

public class FileReadingTest {
	public static void main(String[] args) {
		long time;
		
		PLYReader rd = new PLYReader(1);
		
		long n = System.currentTimeMillis();
		
		FVPolygonMesh pm = rd.getFVMesh("data/dragon_vrip.ply");
		
		long m = System.currentTimeMillis();
				
		time = m-n;
		
		System.out.println("elements:	" + (pm.triangulate().size() + pm.vertices().size()));
		System.out.println("faces:   " + pm.triangulate().size());
		System.out.println("Vertices:   " + pm.vertices().size());
		System.out.println("Time needed:	"+ time);
		
	}
}

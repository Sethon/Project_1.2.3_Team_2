package testing;


import surfaces.Spiral;



public class ConvergenceTest {
	public static void main(String[] args) {
		Spiral s = new Spiral(1.0, 2.0, 0.0, 1.0, 100, 100);
		System.out.println("AREA: " + s.surfaceArea());
	}
}

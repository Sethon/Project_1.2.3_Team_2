package surfaces;

/**
 * Creates a parametric surface with the mathematical functions of a spiral.
 */
public class Spiral extends ParametricSurface3D {

	/**
	 * Creates a new spiral with the given information
	 * @param t0 Lower bound for the first parametric parameter.
	 * @param t1 Upper bound for the first parametric parameter.
	 * @param s0 Lower bound for the second parametric parameter.
	 * @param s1 Upper bound for the second parametric parameter.
	 * @param n1 Resolution for triangulation.
	 * @param n2 Resolution for triangulation.
	 */
	public Spiral(double t0, double t1, double s0, double s1, int n1, int n2) {
		instCnt++;
		assignLabel();
		super.t0 = t0;
		super.t1 = t1;
		super.s0 = s0;
		super.s1 = s1;
		super.n1 = n1;
		super.n2 = n2;
	}
	
	/**
	 * Computes the x-coordinates of the spiral.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeX(double t, double s) {
		return t * Math.cos(2 * Math.PI * s);
	}

	/**
	 * Computes the y-coordinates of the spiral.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeY(double t, double s) {
		return t * Math.sin(2 * Math.PI * s);
	}

	/**
	 * Computes the z-coordinates of the spiral.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeZ(double t, double s) {
		return s;
	}
	
	/**
	 * Returns a string with the information of the spiral.
	 * @return String containing the information.
	 */
	@Override
	public String toString() {
		return "Spiral - " + super.toString() + 
				"\n" + "Surface area: " + super.surfaceArea() + " sq. u.";
	}

	/**
	 * Computes the x-coordinates of the spiral with respect to t.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeXt(double t, double s) {
		return Math.cos(2 * Math.PI * s);
	}

	/**
	 * Computes the x-coordinates of the spiral with respect to s.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeXs(double t, double s) {
		return - 2 * Math.PI * t * Math.sin(2 * Math.PI * s);
	}
	
	/**
	 * Computes the y-coordinates of the spiral with respect to t.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeYt(double t, double s) {
		return Math.sin(2 * Math.PI * s);
	}
	
	/**
	 * Computes the y-coordinates of the spiral with respect to s.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeYs(double t, double s) {
		return 2 * Math.PI * t * Math.cos(2 * Math.PI * s);
	}
	
	/**
	 * Computes the z-coordinates of the spiral with respect to t.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeZt(double t, double s) {
		return 0;
	}
	
	/**
	 * Computes the z-coordinates of the spiral with respect to s.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeZs(double t, double s) {
		return 1;
	}
}

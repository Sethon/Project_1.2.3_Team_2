package surfaces;

/**
 * Creates a parametric surface with the mathematical functions of a Torus.
 */
public class Torus extends ParametricSurface3D {
	private double R;
	private double r;
	
	/**
	 * Creates a new torus with the given information
	 * @param t0 Lower bound for the first parametric parameter.
	 * @param t1 Upper bound for the first parametric parameter.
	 * @param s0 Lower bound for the second parametric parameter.
	 * @param s1 Upper bound for the second parametric parameter.
	 * @param n1 Resolution for triangulation.
	 * @param n2 Resolution for triangulation.
	 * @param R radius for the complete torus.
	 * @param r radius for the inner circle of the torus.
	 */
	public Torus(double t0, double t1, double s0, double s1, int n1, int n2, double R, double r) {
		instCnt++;
		assignLabel();
		super.t0 = t0;
		super.t1 = t1;
		super.s0 = s0;
		super.s1 = s1;
		super.n1 = n1;
		super.n2 = n2;
		this.R = R;
		this.r = r;
	}

	/**
	 * Computes the x-coordinates of the spiral.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeX(double t, double s) {
		return R * Math.cos(s) + r * Math.cos(t) * Math.cos(s);
	}

	/**
	 * Computes the y-coordinates of the spiral.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeY(double t, double s) {
		return R * Math.sin(s) + r * Math.cos(t) * Math.sin(s);
	}

	/**
	 * Computes the z-coordinates of the spiral.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeZ(double t, double s) {
		return r * Math.sin(t);
	}

	/**
	 * Returns a string with the information of the spiral.
	 * @return String containing the information.
	 */
	@Override
	public String toString() {
		return "Torus - " + super.toString() + "\n" + "R = " + R + " u." + "; r = " + r + " u." +
				"\n" + "Surface area: " + super.surfaceArea() + " sq. u.";
	}

	/**
	 * Computes the x-coordinates of the spiral with respect to t.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeXt(double t, double s) {
		return r * Math.cos(s) * (- Math.sin(t));
	}

	/**
	 * Computes the x-coordinates of the spiral with respect to s.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeXs(double t, double s) {
		return R * (- Math.sin(s)) + r * Math.cos(t) * (- Math.sin(s));
	}

	/**
	 * Computes the y-coordinates of the spiral with respect to t.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeYt(double t, double s) {
		return r * (- Math.sin(t)) * Math.sin(s);
	}

	/**
	 * Computes the y-coordinates of the spiral with respect to s.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeYs(double t, double s) {
		return R * Math.cos(s) + r * Math.cos(t) * Math.cos(s);
	}

	/**
	 * Computes the z-coordinates of the spiral with respect to t.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeZt(double t, double s) {
		return r * Math.cos(t);
	}

	/**
	 * Computes the z-coordinates of the spiral with respect to s.
	 * @param t First parameter for the spiral.
	 * @param s Second parameter for the spiral.
	 */
	@Override
	public double computeZs(double t, double s) {
		return 0;
	}
}

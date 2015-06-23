package surfaces;


import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Abstract extension of the Surface3D class which is used for parametric surfaces.
 */
public abstract class ParametricSurface3D extends Surface3D {
	//unit vectors
	public static final String TRAPEZOID 				= "Trapezoidal rule";
	public static final String SIMPSON 					= "Simpson's rule";
	
	protected static final Vector3D 	I_VECTOR 		= new Vector3D(1.0, 0.0, 0.0);
	protected static final Vector3D 	J_VECTOR 		= new Vector3D(0.0, 1.0, 0.0);
	protected static final Vector3D 	K_VECTOR 		= new Vector3D(0.0, 0.0, 1.0);
	private static final int INTEGRATION_STEPS_TRAP 	= 1000;
	private static final int INTEGRATION_STEPS_SIMP 	= 1000;
	private static boolean				trapezoid		= true;
	
	protected double 	t0;
	protected double 	t1;
	protected double 	s0;
	protected double 	s1;
	protected int 		n1;
	protected int 		n2;
	
	/**
	 * Abstract method which calculates the X value of the parametric surface.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the x-coordinate
	 */
	public abstract double computeX(double t, double s);
	
	/**
	 * Abstract method which calculates the Y value of the parametric surface.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the y-coordinate
	 */
	public abstract double computeY(double t, double s);

	/**
	 * Abstract method which calculates the Z value of the parametric surface.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the z-coordinate
	 */
	public abstract double computeZ(double t, double s);
	
	/**
	 * Abstract method which calculates the X value of the parametric surface with respect to t.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the x-coordinate
	 */
	public abstract double computeXt(double t, double s);

	/**
	 * Abstract method which calculates the X value of the parametric surface with respect to s.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the x-coordinate
	 */
	public abstract double computeXs(double t, double s);

	/**
	 * Abstract method which calculates the Y value of the parametric surface with respect to t.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the y-coordinate
	 */
	public abstract double computeYt(double t, double s);

	/**
	 * Abstract method which calculates the Y value of the parametric surface with respect to s.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the y-coordinate
	 */
	public abstract double computeYs(double t, double s);

	/**
	 * Abstract method which calculates the Z value of the parametric surface with respect to t.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the z-coordinate
	 */
	public abstract double computeZt(double t, double s);
	
	/**
	 * Abstract method which calculates the Z value of the parametric surface with respect to s.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the z-coordinate
	 */
	public abstract double computeZs(double t, double s);
	
	/**
	 * Calculates the vector R using the parametric parameters t and s.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the computed vector.
	 */
	public Vector3D computeR(double t, double s) {
		Vector3D a = I_VECTOR.scalarMultiply(computeX(t, s));
		Vector3D b = J_VECTOR.scalarMultiply(computeY(t, s));
		Vector3D c = K_VECTOR.scalarMultiply(computeZ(t, s));
		
		Vector3D d = a.add(b);
		
		return d.add(c);
	}

	/**
	 * Calculates the vector R with respect to t using the parametric parameters t and s.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the computed vector.
	 */
	public Vector3D computeRt(double t, double s) {
		Vector3D a = I_VECTOR.scalarMultiply(computeXt(t, s));
		Vector3D b = J_VECTOR.scalarMultiply(computeYt(t, s));
		Vector3D c = K_VECTOR.scalarMultiply(computeZt(t, s));
		
		Vector3D d = a.add(b);
		
		return d.add(c);
	}

	/**
	 * Calculates the vector R with respect to s using the parametric parameters t and s.
	 * @param t First parameter which is needed for the parametric coordinate calculations.
	 * @param s Second parameter which is needed for the parametric coordinate calculations.
	 * @return the computed vector.
	 */
	public Vector3D computeRs(double t, double s) {
		Vector3D a = I_VECTOR.scalarMultiply(computeXs(t, s));
		Vector3D b = J_VECTOR.scalarMultiply(computeYs(t, s));
		Vector3D c = K_VECTOR.scalarMultiply(computeZs(t, s));
		
		Vector3D d = a.add(b);
		
		return d.add(c);
	}
	
	/**
	 * Checks which of the surface area calculations need to be called.
	 */
	@Override
	public double surfaceArea() {
		if (trapezoid) {
			return surfaceAreaTrap();
		} else {
			return surfaceAreaSimp();
		}	
	}
	
	/**
	 * Uses the trapezoid method to calculate the surface area of the parametric surface.
	 * @return the surface area.
	 */
	private double surfaceAreaTrap() {
		double[] It = new double[INTEGRATION_STEPS_TRAP + 1];
		double stepsize = (t1-t0)/INTEGRATION_STEPS_TRAP;
		for(int i = 0; i < It.length; i++) {
			double valS = s0 + (s1 - s0)/INTEGRATION_STEPS_TRAP*i;
			It[i] = trapezoid(stepsize, valS, t0, t1);
		}
		double Ifin = 0;
		for (int i = 0; i < It.length; i++) {
			if (i == 0 || (i == It.length - 1)) {
				Ifin += 0.5 * It[i];
			}
			else {
				Ifin += It[i];
			}		
		}
		Ifin = ((s1 - s0)/INTEGRATION_STEPS_TRAP) * Ifin;
		return Ifin;
	}
	
	/**
	 * Uses the Simpson method to calculate the surface area of the parametric surface.
	 * @return the surface area.
	 */
	private double surfaceAreaSimp() {
		double[] It = new double[INTEGRATION_STEPS_SIMP + 1];
		double stepsize = (t1 - t0)/INTEGRATION_STEPS_SIMP;
		for(int i = 0; i < It.length; i++) {
			double valS = s0 + (s1 - s0)/INTEGRATION_STEPS_SIMP*i;
			It[i] = simpson(stepsize, valS, t0, t1);
		}
		
		double Ifin = 0;
		for (int i = 0; i < It.length; i++) {
			if(i == 0 || i == It.length - 1) {
				Ifin += It[i];
			}
			else if(i % 2 == 0) {
				Ifin += (2 * It[i]);
			}
			else {
				Ifin += (4 * It[i]);
			}
		}
		return Ifin * (((s1 - s0)/INTEGRATION_STEPS_SIMP)/3);
	}
	
	/**
	 * The trapezoid method for calculating integrals.
	 * @param step the stepsize for the checks.
	 * @param x Certain x value for the integral.
	 * @param t0 Lower bound for the integral.
	 * @param tn Upper bound for the integral.
	 * @return the computed integral value.
	 */
	private double trapezoid(double step, double x, double t0, double tn) {
		double I = 0;
		for(double i = t0; i <= tn + 10E-14; i+=step) {
			if(i==t0 || (i <= tn + 10E-14 && i >= tn - 10E-14)) {
				Vector3D Rt = computeRt(i, x); 
				Vector3D Rs = computeRs(i, x);
				Vector3D R = Rt.crossProduct(Rs);
				double norm = R.getNorm();
				I += 0.5 * norm;
			}
			else {
				Vector3D Rt = computeRt(i, x); 
				Vector3D Rs = computeRs(i, x);
				Vector3D R = Rt.crossProduct(Rs);
				double norm = R.getNorm();
				I += norm;
			}
				
		}
		return step * I;
	}

	/**
	 * The Simpson method for calculating integrals.
	 * @param step the stepsize for the checks.
	 * @param x Certain x value for the integral.
	 * @param t0 Lower bound for the integral.
	 * @param tn Upper bound for the integral.
	 * @return the computed integral value.
	 */
	private double simpson(double step, double x, double t0, double tn) {
		double Is = 0;
		for(int i = 0; i < INTEGRATION_STEPS_SIMP + 1; i++) {
			double it = t0 + step * i;
			Vector3D Rt = computeRt(it, x); 
			Vector3D Rs = computeRs(it, x);
			Vector3D R = Rt.crossProduct(Rs);
			double norm = R.getNorm();
			if(i == 0 || i == INTEGRATION_STEPS_SIMP) {
				Is += norm;
			}
			else if(i % 2 == 0) {
				Is += (2 * norm);
			}
			else {
				Is += (4 * norm);
			}
		}
		Is = Is * (step/3);
		return Is;
	}
	
	/**
	 * Constructs and returns the faces of the parametric surface.
	 * @return the faces of the parametric surface.
	 */
	@Override
	public ArrayList<Triangle3D> triangulate() {
		ArrayList<Triangle3D> tmp = new ArrayList<>();
		for (double t = t0; t < t1 - 10E-14; t += (t1 - t0)/n1) {
	    	for (double s = s0; s < s1 - 10E-14; s += (s1 - s0)/n2) {
	    		Triangle3D tr = new Triangle3D(new Point3D(computeX(t, s), computeY(t, s), computeZ(t, s)),
	    				new Point3D(computeX(t + (t1 - t0)/n1, s), computeY(t + (t1 - t0)/n1, s), computeZ(t + (t1 - t0)/n1, s)),
	    				new Point3D(computeX(t, s + (s1 - s0)/n2), computeY(t, s + (s1 - s0)/n2), computeZ(t, s + (s1 - s0)/n2)));
	    		tmp.add(tr);
	    	}
	    }
		
		for (double t = t1; t > t0 + 10E-14; t -= (t1 - t0)/n1) {
	    	for (double s = s1; s > s0 + 10E-14; s -= (s1 - s0)/n2) {
	    		Triangle3D tr = new Triangle3D(new Point3D(computeX(t, s), computeY(t, s), computeZ(t, s)),
	    				new Point3D(computeX(t - (t1 - t0)/n1, s), computeY(t - (t1 - t0)/n1, s), computeZ(t - (t1 - t0)/n1, s)),
	    				new Point3D(computeX(t, s - (s1 - s0)/n2), computeY(t, s - (s1 - s0)/n2), computeZ(t, s - (s1 - s0)/n2)));
	    		tmp.add(tr);
	    	}
	    }
		return tmp;
	}
	
	/**
	 * Returns the vertices of the parametric surfaces.
	 * @return the vertices of te parametric surface.
	 */
	@Override
	public ArrayList<Point3D> vertices() {
		ArrayList<Point3D> tmp = new ArrayList<>();
		for (double t = t0; t <= t1 + 10E-14; t += (t1 - t0)/n1) {
	    	for (double s = s0; s <= s1 + 10E-14; s += (s1 - s0)/n2) {
	    		tmp.add(new Point3D(computeX(t, s), computeY(t, s), computeZ(t, s)));
	    	}
		}
		return tmp;
	}
	
	/**
	 * Returns a string containing all the information of the parametric surface.
	 * @return the string containing the information.
	 */
	@Override
	public String toString() {
		return "Parametric surface" + "\nt ∈ [" + t0 + "; " + t1 + "]" + "\ns ∈ [" + s0 + "; " + s1 + "]";
	}
	
	/**
	 * Changes the numerical approach for calculating integrals.
	 * @param label String containing the approach.
	 */
	public static void switchNumericalMethod(String label) {
		trapezoid = label.equals(TRAPEZOID);
	}
}

package surfaces;

public class Torus extends ParametricSurface3D {
	private double R;
	private double r;
	
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
	
	@Override
	public double computeX(double t, double s) {
		return R * Math.cos(s) + r * Math.cos(t) * Math.cos(s);
	}

	@Override
	public double computeY(double t, double s) {
		return R * Math.sin(s) + r * Math.cos(t) * Math.sin(s);
	}

	@Override
	public double computeZ(double t, double s) {
		return r * Math.sin(t);
	}

	@Override
	public String toString() {
		return "Torus - " + super.toString() + "\n" + "R = " + R + " u." + "; r = " + r + " u." +
				"\n" + "Surface area: " + super.surfaceArea() + " sq. u.";
	}

	@Override
	public double computeXt(double t, double s) {
		return r * Math.cos(s) * (- Math.sin(t));
	}

	@Override
	public double computeXs(double t, double s) {
		return R * (- Math.sin(s)) + r * Math.cos(t) * (- Math.sin(s));
	}

	@Override
	public double computeYt(double t, double s) {
		return r * (- Math.sin(t)) * Math.sin(s);
	}

	@Override
	public double computeYs(double t, double s) {
		return R * Math.cos(s) + r * Math.cos(t) * Math.cos(s);
	}

	@Override
	public double computeZt(double t, double s) {
		return r * Math.cos(t);
	}

	@Override
	public double computeZs(double t, double s) {
		return 0;
	}
}

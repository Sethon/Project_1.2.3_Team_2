package surfaces;

public class Spiral extends ParametricSurface3D {

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
	
	@Override
	public double computeX(double t, double s) {
		return t * Math.cos(2 * Math.PI * s);
	}

	@Override
	public double computeY(double t, double s) {
		return t * Math.sin(2 * Math.PI * s);
	}

	@Override
	public double computeZ(double t, double s) {
		return s;
	}
	
	@Override
	public String toString() {
		return "Spiral - " + super.toString() + 
				"\n" + "Surface area: " + super.surfaceArea() + " sq. u.";
	}

	@Override
	public double computeXt(double t, double s) {
		return Math.cos(2 * Math.PI * s);
	}

	@Override
	public double computeXs(double t, double s) {
		return - 2 * Math.PI * t * Math.sin(2 * Math.PI * s);
	}

	@Override
	public double computeYt(double t, double s) {
		return Math.sin(2 * Math.PI * s);
	}

	@Override
	public double computeYs(double t, double s) {
		return 2 * Math.PI * t * Math.cos(2 * Math.PI * s);
	}

	@Override
	public double computeZt(double t, double s) {
		return 0;
	}

	@Override
	public double computeZs(double t, double s) {
		return 1;
	}
}

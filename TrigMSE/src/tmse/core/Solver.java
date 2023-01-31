package tmse.core;

public class Solver
{
	public static double[] calc(int size,double freq,double[] x,double[] y)
	{
		double sin = 0.0d;
		double cos = 0.0d;
		double sin_2 = 0.0d;
		double cos_2 = 0.0d;
		double sincos = 0.0d;
		double yisin = 0.0d;
		double yicos = 0.0d;
		double yi = 0.0d;

		for(int i=0; i<size; i++) {
			sin += Math.sin(freq * x[i]);
			cos += Math.cos(freq * x[i]);
			sin_2 += Math.pow(Math.sin(freq * x[i]),2.0d);
			cos_2 += Math.pow(Math.sin(freq * x[i]),2.0d);
			sincos += Math.sin(freq * x[i]) * Math.cos(freq * x[i]);
			yisin += y[i] * Math.sin(freq * x[i]);
			yicos += y[i] * Math.cos(freq * x[i]);
			yi += y[i];
		}

		S3Equation eq = new S3Equation();
		eq.line(0, sin_2, sincos, sin);
		eq.line(1, sincos, cos_2, cos);
		eq.line(2, sin, cos, size);
		eq.setVectorAt(0, yisin);
		eq.setVectorAt(1, yicos);
		eq.setVectorAt(2, yi);

		double[] sol = eq.solve();
		double a = Math.sqrt(Math.pow(sol[0],2.0d) + Math.pow(sol[1],2.0d));
		double b = Math.atan2(sol[1],sol[0]);

		return new double[] {a,b,sol[2]};
	}
}

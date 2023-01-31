package tmse.core;

public class S3Equation
{
	protected double[][] matrix = null;
	protected double[] vector = null;

	public S3Equation()
	{
		this.matrix = new double[3][3];
		for(int i=0; i<3; i++) for(int j=0; j<3; j++) this.matrix[i][j] = i == j ? 1.0d : 0.0d;

		this.vector = new double[3];
		for(int k=0; k<3; k++) this.vector[k] = 0.0d;
	}

	@SuppressWarnings("unused")
	private void display(double[][] m)
	{
		for(int i=0; i<3; i++) {
			for(int j=0; j<4; j++) System.out.print(m[i][j] + "\t");
			System.out.println();
		}
		System.out.println();
	}

	public double[] solve()
	{
		double[][] m = new double[3][4];
		for(int i=0; i<3; i++) for(int j=0; j<3; j++) m[i][j] = this.matrix[i][j];
		for(int k=0; k<3; k++) m[k][3] = this.vector[k];

		double c = 0.0d;
		for(int p=0; p<3; p++) {
			for(int q=p+1; q<3; q++) {
				c = m[q][p] / m[p][p];
				for(int r=0; r<4; r++) m[q][r] -= c * m[p][r];
			}
		}
		for(int p=2; p>=0; p--) {
			for(int q=p-1; q>=0; q--) {
				c = m[q][p] / m[p][p];
				for(int r=3; r>=p; r--) m[q][r] -= c * m[p][r];
			}
		}

		double[] sol = new double[3];
		for(int k=0; k<3; k++) sol[k] = m[k][3] / m[k][k];

		return sol;
	}

	public void line(int l,double v1,double v2,double v3)
	{
		this.matrix[l][0] = v1;
		this.matrix[l][1] = v2;
		this.matrix[l][2] = v3;
	}

	public void setElementAt(int i,int j,double v)
	{
		this.matrix[i][j] = v;
	}

	public void setVectorAt(int k,double v)
	{
		this.vector[k] = v;
	}

	public double getElementAt(int i,int j)
	{
		return this.matrix[i][j];
	}

	public double getVectorAt(int k)
	{
		return this.vector[k];
	}
}

package jwork;
public class LinearCalculate {
	//String -->  Matrix(double [][])
	 double[][] Matr(String s,int n,int m) throws IllegalArgumentException{
		 	if(s.matches("^[0-9]*$")) {//check if string have illegal characters
		 		
		 		throw new IllegalArgumentException("分隔符缺失或");
		 	}
			double [][] matr=new double[n][m];
			if(s.matches("^[^0-9,;]+$"))
			{	
				throw new IllegalArgumentException("未知符号输入");
			}
			int num=0;
			for (int temp0=0;temp0<s.length();temp0++)
			{	char p=s.charAt(temp0);
				if(Character.isDigit(p)) {
					int i=0;
					String s1="";
					while((s.charAt(i+temp0)!=','&&s.charAt(i+temp0)!=';')&&(i+temp0)<s.length())
					{
						s1=s1+s.charAt(i+temp0);
						i++;
					}
					temp0+=i;
					matr[(num)/(m)][(num++)%m]=Double.parseDouble(s1);
				} 	
				else if(p==',')
				{
					if(s.charAt(temp0+1)==','||s.charAt(temp0+1)==';')
					{
						matr[(num/m)][(num++)%m]=0;
					}
				}
				else if(p==';')
				{
					num++;
				}
			}
			return matr;
		}
	 double[][]rowToColumn(double[][]b)
	{	int temp=b[0].length;
		double[][]n=new double[temp][1];
		for(int o=0;o<temp;o++)
		{
			n[o][0]=b[0][o];
		}
		return n;
	}
//	private double[]columnToRow(double[][]b)
//	{	int temp=b.length;
//		double[][]n=new double[1][temp];
//		for(int o=0;o<temp;o++)
//		{
//			n[0][o]=b[o][0];
//		}
//		return n[0];
//	}
	   int findSign(String s)//verify the input matrix size is right
	{	int count=0;
		for (int i=0;i<s.length();i++)
		{
  	        if(s.charAt(i)==','|| s.charAt(i)==';')
 	        {
 	        	count++;
 	        }
		}
		return count;
	}
	   private boolean isSymmetry(double [][]a,int lenOfA)
	   {
		   for(int row=0;row<lenOfA-1;row++)
		   {
			   for(int col=1;col<lenOfA;col++)
			   {
				   if(a[row][col]!=a[col][row])
				   {
					   return false;
				   }
			   }
			  
		   }
		   return true;
	   }
	   public double[] linearCalc(double [][]A,double[][]b) {
		   int lenOfA=A.length;
		   if(lenOfA<32||!(isSymmetry(A,lenOfA)))
		   {
			   double [][]B=rowToColumn(b);
			   return gaussMethod(A,B);
		   }
		   else {
			   double[] B = b[0];
			   try {
		        return cG(A,B,lenOfA);
			   }
			   //if matrix is not a Positive definite matrix 
			   //cG will throw exception
			   catch(IllegalArgumentException e1) 
			   {
			   	B=null;
			   	double [][]B1=rowToColumn(b);
				return gaussMethod(A,B1);
		   }
			   }
		}
	    public  double[] cG(double[][] A, double[] b,int lenOfA) throws IllegalArgumentException
	    {	
	    	double eps=1e-6;
	        int n = lenOfA;
	        double[] x = new double[n];
	        double[] r = b.clone(); 
	        double[]r0=r;
	        double[] p = r.clone();
	        for (int k = 0; k < n; k++)
	        {
	            double[] Ap = multiply(A, p);
	            double alpha = dot(r, r) / dot(p, Ap);
	            for (int i = 0; i < n; i++) {
	                x[i] += alpha * p[i];
	                r[i] -= alpha * Ap[i];
	        }
	            double beta = dot(r, r) / dot(p, p); 
	            for (int i = 0; i < n; i++) {
	                p[i] = r[i] + beta * p[i];
	            }
	            if((norm(r)/norm(r0))>eps)
	            {
	            	throw new IllegalArgumentException("非正定");
	            }
	        }
			return x;
	        
	    }
	    public  double dot(double[] a, double[] b) {
	        double dot = 0;
	        for (int i = 0; i < a.length; i++) {
	            dot += a[i] * b[i];
	        }
	        return dot;
	    }

	    public  double[] multiply(double[][] A, double[] x) {
	        int n = A.length;
	        double[] result = new double[n];
	        for (int i = 0; i < n; i++) {
	            for (int j = 0; j < n; j++) {
	                result[i] += A[i][j] * x[j];
	            }
	        }
	        return result;
	    }
		private double [][] swapMatrix(double [][] a,int rowInit,int rowTarget) throws IllegalArgumentException
		{	
			if(rowInit==rowTarget)
			{return a;}
//			if(a.length<(rowTarget>rowInit?rowTarget:rowInit))
//			{
//				throw new IllegalArgumentException("矩阵行数超出限制，交换失败");
//			}
			for (int column =0;column<a[0].length;column++)
			{
				double c=a[rowInit][column];
				a[rowInit][column]=a[rowTarget][column];
				a[rowTarget][column]=c;				
			}
			return a;
		}
		private double [][] subMatrix(double [][]a,int init){
			int a_row=a.length;
			for(int i=a_row;i>=0;i--)//column
			{//tips:don't let i=0;i<a_row+1; i++!
				for(int j=0;j<a_row;j++) {//row
					if(j==init) {continue;}
					else
					{	
						a[j][i]-=a[init][i]*a[j][init]/a[init][init];					
					}
				}

			}
			return a;
		}
		//2-norm
		private  double norm(double[] a) {
		double sum = 0;
		for (double elem : a) {
		    sum += elem * elem;
		}
		return Math.sqrt(sum);
		}
		//temporary scenario
 		double [] gaussMethod(double[][]a,double[][]b){
 			int a_column=a[0].length;
 			int a_row=a.length;
 			double [][]augmentedMatrix=new double[a_row][a_column+1];
 			//let double[][] a,b merge into an augmented matrix.
 			/*-		- 		- 		-
 			 *| 1 2 |       | 1 2 1 |
 			 *| 2 1 |  -->  | 2 1 1 |
 			 *-     - 		-		-
 			 * */
 			for (int i=0;i<a_row;i++)
 			{
 				for(int j=0;j<a_column;j++)
 				{
 					augmentedMatrix[i][j]=a[i][j];
 				}
 				augmentedMatrix[i][a_row]=b[i][0];
 			} 			
 			for (int i=0;i<a_column;i++)
 			{
 				int maxRow=i;
 				for(int j=i;j<a_row-1;j++)
 				{
 					if(Math.abs(a[j][i])<Math.abs(a[j+1][i]))
 					{
 						maxRow=1+j;
 					}
 				}				
 				augmentedMatrix=swapMatrix(augmentedMatrix,i,maxRow);
 				augmentedMatrix=subMatrix(augmentedMatrix,i);
 			}
 			double [] x=new double[a_row];
 			for(int i=0;i<a_row;i++)
 			{
 				x[i]=augmentedMatrix[i][a_row]/augmentedMatrix[i][i];
 			}
 			return x;
 		}	
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
// 		private  double[][] multiply(double scalar, double[][] a) {
//	    int m = a.length;
//	    int n = a[0].length;
//	    double[][] c = new double[m][n];
//	    for (int i = 0; i < m; i++) {
//	        for (int j = 0; j < n; j++) {
//	            c[i][j] = scalar * a[i][j];
//	        }
//	    }
//	    return c;
//	}
//	private  double[][] multiply(double[][] a, double[][] b) {
//		int m = a.length;
//		int n = b[0].length;
//		//int p = b.length;
//		double[][] c = new double[m][n];
//		for (int i = 0; i < m; i++) {
//		    for (int j = 0; j < n; j++) {
//		        for (int k = 0; k < m; k++) {
//		        	//System.out.print(k);
//		            c[i][j] += a[i][k] * b[k][j];
//		            
//		        }
//		    }
//		}
//		return c;
//		}
//
//		public void print(double [][] s)
//		{
//			for (double[] a:s) {
//				for(double b:a) {
//				System.out.println(b);
//			}
//				System.out.println("\n");
//				}
//		}
//		private  double[][] add(double[][] a, double[][] b) {
//		//	print(b);
//		int m = a.length;
//		int n = a[0].length;
//		double[][] c = new double[m][n];
//		for (int i = 0; i < m; i++) {
//		    for (int j = 0; j < n; j++) {
//		        c[i][j] = a[i][j] + b[i][j];
//		    }
//		}
//		return c;
//		}
//
//		private  double[][] subtract(double[][] a, double[][] b) {
//		int m = a.length;
//		int n = a[0].length;
//		double[][] c = new double[m][n];
//		for (int i = 0; i < m; i++) {
//		    for (int j = 0; j < n; j++) {
//		        c[i][j] = a[i][j] - b[i][j];
//		    }
//		}
//		return c;
//		}
//		private  double[][] transpose(double[][] a) {
//		int m = a.length;
//		int n = a[0].length;
//		double[][] b = new double[n][m];
//		for (int i = 0; i < m; i++) {
//		    for (int j = 0; j < n; j++) {
//		        b[j][i] = a[i][j];
//		    }
//		}
//		return b;
//		}
//

//
//		private  double[][] dot(double[][] a, double[][] b) {
//		double[][] c = multiply(a, b);
//		return new double[][]{{norm(c)}};
//		}
//
//	private  double[][]fillInit(double [][]r,int n)
//	{
//		for (double[] row : r) {
//		    Arrays.fill(row, n);
//		}
//		return r;
//	}
//	  double[]BiCGStab(double [][] a,double [][]b,double tol,int limit)
//	{	int n=b.length;
//	b=multiply(transpose(a),b);
//	a=multiply(transpose(a),a);
//		boolean[] converged = {false};
//		double[][] v = new double[n][1];
//		double[][] r = new double[n][1];
//		double[][] r0_hat = new double[n][1];
//		double[][] p = new double[n][1];
//		double[][] s = new double[n][1];
//		double[][] t = new double[n][1];
//		double[][] x = new double [n][1];
//		x=fillInit(x,1);
//		r=fillInit(r,0);
//		r0_hat=fillInit(r0_hat,0);
//		p=fillInit(p,0);
//		s=fillInit(s,0);
//		t=fillInit(t,0);
//		v=fillInit(v,1);
//		r = subtract(b, multiply(a, x));
//		r0_hat = r.clone();
//		double rho0 = 1.0;
//		double alpha = 1.0;
//		double w = 1.0;
//		v = new double[n][1];
//		p = new double[n][1];
//		double rho1 = dot(transpose(r0_hat), r)[0][0];
//		int iters = 0;
//		while (true) {
//		    iters++;
//		    converged[0] = (norm(r) < tol * norm(b));
//		    if (converged[0] || iters == limit) {
//		        break;
//		    }
//		    double beta = (rho1 / rho0) * (alpha / w);
//		    p = add(r, multiply(beta, subtract(p, multiply(w, v))));
//		    v = multiply(a, p);
//		    print(p);
//		    alpha = rho1 / dot(transpose(r0_hat), v)[0][0];	
//		    s = subtract(r, multiply(alpha, v));//err
//		    t = multiply(a, s);
//		    w = dot(transpose(t), s)[0][0] / dot(transpose(t), t)[0][0];
//		    rho0 = rho1;
//		    rho1 = -w * dot(transpose(r0_hat), t)[0][0];
//		    x = add(x, add(multiply(alpha, p), multiply(w, s)));
//		    r = subtract(s, multiply(w, t));		    
//		}
//
//		return  columnToRow(x);
//		}
		
//		if(((a.length)&(a.length-1))!=0)//estimate if matrix size is 2^k
//		{
 }
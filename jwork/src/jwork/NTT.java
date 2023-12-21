package jwork;

/*
 * NTT(String a,String b) return int[]
 * int []=NTT.getResult();
 */
public class NTT {
   private static final int MOD = 998244353;
   private String result;
   private static final int ROOT = 3;
   private int M=1;
   private int[] resArr;
   private int pow(int x, int n) {
       int res = 1;
       while (n > 0) {
           if ((n&1)!=0) {
               res = (int) ((long) res * x % MOD);
           }
           x = (int) ((long) x * x % MOD);
           n >>=1;
       }
       return res;
   }
   private void ntt(int[] a, boolean inverse) {
       int n = a.length;
       //count number of 0 after the last 1 in binary
       int logn = Integer.numberOfTrailingZeros(n);
       int[] rev = new int[n];
       for (int i = 0; i < n; ++i) {
           rev[i] = (rev[i >> 1] >> 1) | ((i & 1) << (logn - 1));
           if (i < rev[i]) {
               int temp = a[i];
               a[i] = a[rev[i]];
               a[rev[i]] = temp;
           }
       }
       for (int len = 2; len <= n; len <<= 1) {
           int wlen = inverse ? pow(ROOT, MOD - 1 - MOD / len) : pow(ROOT, MOD / len);
           for (int i = 0; i < n; i += len) {
               int w = 1;
               for (int j = 0; j < len / 2; ++j) {
                   int u = a[i + j];
                   int v = (int) ((long) a[i + j + len / 2] * w % MOD);
                   a[i + j] = (u + v) % MOD;
                   a[i + j + len / 2] = (u - v + MOD) % MOD;
                   w = (int) ((long) w * wlen % MOD);
               }
           }
       }
       if (inverse) {
           int inv = pow(n, MOD - 2);
           for (int i = 0; i < n; ++i) {
               a[i] = (int) ((long) a[i] * inv % MOD);
           }
       }
   }
	private int findN(int n) {
		n--;
	    n |= n >> 1;
	    n |= n >> 2;
	    n |= n >> 4;
	    n |= n >> 8;
	    n |= n >> 16;
	    return (n + 1);
	    }
   public  int[] multiplyPolynomials(String A, String B) {
       int degA = A.length();
       int degB = B.length();
       this.M=findN(degA>degB?degA:degB);
       int n= findN(degA+degB);
       int[] coeffA = new int[n];
       int[] coeffB = new int[n];
       for (int i = 0; i < degA; i++) {
           coeffA[i] = A.charAt(degA - 1 - i) - '0';
       }
       for (int i = 0; i < degB; i++) {
           coeffB[i] = B.charAt(degB - 1 - i) - '0';
       }
       ntt(coeffA, false);
       ntt(coeffB, false);
       for (int i = 0; i < n; i++) {
           coeffA[i] = (int) ((long) coeffA[i] * coeffB[i] % MOD);
       }
       ntt(coeffA, true);
       return coeffA;
   }
   private String arrToStr(int[]arr)
	{	
		StringBuffer sb = new StringBuffer();
		for(int j=arr.length-1;j>=0;j--){
			sb.append(arr[j]);
		}
		return sb.toString();
	}
   private int [] carryArr(int []res)
	{	
		
		int a=0;
		for(int i=0;i<2*M-1;i++)
		{	
			a=res[i]/10;
			int carry=(a==0)?0:a;
			//System.out.println(res[i]);
			res[i]%=10;
			res[i+1]+=carry;
		}
		return res;
	}
   public  NTT(String a,String b) {  
	   
	   this.resArr=multiplyPolynomials(a,b);
       this.result = arrToStr(carryArr(resArr));
       
   }
   public String getResult() {
	   return this.result;
   }
   public int[] getResArr() {
	   return this.resArr;
   }
  
   
}
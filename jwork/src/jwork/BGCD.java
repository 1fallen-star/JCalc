package jwork;

//Binary GCD
/*BGCD(a,b) return the GCD of a,b
 * */
public class BGCD {
	private int result=0;
	public BGCD(int a,int b)
	{
		this.result=calculateGCD(a,b);
	}
	public int getResult()
	{
		return this.result;
	}
    public int calculateGCD(int a, int b) {
    	int c=a;
        if (b > a) {
            a=b;
            b=c;
        }
        a=b;
        b=c%b;
        while (b != 0) {
            if ((a & 1) == 0 && (b & 1) == 0) {
                a >>= 1;
                b >>= 1;
            } else if ((a & 1) == 0) {
                a >>= 1;
            } else if ((b & 1) == 0) {
                b >>= 1;
            } else {
                int t = (a - b)&(1<<31-1);
                //abs(x)
                a =a>b?b:a;
                b = t;
            }
        }
        return a << calculatePowerOf2(a);
    }
    public  int calculatePowerOf2(int n) {
        int power = 0;
        while ((n & 1) == 0) {
            n >>= 1;
            power++;
        }
        return power;
    }  
}
package jwork;
import java.util.List;
import java.util.Stack;

/*fastMul function uses NTT to make multiplication
 * fastMul will check your input if have .
 * the result is String instead of double or float
 * */
/*expOfNum function check String(3)
 * if ==0,then return positive
 * else ==1 return negative
 * */
public class SymbolicComputation {//exact compute
//	private int pointOfNum(String a,int lenOfa)
//	{
//		int i=0;
//		while(a.charAt(i+++1)!='.'&&(i+1)<lenOfa);			
//		return i;
//	}
//	private int [] carryArr(int []res,int N)
//	{	
//		int a=0;
//		for(int i=0;i<2*N-1;i++)
//		{	
//			a=res[i]/10;
//			int carry=(a==0)?0:a;
//			res[i]%=10;
//			res[i+1]+=carry;
//		}
//		return res;
//	}
//	public String div(String a ,String b)
//	{
//		int lenOfa=a.length();
//		int lenOfb=b.length();
//		int dividend=0;
//		int divisor=0;
//		int minusOfa=(int)a.charAt(3)-30;
//		int minusOfb=(int)b.charAt(3)-30;
//		int i=0;
//		int expOfRes=expOfNum(a)-expOfNum(b);
//		int numOfa=Integer.parseInt(a.substring(4,lenOfa));
//		int numOfb=Integer.parseInt(b.substring(4,lenOfb));
//		BGCD divGCD=new BGCD(numOfa,numOfb);
//		
//		return ;
//	}
//	private String divisible (String a,String b)
//	{
//		int m=a.length();
//		int n=b.length();
//		int[]r=new int[m-n+2];
//		int k=0;
//		for(k=m-n+1;k>=0;k--)
//		{
//			
//		}
//	}
//	public Data add(Data a ,Data b)
//	{	
//		int aDivs=Integer.parseInt(a.getDivisor());
//		int bDivs=Integer.parseInt(b.getDivisor());
//		BGCD divGCD=new BGCD(aDivs,bDivs);
//		int gcd=divGCD.getResult();
//		NTT divs=new NTT(a.getDivisor(),b.getDivisor());
//		int ress=Integer.parseInt(divs.getResult());
//		int resDivs=ress/gcd;
//		int lenOfas=a.getDivisor().length();
//		int lenOfbs=b.getDivisor().length();
//		int lenOfad=a.getDividend().length();
//		int lenOfbd=b.getDividend().length();
//		int res[]=new int[2];
//		res[1]=findN(lenOfas>lenOfbs?lenOfas:lenOfbs);
//		res[0]=findN(lenOfad>lenOfbd?lenOfad:lenOfbd);
//		
//		int N=0;
//		
//		return ;
//	}
//	
//	private int findN(int n) {
//		n--;
//	    n |= n >> 1;
//	    n |= n >> 2;
//	    n |= n >> 4;
//	    n |= n >> 8;
//	    n |= n >> 16;
//	    return (n + 1);
//	    }
	public Data fastMul(Data a,Data b)
	{	
		int expOfRes=(a.getExpNum()+b.getExpNum());

		NTT divd=new NTT(a.getDividend(),b.getDividend());
		NTT divs=new NTT(a.getDivisor(),b.getDivisor());
		
		//int numOfb=Integer.parseInt
		String numOfb=(divs.getResult());
		//int numOfa=Integer.parseInt
		String numOfa=(divd.getResult());
//		if(numOfb!=1) {
//		BGCD divGCD=new BGCD(numOfa,numOfb);
//		int gcd=divGCD.getResult();
//		numOfa/=gcd;
//		numOfb/=gcd;
//		}
		
		Data res=new Data(expOfRes,(b.getNumSign()^a.getNumSign()),
				numOfa,numOfb);
		//System.out.println(res.turnToStr()+"dawdada");
		return res;
	}
	public String compute(List<String> signlist) {
		
			 int numberOfDigits=0;
			 
			//System.out.print(signlist);//debug
			 for (Object element : signlist) {
				 //get the type of element
				 //and  judge if parameters are right
				    Class<?> clazz = element.getClass();
				    if (clazz.equals(Integer.class)) {
				    	numberOfDigits++;
				    }

				}
			 if(numberOfDigits>1&&numberOfDigits!=2*(signlist.size())/3)
			 {
				 throw new IllegalArgumentException("Wrong Argument");
			 }
			Stack<Data> numStack = new Stack<Data>();		
			int i=0;
			if(signlist.isEmpty())
			{
				return "语法错误，参数缺失";
			}
			while(i<signlist.size())
			{
				//maybe here need to be rewrite to Factory Pattern
				char ci=signlist.get(i).charAt(0);
				
				if(Character.isDigit(ci))//help me to optimize the follow code
				{	
					
					String num = signlist.get(i);
					
					Data number=new Data(num.trim());
					numStack.push(number);
				}
//				else if(ci=='+')
//				{
//					Data num2=numStack.pop();
//					Data num1=numStack.pop();
//					numStack.push(add(num1,num2));
//				}
//				else if(ci=='-')
//				{
//					Data num1=new Data("0");
//					Data num2=numStack.pop();
//					num1=numStack.pop();
//					numStack.push(sub(num1,num2));
//				}
				else if(ci=='*')
				{
					Data num2=numStack.pop();
					Data num1=numStack.pop();

					numStack.push(fastMul(num1,num2));
				}
//				else if(ci=='/')
//				{
//					 
//				   
//				    Data num2=numStack.pop();
//					Data num1=numStack.pop();
//					numStack.push(div(num1,num2));
//				}
				
				i++;
			}
			
				return numStack.pop().turnToStr();

		}
		
	}

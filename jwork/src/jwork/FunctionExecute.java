package jwork;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class FunctionExecute {

	public static boolean checkInput(Double double1) throws IllegalArgumentException  {
	        
	    if(double1==0){//if .pop() is zero,then throw exception
	                throw new IllegalArgumentException("输入数据产生infinity");
	            }
	        return true;
	}
	private double F(double val,String f) {//this part of code needs to be optimized
		//System.out.println(polishNotation(f,1,val));//debug
		return finalCalcGeneral(polishNotation(f,1,val));
		}
	private  double simpson(double a,double b,String f) {
		  double c=a+(b-a)/2;
		  return (F(a,f)+4*F(c,f)+F(b,f))*(b-a)/6;
		}

	 double asr(double a,double b,double eps,double S,String f) {
		  double c=a+(b-a)/2;
		  double lS=simpson(a,c,f),rS=simpson(c,b,f);
		  if(Math.abs(lS+rS-S)<=15*eps) return lS+rS+(lS+rS-S)/15.0;//this number was determined by mathematics
		  return asr(a,c,eps/2,lS,f)+asr(c,b,eps/2,rS,f);
		}//O(log(n/eps))
	 double finalCalcGeneral(List<String> signlist) throws IllegalArgumentException,EmptyStackException
	{	
		 int numberOfDigits=0;
		 int numberOfX=0;
		 //System.out.print(signlist);//debug
		 for (Object element : signlist) {
			//get the type of element
			 //and  judge if parameters are right
			    Class<?> clazz = element.getClass();
			    if (clazz.equals(Integer.class)) {
			    	numberOfDigits++;
			    }
			    else if(element=="x"||element=="X")
			    {
			    	numberOfX++;
			    }
			}
		 if(numberOfDigits>1&&numberOfDigits!=2*(signlist.size()-numberOfX)/3)
		 {
			 throw new IllegalArgumentException("符号数字个数不匹配");
		 }
		Stack<Double> numStack = new Stack<Double>();		
		int i=0;
		if(signlist.isEmpty())
		{
			return 0;
		}
		while(i<signlist.size())
		{
			//maybe here need to be rewrite to Factory Pattern
			char ci=signlist.get(i).charAt(0);
			if(Character.isDigit(ci))//help me to optimize the follow code
			{
				double num = Double.parseDouble(signlist.get(i));
				numStack.push(num);
			}
			else if(ci=='+')
			{
				double num2=numStack.pop();
				double num1=numStack.pop();
				numStack.push(num1+num2);
			}
			else if(ci=='-')
			{
				double num1=0.0;
				double num2=numStack.pop();
				num1=numStack.pop();
				numStack.push(num1-num2);
			}
			else if(ci=='*')
			{
				double num2=numStack.pop();
				double num1=numStack.pop();
				numStack.push(num1*num2);
			}
			else if(ci=='/')
			{
				 
			    checkInput(numStack.peek());
			    double num2=numStack.pop();
				double num1=numStack.pop();
				numStack.push(num1/num2);
			}
			else if(ci=='@')
			{
				double num2=numStack.pop();
				numStack.pop();
				numStack.push(Math.sin(num2));
			}
			else if(ci=='#')
			{
				double num2=numStack.pop();
				numStack.pop();
				numStack.push(Math.cos(num2));
			}
			else if(ci=='$')
			{
				double num2=numStack.pop();
				numStack.pop();
				numStack.push(Math.tan(num2));
			}
			else if(ci=='%')
			{
			    checkInput(numStack.peek());    
				double num2=numStack.pop();
				numStack.pop();
				numStack.push(Math.log(num2));
			}
			else if(ci==':')
			{
				double num2=numStack.pop();
				numStack.pop();
				numStack.push(Math.exp(num2));
			}
			else if(ci=='&')
			{
				double num2=numStack.pop();
				double num1=numStack.pop();
				numStack.push(Math.pow(num1,num2));
			}
			else if(ci=='~')
			{
				double num2=numStack.pop();
				numStack.pop();
				numStack.push(Math.atan(num2));
			} 
			i++;
		}
		if(Math.abs(numStack.peek())<1E-15)
			{
				return 0;
			}
			return numStack.pop();

	}
	 List<String> polishNotation (String s) throws IllegalArgumentException{//overload
		return polishNotation(s,0,0.0);
	}

	 List<String> polishNotation (String s,int model,double number) throws IllegalArgumentException{
		 //number is value of x
		List<String> items = new ArrayList<String>();
		if(model!=0&&s.matches(".*[^\\x23-\\x40costanePxXil^]+.*"))//check if string have illegal character
		{
			throw new IllegalArgumentException("输入数据含有未知符号");
		}
		s=s.replaceAll("(?<![0-9])-", "0-");
	    Map<Character, Integer> opMap = new HashMap<Character, Integer>();
	    opMap.put('(', 0);
	    opMap.put('+', 1);
	    opMap.put('-', 1);
	    opMap.put('*', 2);
	    opMap.put('/', 2);
	    opMap.put('@',3);//sin
	    opMap.put('#', 3);//cos
	    opMap.put('$', 3);//tan
	    opMap.put('%', 3);//ln
	    opMap.put(':', 3);//exp
	    opMap.put('&', 3);//pow
	    opMap.put('~', 3);//arctan
	    //opMap.put('>',3);//differential
	    //opMap.put('.',4);
	    //hashmap set operation priority
	    Stack<Character> opStack = new Stack<Character>();
	    int end = 0;
	    while (end < s.length()) {
	        int start = end;
	        char c = s.charAt(end++);
	        if (c == '(') {
	            opStack.push(c);
	        } 
	        else if (c == '+' || c == '-' || c == '*' || c == '/') {
	            while (!opStack.isEmpty() && opMap.get(c) <= opMap.get(opStack.peek()))
	                items.add(String.valueOf(opStack.pop())+' ');
	            opStack.push(c);
	        } 
	        else if (c == ')') {
	            while (opStack.peek() != '(')
	                items.add(String.valueOf(opStack.pop())+' ');
	            opStack.pop();
	        } 
	        else if (Character.isDigit(c)||c==';'||c=='?'||c=='x'||c=='X'||c=='.') {
	        	if(c==';')
	        	{
	        		items.add("3.141592653589793238462643383279");
	        	}
	        	else if(c=='?')
	        	{
	        		items.add("2.718281828459045235360287471353");
	        	}
	        	else if(c=='x'||c=='X')
	        	{
	        		items.add(""+number);
	        	}
	            while (end < s.length() && (Character.isDigit(s.charAt(end))||s.charAt(end)=='.')) 
	                end++;
	            items.add(s.substring(start, end)+' '); // extract the number
	            
	            }
	        else if(c == '@' || c == '#' || c == '$' || c == '%'|| c == ':' || c == '&' || c == '~') {
	        	 while (!opStack.isEmpty() && opMap.get(c) <= opMap.get(opStack.peek()))
		                items.add(String.valueOf(opStack.pop())+' ');
		            opStack.push(c);
	        }
	    }
	    while (!opStack.isEmpty()) 
	    	items.add(String.valueOf(opStack.pop())); // pop up remaining items.
	    return items;
	}
}

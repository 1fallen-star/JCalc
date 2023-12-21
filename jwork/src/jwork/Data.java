package jwork;
/* xxxSign has 1 or -1
 * expNum is 1e+expNum
 *  a/b ==dividend/divisor
 * */
public class Data {
	//why do I use int to express expNum?
	//because expNum may be larger than 8 million
	private int expNum=0;
	private boolean numSign=true;
	private String dividend="0";
	private String divisor="1";
	public Data(int expNum,boolean numSign,
			String dividend,String divisor)
	{
		this.expNum=expNum;
		this.numSign=numSign;
		this.dividend=dividend;
		this.divisor=divisor;
	}
	public Data(String dividend)
	{
		this.expNum=0;
		this.numSign=false;
		this.dividend=dividend;
		this.divisor="1";
	}
	public void setExpNum(byte expNum)
	{
		this.expNum=expNum;
	}
	public void setNumSign(boolean sign)
	{
		this.numSign=sign;
	}
	public void setDividend(String divd)
	{
		this.dividend=divd;
	}
	public void setDivisor(String divs)
	{
		this.divisor=divs;
	}
	public int getExpNum()
	{
		return this.expNum;
	}
	public boolean getNumSign()
	{
		return numSign;
	}
	public String  getDivisor()
	{
		return this.divisor;
	}
	public  String getDividend()
	{
		return this.dividend;
	}
	public String turnToStr()
	{
		StringBuffer sb = new StringBuffer();
		
		if(numSign==true)
		{
			sb.append("-");
		}
		String divd="0";
		
		if(!dividend.matches("^0*")) {
			
			 divd=dividend.replaceFirst("^0*", "");
		}
		int lenOfDivd=divd.length();
		char c=divd.charAt(0);
		divd=divd.replaceFirst(c+"",c+".");
		sb.append(divd);
		expNum+=lenOfDivd-1;
		if(expNum>=0)
		{
			sb.append("*10^("+expNum+")");
		}
		else {
			sb.append("*10^(-"+expNum+")");
		}
		if(Integer.parseInt(divisor)!=1)
		{
			sb.append("/");
			sb.append(divisor);
		}
		return sb.toString();
		
	}
}

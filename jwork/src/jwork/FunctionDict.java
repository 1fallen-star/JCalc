package jwork;
import java.util.Map;
import java.util.HashMap;
//Singleton pattern
public class FunctionDict {
	private static FunctionDict instance = new FunctionDict();
	private Map<String, String> funcStr = new HashMap<String, String>();
	private FunctionDict(){
	this.funcStr.put("e","?");//e
    this.funcStr.put("sin","1@");//sin
    this.funcStr.put("cos","1#");//cos
    this.funcStr.put("tan","1\\$");//tan
    this.funcStr.put("ln","1%");//ln
    this.funcStr.put("\\^","&");//pow
    this.funcStr.put("ata","1~");//arctan
    //this.funcStr.put("'",">1");//differential
    this.funcStr.put("Pi",";");//Pi   
    //map set function dictionary
	}
	public static FunctionDict getInstance() {
        return instance;
    }
//	 private String[][]funcStr= {{"sin","1@"},{"cos","1#"},{"tan","1\\$"},{"ln","1%"},
//	    		{"exp","1:"},{"\\^","&"},{"ata","1~"},{"Pi",";"},{"e","?"},{"'",">1"}};
	 //function dict 
	 //IMPORTANT!:When you use replaceAll to replace character^$/,you must add \\ front of it
	 Map<String, String> getFuncDict() {
		 return this.funcStr;
	 }
	 void setFuncDict(Map<String, String> funcdict) {
		 this.funcStr=funcdict;
	 }
	 void appendFuncDict(String appendFuncDictKey,String appendFuncDictVal)
	 {
		 this.funcStr.put(appendFuncDictKey, appendFuncDictVal);
	 }
	 public String toString(){	
		 String result="";
		 for(String key : funcStr.keySet()){
			   result=key+" "+result;
			}
		 return result;
	 }
	 public String replaceStr(String origin)
	 {
		 	String newString=origin;
	        for (Map.Entry<String, String> entry : this.funcStr.entrySet()) {
	            String key = entry.getKey();
	            String value = entry.getValue();
	            newString = newString.replaceAll(key, value);
	        }
	        return newString;
	 }
}

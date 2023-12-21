package jwork;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;
import javax.swing.*;
import java.awt.*;
//version - 1.1
public class CalcConsole extends JFrame {
	private static  int commandCount=0;
	private static final long serialVersionUID = 7056151325900741455L;
	/* textField is used to place input data
	 * textArea is used to place result data
	 * */
    private JTextField textField;
    private JTextArea textArea;
    public CalcConsole() {
        setTitle("SciComputation_version1.1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textField = new JTextField(50);
        textArea = new JTextArea(25, 50);
        textArea.setEditable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(textField, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(panel);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText();
                String res="";
                String currentText = textArea.getText();
                FunctionDict funcStr= FunctionDict.getInstance();
                String[] parameters=input.split(" ");
                int lenOfParameters=parameters.length;
                boolean ifCle=false;
                switch(parameters[0]) {
                case "\\dict" :
                {
                	res=funcStr.toString()+"\n补充说明:其中Pi,e为常数,\\^请视作^,使用函数时格式仿照sin(x)";
                	textField.setText("\\");
                	commandCount++;
                	break;
                }
                case "\\help":
                {
                	res="帮助:"
                			+ "\n命令列表:\n(1):\\dict 获取函数列表及输入格式"
                			+ "\n(2)\\clear 清空当前面板"
                			+"\n(3):\\help 获取帮助"
                			+"\n(4):\\int inf sup eps assumption inp 计算定积分数值解"
                			+ "\ninf:积分下界    sup:积分上界    eps:最大容许误差(输入的正整数值为精确到小数点后几位)    \nassumption:预设积分值(好的初值可以有效加速计算)    inp:待计算式"
                			+ "\n计算示例:\\int 0 1 2 1 sin(e^(x^2))"
                			+"\n(5):\\elem inp 计算常规初等运算,如sin(Pi)"
                			+"\ninp 同上"
                			+ "\n(6):\\linear A b 计算线性方程组"
                			+ "\nAx=b"
                			+ "\n计算示例:\\linear 1,2;3,4; 1,5;"
                			+ "\n(7):\\exact inp 高精度运算"
                			+ "\n inp 同上"
                			+ "\n(8):\\export path 导出计算结果到指定路径,若path为空则生成在当前文件目录下"
                			+ "\n 示例:\\export C:    该命令将导出结果C:\\ComputeResult.txt"
                			+"\n输入注意:\n(1)多参数计算时请用space键分割参数"
                			+ "\n(2)高精度运算时请确保数据位数小于868万位"
                			+ "\n(3)指数运算2^sin(2)时请写成2^(sin(2))的形式"
                			+"\n(4)arctan(x)应写为ata(x)";
                	textField.setText("\\");
                	commandCount++;
                	break;
                }
                case "\\exact":
                {
                	if(lenOfParameters!=2)
                	{	
                		textField.setText("\\");
                		res="语法错误，参数缺失";
                		break;
                	}
                	String inp =parameters[1];
            		inp=funcStr.replaceStr(inp);//normalize
            		
            		 List<String> items = new ArrayList<String>();
            		 FunctionExecute funcFind=new FunctionExecute();
            		 try{
            			 items=funcFind.polishNotation(inp); 
            			  SymbolicComputation sybCompute=new SymbolicComputation();
            			  res=sybCompute.compute(items);
            			 }
            		 catch(IllegalArgumentException e1)
            		 {
            			 res="请检查数据是否正确,详细信息:"+e1;
            		 }
            		 catch(EmptyStackException e3) {
                 		res="请检查数据是否正确,详细信息:空栈";
                 	}
            		 textField.setText("\\exact ");
            		 //System.out.println(items);//debug
            		 break;
                }
                case "\\int":{
                	if(lenOfParameters!=6)
                	{
                		textField.setText("\\");
	                	res="语法错误，参数缺失";
	                	break;
                	}
                	FunctionExecute inte=new FunctionExecute();
                    String sup=parameters[2];
                    String inf=parameters[1];
                    String eps=parameters[3];
                    if(!eps.matches("^[0-9]*$")) {
                    	textField.setText("");
	                	res="误差格式错误，请输入整数";
	                	break;
        		 	}
                    int eps1=Integer.parseInt(eps);
                    double eps2=Math.pow(10, -eps1);
                    String assumption=parameters[4];
                    String inp=parameters[5];
                        	sup=funcStr.replaceStr(sup);
                        	inf=funcStr.replaceStr(inf);
                        	inp=funcStr.replaceStr(inp);//normalize,replace function to reserved word
                        	List<String> items = new ArrayList<String>();
                        	try{
                        		items=inte.polishNotation(inf);   
	                        	double t1=inte.finalCalcGeneral(items);
	                        	items.clear();
	                        	items=inte.polishNotation(sup);   
	                        	double t2=inte.finalCalcGeneral(items);
	                        	double outNum =inte.asr(t1,
                      			   t2,eps2,
                      			   Double.parseDouble(assumption),inp);
                      	 //System.out.println(items);//debug
                      	  if(t1<0) {
                      		  res="积分下界为负结果可能出错"+outNum;
                      		  } 
                      	  else{
                      		  res=""+outNum;}  
                        	}
                        	catch(IllegalArgumentException e2) {
                        		res="请检查数据是否正确,详细信息:"+e2;
                        	}
                        	catch(EmptyStackException e3) {
                        		res="请检查数据是否正确,详细信息:空栈";
                        		}
                        	textField.setText("\\int ");
                        	commandCount++;
                        	break;
                        		}
                case "\\linear":
                {
                	if(lenOfParameters!=3)
                	{	
                		textField.setText("\\");
                		res="语法错误，参数缺失";
                		break;
                	}
                	String inputLinear1=parameters[2];//b
                	String inp=parameters[1];//A
           	        LinearCalculate linea=new LinearCalculate();
           	        int num=linea.findSign(inputLinear1);
           	        if(num*num!=linea.findSign(inp))
           	        {
           	        	res="请检查数据是否正确,列主元消元法需要的矩阵形状不匹配";
           	        }
           	        else {
           	        	try {
           	        			double[][] m=linea.Matr(inp,num,num);
           	        			double [][]n=linea.Matr(inputLinear1, 1, num);
           	        			double[] x =linea.linearCalc(m,n);
           	        			res=Arrays.toString(x);
           	        }
           	        	catch (IllegalArgumentException e4) {
           	        			res="请检查数据是否正确,详细信息:矩阵语法错误"+e4;
           	        	}     	
           	        }
           	        textField.setText("\\linear ");
           	     commandCount++;
           	        break;
           	        }
                case "\\elem" : 
                {
                	if(lenOfParameters!=2)
                	{
                		res="语法错误，参数缺失";
                		break;
                	}
                        	String inp =parameters[1];
                        	if(inp.matches(".*[^\\x23-\\x40costanePil^]+.*"))
                        	{
                        		res="输入数据含有未知符号";
                        		break;
                        	}
                    		inp=funcStr.replaceStr(inp);//normalize
                    		 List<String> items = new ArrayList<String>();
                    		 
                    		 FunctionExecute funcFind=new FunctionExecute();
                    		 try{
                    			 	items=funcFind.polishNotation(inp); 
                    			 	
                    			 	res=""+funcFind.finalCalcGeneral(items);
                    			 }
                    		 catch(IllegalArgumentException e1)
                    		 {
                    			 res="请检查数据是否正确,详细信息:"+e1;
                    		 }
                    		 catch(EmptyStackException e3) {
                         		res="请检查数据是否正确,详细信息:空栈";}
                    		 //System.out.println(items);//debug
                    		 textField.setText("\\elem ");
                    		 commandCount++;
                    		 break;
                	}
                case "\\export" :{
                	String inp="";
                	if(lenOfParameters==2) {
                		inp =parameters[1];
                	}
                	else{
                		inp= System.getProperty("user.dir");
                	}
                	try {
                		FileWriter fw=new FileWriter(inp+"\\ComputeResult.txt");
                		fw.write(textArea.getText()+"");
                		fw.close();
                		
                		res="导出成功";
                		}
                	catch(IOException e1)
                		{
                			res="导出失败:"+e1;
                		}
                	textField.setText("\\");
                	commandCount++;
                	break;
                		}
                case "\\clear":
                {res="";
                	ifCle=true;
                	textField.setText("");
                	commandCount=0;
                	break;
                }
                case "\\exit":
                {	
                	 System.exit(0);  
                }
                default :
                {	
                	textField.setText("\\");
                	res="输入语法错误,可输入\\help获取帮助";
                	break;
                }
                }
                String newText="";
                if(ifCle==false) {
                	 newText = "["+commandCount+"]\n"+input + "\n" + res+"\n\n"+currentText;
                }
                textArea.setText(newText);
                textArea.setCaretPosition(0);

             
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String []args)
    {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalcConsole();
            }
        });
    }
    }
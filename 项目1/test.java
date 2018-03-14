
import java.io.*;
import java.util.*;
import java.util.regex.*;



public class test 
{
    /**
	*  读一行
    */
    public static void readFileByLines(String fileName) 
    {
		try 
		{
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{
				Check_Formula check_form = new Check_Formula();
				boolean ok = check_form.checkExpression(line);

				if (ok)
				{
			        TestStack testStacknew = new TestStack(line);  
			        testStacknew.analysisString();  
				}
				else
				{
			        System.out.print(line);
			    }
		        System.out.print('\n');
			}
			fileReader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }

    public static void main(String[] args) 
    {
    	System.out.println("input 1 to make the correct formula");
    	System.out.println("input 0 to make the error formula");

    	Scanner sc = new Scanner(System.in);	
    	int i = sc.nextInt();

    	System.out.println("input the number of formula");
    	int n = sc.nextInt();
    	if (i == 1)	
    	{
    		FormulaMake formula_object = new FormulaMake();
    		formula_object.make_correct_form(n);
			readFileByLines("formula.txt");
    	}
    	else
    	{
    		FormulaMake formula_object = new FormulaMake();
    		formula_object.make_error_form(n);
			readFileByLines("formula.txt");
    	}
    }
}

class TestStack 
{  
  
    private String testString = null;  
    private Stack<Character> stack = null;  
  

    public TestStack(String testString) 
    {  
        this.testString = testString;  
        this.stack = new Stack<Character>();  
    }  
  
    public void analysisString() 
    {  
        for (int i = 0; i < testString.length(); i++) 
        {  
            char c = testString.charAt(i);  
            if (c == '+' || c == '-') 
            {  
                if (stack.isEmpty() || stack.peek() == '(') 
                {  
                    stack.push(c);  
                } 
                else 
                {  
                    while (!stack.isEmpty()  
                            && (stack.peek() == '*' || stack.peek() == '/'  
                                    || stack.peek() == '+' || stack.peek() == '-')) 
                    {  
                        System.out.print(stack.pop());  
                    }  
                    stack.push(c);  
                }  
            } 
            else if (c == '*' || c == '/') 
            {  
                if (stack.isEmpty() || stack.peek() == '+'  
                        || stack.peek() == '-' || stack.peek() == '(') 
                {  
                    stack.push(c);  
                } 
                else 
                {  
                    while (!stack.isEmpty()  
                            && (stack.peek() == '/' || stack.peek() == '*')) 
                    {  
                        System.out.print(stack.pop());  
                    }  
                    stack.push(c);  
                }  
            } 
            else if (c == '(') 
            {  
                stack.push(c);  
            } 
            else if (c == ')') 
            {  
                char temp = ' ';  
                while ((temp = stack.pop()) != '(') 
                {  
                    System.out.print(temp);  
                }  
            } 
            else 
            {  
                System.out.print(c);  
            }  
        }  
        if (!stack.isEmpty()) 
        {  
            while (!stack.isEmpty()) 
            {  
                System.out.print(stack.pop());  
            }  
        }  
    }  
  
} 

class Check_Formula 
{
	/**
	* 检查表达式是否合法，合法返回true
	* @param expression
	* @return
	*/
	public boolean checkExpression(String expression) 
	{
	    //去除空格
	    expression = expression.replaceAll(" ","");

	    Set<Character> operate_set = new HashSet<Character>();
	    operate_set.add('-');
	    operate_set.add('+');
	    operate_set.add('*');
	    operate_set.add('/');

	    //拆分字符串
	    char[] arr = expression.toCharArray();
	    int len = arr.length;

	    //前后括号计数，用来判断括号是否合法
	    int checkNum=0;

	    //数字集合
	    List<Character> digit_list = new ArrayList<>();
	    StringBuffer stringBuffer = new StringBuffer();
	    //循环判断
	    for (int i = 0; i < len; i++) 
	    {
	        if(Character.isDigit(arr[i]) || arr[i] == '.')
	        { //数字和小数点判断
	            //把数字和小数点加入到集合中，为了下一步判断数字是否合法
	            digit_list.add(arr[i]);
	        }
	        else 
	        { //非数字和小数点
	            //如果集合中有值，取出来判断这个数字整体是否合法
	            if(digit_list.size() > 0) 
	            {
	                //判断字符串是否合法
	                boolean result = getNumberStringFromList(digit_list);
	                if(result)
	                {
	                    //如果合法，清空集合，为了判断下一个
	                    digit_list.clear();
	                }
	                else{
	                    //不合法，返回false
	                    return false;
	                }
	            }

	            if (arr[i] == '+' || arr[i] == '*' || arr[i] == '/') 
	            {
	                //判断规则(1.不能位于首位 2.不能位于末尾 3.后面不能有其他运算符 4.后面不能有后括号)
	                if (i == 0 || i == (len - 1) || operate_set.contains(arr[i + 1]) || arr[i + 1] == ')') 
	                {
	                    System.out.println("error type : '+' or '*' or '/' ->"+ arr[i]);
	                    return false;
	                }
	            } 
	            else if (arr[i] == '-') 
	            {
	                //减号判断规则(1.不能位于末尾 2.后面不能有其他运算符 3.后面不能有后括号)
	                if (i == (len - 1) || operate_set.contains(arr[i + 1]) || arr[i + 1] == ')') 
	                {
	                    System.out.println("error type : '-' ->"+ arr[i]);
	                    return false;
	                }

	            } 
	            else if (arr[i] == '(') 
	            {
	                checkNum++;
	                //判断规则(1.不能位于末尾 2.后面不能有+，*，/运算符和后括号 3.前面不能为数字)
	                if (i == (len - 1) || arr[i + 1] == '+' || arr[i + 1] == '*' || arr[i + 1] == '/' || arr[i + 1] == ')'||(i != 0 && Character.isDigit(arr[i-1]))) 
	                {
	                    System.out.println("error type : '(' ->"+ arr[i]);
	                    return false;
	                }
	            } 
	            else if (arr[i] == ')') 
	            {
	                checkNum--;
	                //判定规则(1.不能位于首位 2.后面不能是前括号 3.括号计数不能小于0，小于0说明前面少了前括号)
	                if (i == 0 || (i < (len - 1) && arr[i + 1] == '(') || checkNum < 0) 
	                {
	                    System.out.println("error type : ')' ->"+ arr[i]);
	                    return false;
	                }
	            }
	            else
	            {
	                //非数字和运算符
	                System.out.println("error type : not number and operator ->"+ arr[i]);
	                return false;
	            }
	        }
	    }

	    //如果集合中有值，取出来判断这个数字整体是否合法
	    if(digit_list.size() > 0) 
	    {
	        //判断字符串是否合法
	        boolean result = getNumberStringFromList(digit_list);
	        if(result) 
	        {
	            //如果合法，清空集合，为了判断下一个
	            digit_list.clear();
	        }
	        else
	        {
	            //不合法，返回false
	            return false;
	        }
	    }

	    //不为0,说明括号不对等，可能多前括号
	    if(checkNum != 0)
	    {
	        return false;
	    }
	    return true;
	}


	/** 
	* 判断字符串是否为整数，浮点数类型，是返回true 
	* @param str 
	* @return 
	*/
	public static boolean isNumber(String str)
	{ 
		Pattern pattern = Pattern.compile("^-?([1-9]\\d*\\.\\d+|0\\.\\d*[1-9]\\d*|[1-9]\\d*|0)$"); 
		Matcher isNum = pattern.matcher(str); 
		if(!isNum.matches())
		{ 
			return false; 
		} 
		return true;
	}

	/**
	 * 把集合中的字符，拼接成字符串，并校验字符串是否是数值
	 * @param list
	 * @return
	 */
	private boolean getNumberStringFromList(List<Character> list)
	{
	    //如果集合中有值，取出来判断这个数字整体是否合法
	    if(list != null) 
	    {
	        StringBuffer stringBuffer = new StringBuffer();
	        for(Character character : list)
	        {
	            stringBuffer.append(character);
	        }
	        //正则判断数字是否合法
	        boolean result = isNumber(stringBuffer.toString());

	        if(!result)
	        {
	            System.out.println("error type: digit ->"+stringBuffer.toString());
	        }
	        return result;
	    }
	    return false;
	}
}

class FormulaMake
{


    /*
    * @param num 表达式个数
    *
    */
    
    public static void make_correct_form(int num_of_form)
    {
    	try
    	{
	    	PrintWriter out = new PrintWriter("formula.txt");
	    	while (num_of_form-- > 0)
	    	{
		    	Random rand = new Random();
		    	String result = "";
		    	char[] c = {'+', '-', '*', '/'};  
		    	int num_item = rand.nextInt(20) + 2;  // 表达式项数2到22

		    	int num = rand.nextInt(500) + 1;
		    	result = result + Integer.toString(num);
		    	num_item--;
		    		
		    	while (num_item-- > 0)
		    	{
			    	int c_item = rand.nextInt(50) % 4;
			    	char symbol = c[c_item];
			    	num = rand.nextInt(500) + 1;
			    	result = result + symbol + Integer.toString(num);
		    	}
		    	out.println(result);
	    	}
	    	out.close();
    	}
    	catch(IOException e)
    	{}

    }

    public static void make_error_form(int num_of_form)
    {
    	try
    	{
	    	PrintWriter out = new PrintWriter("formula.txt");
	    	while (num_of_form-- > 0)
	    	{
		    	Random rand = new Random();
		    	String result = "";
		    	char[] c = {'+', '-', '*', '/'};  
		    	int num_item = rand.nextInt(20) + 2;  // 表达式项数2到22

		    	while (num_item-- > 0)
		    	{
			    	int num = rand.nextInt(500) + 1;
			    	int odd_or_even = rand.nextInt(10);

			    	if (odd_or_even % 2 == 0)
			    	{
				    	result = result + Integer.toString(num);
			    	}
			    	else
			    	{
				    	int c_item = rand.nextInt(50) % 4;
				    	char symbol = c[c_item];
			    		result = result + symbol;
			    	}

		    	}
		    	out.println(result);
	    	}
	    	out.close();
    	}
    	catch(IOException e)
    	{}

    }

}
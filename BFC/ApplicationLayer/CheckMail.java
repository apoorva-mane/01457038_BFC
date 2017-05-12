package BFC;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class CheckMail
{
public static boolean checkMail(String mailid){
	 boolean flag=false;
	 String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	 Pattern p = Pattern.compile(regEx);
	 Matcher m = p.matcher(mailid);
	 if(m.find())
		flag=true;
	 else
		flag=false;
     return flag;
 }
}
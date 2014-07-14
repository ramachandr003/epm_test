package supportlibraries;

/**

 */
public class CustomFunctions
{
		
	public static boolean isNumeric(String strNumber) throws Exception {

		if (strNumber.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")){
	     return true;
		}else{
	    return false;
		}
	}
	
	
}//class end
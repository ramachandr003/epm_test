package supportlibraries;

public enum Browser
{
	//android,
	chrome,
	safari,
	firefox,
	htmlunit,
	iexplore,
	//iPhone,
	//iPad,
	opera;
		
	public static Browser fromString(String text) {
	    if (text != null) {
	      for (Browser b : Browser.values()) {
	        if (text.trim().equalsIgnoreCase(b.toString())) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }

}
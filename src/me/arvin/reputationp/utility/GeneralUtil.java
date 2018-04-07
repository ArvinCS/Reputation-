package me.arvin.reputationp.utility;

public class GeneralUtil {
	public static boolean isInt(Object s) {
	    try {
	        Integer.parseInt((String) s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}

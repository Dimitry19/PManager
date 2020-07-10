package cm.packagemanager.pmanager.common.utils;

public class StringUtils {

	public final static String EMPTY_STR="";

	private StringUtils() {
		super();
	}

	public static boolean isEmpty(String str) {
		return str==null || str.length()==0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String trim(String str){
		if (isEmpty(str)){
			return str;
		}

		return str.trim();
	}


	public static boolean equals(final String cs1, final String cs2) {
		if (cs1 == cs2) {
			return true;
		}
		if (cs1 == null || cs2 == null) {
			return false;
		}

		return cs1.equals(cs2);
	}

	public static String formatZeroString(String str,int maxlenght){
		if (isEmpty(str)){
			return str;
		}

		for(int i = str.length(); i < maxlenght;i++){
			str = "0"+str;
		}

		return str;
	}
}

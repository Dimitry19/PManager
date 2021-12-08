package cm.packagemanager.pmanager.common.utils;

public class StringUtils {

    public final static String EMPTY_STR = "";

    private StringUtils() {
        super();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String trim(String str) {
        if (isEmpty(str)) {
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

    public static String formatZeroString(String str, int maxlenght) {
        if (isEmpty(str)) {
            return str;
        }

        for (int i = str.length(); i < maxlenght; i++) {
            str = "0" + str;
        }

        return str;
    }

    public static String decapitalize(String name) {
        if (name != null && name.length() != 0) {
            if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
                return name;
            } else {
                char[] chars = name.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                return new String(chars);
            }
        } else {
            return name;
        }
    }

    public static String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }


    public static String concatenate(String... str) {
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            sb.append(s);
        }
        return sb.toString();
    }
}

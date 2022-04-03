package cm.framework.ds.hibernate.utils;

import org.apache.commons.lang3.StringUtils;

public class SQLUtils {
	private static final String LIKE_CHARS = "%";

	public SQLUtils() {
	}

	public static String forLike(String search, boolean caseInsensitive,  boolean before,boolean after) {
		if (StringUtils.isEmpty(search)) {
			return LIKE_CHARS;
		} else {
			StringBuilder sb = new StringBuilder();
			if (before) {
				sb.append(LIKE_CHARS);
			}

			sb.append(caseInsensitive ? search.toUpperCase() : search);
			if (after) {
				sb.append(LIKE_CHARS);
			}

			return sb.toString();
		}
	}
}

package cm.packagemanager.pmanager.common.utils;

import java.util.Collection;


public class CollectionsUtils {

	public static Object getFirstOrNull(Collection<?> elements){

		if(isNotEmpty(elements)){
			return elements.iterator().next();
		}
		return null;
	}

	public static boolean isNotEmpty(Collection<?> elements){

		return !isEmpty(elements);
	}

	public static boolean isEmpty(Collection<?> elements){

		return elements == null || elements.isEmpty();
	}
}

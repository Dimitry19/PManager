package cm.packagemanager.pmanager.common.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionsUtils {

	public static Object getFirstOrNull(Collection<?> elements){

		if(isNotEmpty(elements)){
			return elements.iterator().hasNext();
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

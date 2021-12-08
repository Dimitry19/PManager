package cm.packagemanager.pmanager.common.utils;

import java.util.Arrays;
import java.util.Collection;


public class CollectionsUtils {

    public static Object getFirstOrNull(Collection<?> elements) {

        if (isNotEmpty(elements)) {
            return elements.iterator().next();
        }
        return null;
    }

    public static Object getFirst(Collection<?> elements) {
        return elements.iterator().next();
    }


    public static boolean isNotEmpty(Collection<?> elements) {

        return !isEmpty(elements);
    }

    public static boolean isEmpty(Class[] elements) {
        return isEmpty(Arrays.asList(elements));
    }

    public static boolean isNotEmpty(Class[] elements) {
        return isNotEmpty(Arrays.asList(elements));
    }

    public static boolean isEmpty(Collection<?> elements) {

        return elements == null || elements.isEmpty();
    }

    public static int size(Collection<?> elements) {

        return isEmpty(elements) ? 0 : elements.size();

    }
}

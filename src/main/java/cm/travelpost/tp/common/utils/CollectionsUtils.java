package cm.travelpost.tp.common.utils;


import java.util.*;
import java.util.stream.Collectors;


public class CollectionsUtils {

    static String [] array;

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

    public static void makeCopies(String[] source, List destination) {
        if(isEmpty(destination)){
            destination = new ArrayList();
        }
         array = Arrays.copyOf(source, source.length);

        Collections.addAll(destination, source);
    }

    public static int size(Collection<?> elements) {

        return isEmpty(elements) ? 0 : elements.size();

    }

    public static int size(Class<?> [] elements) {

        return isEmpty(elements) ? 0 : Arrays.asList(elements).size();

    }

    public static boolean isUnique(Collection<?> elements) {

        return size(elements)== 0;
    }

    public static boolean contains(Collection<?> elements,Object o){
         return  isNotEmpty(elements) && elements.contains(o);
    }

    public static boolean notContains(Collection<?> elements,Object o){
        return  !contains(elements,o);
    }

    public static List<?> convertToList(Collection<?> elements){
        if(isEmpty(elements)){
            return new ArrayList<>();
        }
        return elements.stream().collect(Collectors.toList());
    }
}

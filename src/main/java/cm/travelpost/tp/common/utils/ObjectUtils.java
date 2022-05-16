package cm.travelpost.tp.common.utils;


import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
    private static final Logger log = LoggerFactory.getLogger(ObjectUtils.class);

    public static final String CLASS_TO_INVOKE_BLOCK_LABEL = "BlockTagLabel";
    private static final String PREPARA_ETICHETTE_X_BLOCCHETTI_METHOD="preparaEtichetteBlocchetti";

    //if (ObjectUtils.isCallable(activityFindWrapper,"article.id.itemCode") || ObjectUtils.isCallable(activityFindWrapper,"articleFather.id.itemCode")) How to use

    public ObjectUtils() {
    }

    public static boolean isCallable(Object obj, String path) {
        try {
            if (obj == null) {
                throw new RuntimeException("Evaluate object is null");
            } else if (StringUtils.isEmpty(path)) {
                throw new RuntimeException("Path is empty");
            } else {
                String[] token = path.split("\\.");
                if (ArrayUtils.isEmpty(token)) {
                    token = new String[]{path};
                }

                Object currentObj = obj;
                String[] tokens = token;
                int length = token.length;

                for (int i = 0; i < length; ++i) {
                    String methodName = tokens[i];
                    Method method = currentObj.getClass().getMethod("get" + StringUtils.capitalize(methodName));
                    if (method == null) {
                        throw new RuntimeException("Path is invalid");
                    }

                    currentObj = method.invoke(currentObj);
                    if (currentObj == null) {
                        return false;
                    }
                }

                return true;
            }
        } catch (Throwable th) {
            log.error("Erreur, veuillez verifier le path valorisé", th);
            throw new RuntimeException("Erreur, veuillez verifier le path valorisé", th);
        }
    }


    public static boolean isNotNullable(Object obj) {
        return obj!=null;
    }



    private static Object invokeMethod(String pathAndClassName, String methodName, Class paramsClass[], Object[] params) throws Exception {


        Class clazz = Class.forName(pathAndClassName);
        Object iClass = clazz.newInstance();
        Method m = clazz.getDeclaredMethod(methodName, paramsClass);

        Object ret = m.invoke(iClass, params);

        return ret;

    }
    public  void preparaListaEtichette( List etichette, String tipoStampa,List errors) throws Exception{

        Map etichetteStampare = new HashMap();

        Object ret = null;


        if(StringUtils.equals(tipoStampa, "TipoStampaLotti.BLOCCHETTI")){
            Class []paramsClass = {List.class};
            ret = invokeMethod(CLASS_TO_INVOKE_BLOCK_LABEL, PREPARA_ETICHETTE_X_BLOCCHETTI_METHOD, paramsClass, new Object[]{etichette});
        }

    }
}

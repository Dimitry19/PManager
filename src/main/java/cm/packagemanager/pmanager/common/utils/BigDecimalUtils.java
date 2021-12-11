package cm.packagemanager.pmanager.common.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static BigDecimal convertStringToBigDecimal(String value) {

        if (StringUtils.isEmpty(value)) return null;

        return new BigDecimal(value);
    }


    /**
     * Retourne true si la valeur du premier argument est superieure à celle du second
     *
     * @param compare1
     * @param compare2
     * @return
     */
    public static boolean greatherThan(BigDecimal compare1, BigDecimal compare2) {
        return compare1.compareTo(compare2) > 0;
    }

    /**
     * Retourne true si la valeur du premier argument est inferieure à celle du second
     *
     * @param compare1
     * @param compare2
     * @return
     */
    public static boolean lessThan(BigDecimal compare1, BigDecimal compare2) {
        return compare1.compareTo(compare2) < 0;
    }
}

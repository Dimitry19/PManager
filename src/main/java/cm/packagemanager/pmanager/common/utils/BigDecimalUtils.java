package cm.packagemanager.pmanager.common.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

	public static BigDecimal convertStringToBigDecimal(String value){

		if (StringUtils.isEmpty(value)) return null;

		return new BigDecimal(value);
	}

	public static boolean greatherThan( BigDecimal compare1, BigDecimal compare2){
		return compare1.compareTo(compare2)>0;
	}

	public static boolean lessThan( BigDecimal compare1, BigDecimal compare2){
		return compare1.compareTo(compare2)<0;
	}
}

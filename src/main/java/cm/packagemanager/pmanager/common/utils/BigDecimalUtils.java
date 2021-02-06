package cm.packagemanager.pmanager.common.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

	public static BigDecimal convertStringToBigDecimal(String value){

		if (StringUtils.isEmpty(value)) return null;

		return new BigDecimal(value);
	}
}

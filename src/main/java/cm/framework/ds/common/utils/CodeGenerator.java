package cm.framework.ds.common.utils;

import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.utils.SecRandom;

import java.util.Calendar;

public class CodeGenerator {

	public static String generateCode(String token, String type){

		String  year =String.valueOf(DateUtils.gregorianCalendar(Calendar.YEAR)).substring(2);
		String random = SecRandom.randomString(5).toUpperCase();
		String part= StringUtils.length(type)==1 ? type: type.substring(0,1);
		return StringUtils.concatenate(token,part,random, year);
	}
}

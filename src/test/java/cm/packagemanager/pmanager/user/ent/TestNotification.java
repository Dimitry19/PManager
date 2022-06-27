package cm.packagemanager.pmanager.user.ent;


import cm.travelpost.tp.common.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TestNotification {

	static List<String> putIds;
	public static void main(String[] args) {

		 TimeZone tz = TimeZone.getDefault();


		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(tz);
		//calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		 System.out.println(calendar.getTime());
		System.out.println("Custom "+ DateUtils.dateWithoutTime(new Date()));
		System.out.println(DateUtils.milliSecondToDate(1648980069883L));
	}
}

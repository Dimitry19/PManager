package cm.framework.ds.h2.dialect;

import org.hibernate.dialect.H2Dialect;

public class PersonalH2Dialect extends H2Dialect {

	@Override
	public String toBooleanValueString(boolean bool) {
		return bool ? "true" : "false";
	}

}
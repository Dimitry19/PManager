package cm.packagemanager.pmanager.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {




	public  static final String BUYER="B";
	public  static final String SELLER="S";
	public  static final String AP="A";
	public  static final String AUT="V";
	public  static final String NV ="N";
	public  static final String SUBJECT ="SUBJECT";
	public  static final String DEFAULT_TOKEN ="PM";

	@Value("${constant.prod_cat_code}")
	public  String DEFAULT_PROD_CAT_CODE;


}

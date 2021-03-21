package cm.packagemanager.pmanager.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {




	public  static final String BUYER="BUYER";
	public  static final String SELLER="SELLER";
	public  static final String AP="A";
	public  static final String AUT="V";
	public  static final String NV ="N";
	public  static final String SUBJECT ="SUBJECT";
	public  static final String DEFAULT_TOKEN ="PM";

	public  static final String AND =" and ";
	public  static final String OR =" or ";

	@Value("${constant.prod_cat_code}")
	public  String DEFAULT_PROD_CAT_CODE;


	public static final int CONTENT_LENGTH=500;
	public static final int EMAIL_LENGTH=50;


	public static final String USER_TYPE_IMG_UPLOAD="USER";
	public static final String ANNOUNCE_TYPE_IMG_UPLOAD="ANNOUNCE";



}

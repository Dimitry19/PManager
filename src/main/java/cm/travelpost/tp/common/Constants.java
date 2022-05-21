package cm.travelpost.tp.common;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.ManagedBean;

@ManagedBean
public class Constants {

    @Value("${constant.prod_cat_code}")
    public String defaultCategorie;

    public static final String SUBJECT = "SUBJECT";
    public static final String DEFAULT_TOKEN = "TP";
    public static final int TP_ACTIVATE_ACCOUNT =1;

    public static final int CONTENT_LENGTH = 500;

    public static final String USER_TYPE_IMG_UPLOAD = "USER";
    public static final String ANNOUNCE_TYPE_IMG_UPLOAD = "ANNOUNCE";
    public static final String CATEGORY_INDIFFERENT = "Indifférent";



}

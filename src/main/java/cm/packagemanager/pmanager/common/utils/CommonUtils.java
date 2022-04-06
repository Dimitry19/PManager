package cm.packagemanager.pmanager.common.utils;

import cm.packagemanager.pmanager.user.ent.vo.RoleVO;

public class CommonUtils {


    public static final String ROLE_ADMIN="ROLE_ADMIN";
    public static final String ROLE_USER="ROLE_ADMIN";

    public static final String claimsAdminKey="isAdmin";
    public static final String claimsUserKey="isUser";


    public static  String decodeRole(RoleVO role){
        if(role==null) return  null;

            switch (role.getDescription()){
                case ADMIN:
                    return ROLE_ADMIN;
                default:
                        return ROLE_USER;

            }

    }
}

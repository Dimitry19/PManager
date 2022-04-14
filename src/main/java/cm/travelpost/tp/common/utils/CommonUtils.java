package cm.travelpost.tp.common.utils;

import cm.travelpost.tp.user.ent.vo.RoleVO;

import static cm.travelpost.tp.common.enums.RoleEnum.ADMIN;

public class CommonUtils {


    public static final String ROLE_ADMIN="ROLE_ADMIN";
    public static final String ROLE_USER="ROLE_ADMIN";

    public static final String claimsAdminKey="isAdmin";
    public static final String claimsUserKey="isUser";

    private CommonUtils(){}


    public static  String decodeRole(RoleVO role){
        if(role==null) return  null;

        return (role.getDescription() == ADMIN) ? ROLE_ADMIN:ROLE_USER;
    }
}

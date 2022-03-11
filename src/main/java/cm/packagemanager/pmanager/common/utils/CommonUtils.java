package cm.packagemanager.pmanager.common.utils;

import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

public class CommonUtils {


    public static final String ROLE_ADMIN="ROLE_ADMIN";
    public static final String ROLE_USER="ROLE_ADMIN";

    public static  String decodeRole(RoleVO role){
        if(role==null) return  null;

            switch (role.getDescription()){
                case ADMIN:
                    return ROLE_ADMIN;
                case USER:
                    return ROLE_USER;
                    default:
                        return ROLE_USER;

            }

    }
}

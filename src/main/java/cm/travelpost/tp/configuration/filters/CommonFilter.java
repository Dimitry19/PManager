package cm.travelpost.tp.configuration.filters;


import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.CommonUtils;
import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonFilter extends AFilter{

    private static Logger logger = LoggerFactory.getLogger(CommonFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.debug(" ");
    }

    @Override
    public void destroy() {
        logger.debug("Destroy CommonFilter filter");
    }

    public  static List getRolesAuthoritiesUser(UserVO user){

        return getRoles(user);
    }

    @NotNull
    static List getRoles(UserVO user) {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        if(CollectionsUtils.isUnique(user.getRoles())){
            RoleVO role = (RoleVO)CollectionsUtils.getFirstOrNull(user.getRoles());
            return Arrays.asList(new SimpleGrantedAuthority(CommonUtils.decodeRole(role)));
        }
        user.getRoles().stream().forEach(r->roles.add(new SimpleGrantedAuthority(CommonUtils.decodeRole(r))));

        return roles;
    }
}

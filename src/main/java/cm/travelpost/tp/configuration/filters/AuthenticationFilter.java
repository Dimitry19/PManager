package cm.travelpost.tp.configuration.filters;


import cm.travelpost.tp.common.enums.RoleEnum;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.user.ent.service.UserService;
import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.constant.WSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends CommonFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    UserService userService;

    @Value("${tp.travelpost.postman.enable}")
    private boolean postman;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("########## Initiating AuthenticationFilter filter ##########");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

        try {
            if(postman){
                filterChain.doFilter(request, response);
                return;
            }
            if(!authorized(servletRequest,  servletResponse)){
                  return;
              }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //call next filter in the filter chain
        filterChain.doFilter(request, response);

        logger.info("Logging Response :{}", response.getContentType());

    }

    @Override
    public void destroy() {

    }

    private boolean authorized(ServletRequest servletRequest,ServletResponse servletResponse) throws Exception {



        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri=request.getRequestURI();
        String apiKey=request.getHeader(tokenName);
        boolean isService=uri.contains(service);
        boolean isConfirm=uri.contains("confirm");
        boolean isNotApiKey=(StringUtils.isEmpty(apiKey)|| !apiKey.equals(token));
        boolean isNotUpload=!uri.contains(WSConstants.UPLOAD);
        boolean isDashBoard=uri.contains(WSConstants.DASHBOARD);
        String username=request.getHeader(sessionHeader);
        boolean isAdminRole=false;


//        System.out.println("uri: "+uri);
//        System.out.println("apiKey: "+apiKey);
//        System.out.println("isService: "+isService);
//        System.out.println("isConfirm: "+isConfirm);
//        System.out.println("isNotApiKey: "+isNotApiKey);
//        System.out.println("isNotUpload: "+isNotUpload);
//        System.out.println("isDashBoard: "+isDashBoard);
//        System.out.println("username: "+username);


        if(StringUtils.isNotEmpty(username)){
            // Ici  je verifie si l'utilisateur a le role ADMIN pour pouvoir acceder ?? la dashboard
            UserVO user=userService.findByUsername(username, Boolean.FALSE);
            if (user!=null) {
                isAdminRole = CollectionsUtils.size(user.getRoles()
                        .stream()
                        .map(RoleVO::getDescription)
                        .filter(r -> r == RoleEnum.ADMIN).collect(Collectors.toList())) != 0;
            }
        }

        if((!isConfirm && isService && isNotApiKey && isNotUpload ) ||(!isAdminRole  && isDashBoard)){
            error(response,false);
            return false;
        }
        return true;
    }
}

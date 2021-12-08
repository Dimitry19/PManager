package cm.packagemanager.pmanager.component;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CrossDomainFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); //toutes les URI sont autorisées
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST,PUT,DELETE");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-req,token");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
package cm.travelpost.tp.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.ORIGIN;


//@Component
public class CrossDomainFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(CrossDomainFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        String origin = httpServletRequest.getHeader(ORIGIN);

        logger.info("Cross Domain origin : {}",origin);
       // httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); //toutes les URI sont autorisées
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST,PUT,DELETE,OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-req,token,content-type, authorization");


        logger.info("Cross Domain method : {}",httpServletRequest.getMethod());


        if (httpServletRequest.getMethod().equals("OPTIONS"))
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        else
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
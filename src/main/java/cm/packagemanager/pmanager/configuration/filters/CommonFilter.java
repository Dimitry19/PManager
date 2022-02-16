package cm.packagemanager.pmanager.configuration.filters;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import java.io.IOException;

public class CommonFilter extends AFilter{



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}

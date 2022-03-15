package cm.packagemanager.pmanager.configuration.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpsEnforcer extends CommonFilter {
	public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";

	private static Logger logger = LoggerFactory.getLogger(HttpsEnforcer.class);


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("########## Initiating HttpsEnforcer filter ##########");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (request.getHeader(X_FORWARDED_PROTO) != null) {

			if (request.getHeader(X_FORWARDED_PROTO).indexOf("https") != 0) {
				String pathInfo = (request.getPathInfo() != null) ? request.getPathInfo() : "/pmanager";
				response.sendRedirect("https://" + request.getServerName() + ":"+request.getServerPort() + pathInfo);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() { }
}
package cm.travelpost.tp.configuration.filters;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter  {

    protected final Log logger = LogFactory.getLog(JwtAuthenticationFilter.class);

}

package cm.travelpost.tp.configuration.filters;


import cm.travelpost.tp.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)*/
public class SessionFilter extends OncePerRequestFilter  {


    protected final Log logger = LogFactory.getLog(SessionFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        try{

            filterChain.doFilter(request, response);
        }catch(ExpiredJwtException ex) {
            request.setAttribute("exception", ex);
            error(response);
            //throw ex;
        }catch(BadCredentialsException ex){
            request.setAttribute("exception", ex);
            error(response);
            //throw ex;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void error(HttpServletResponse response) throws IOException {

        ErrorResponse errorResponse = new ErrorResponse();
        String[] codes=new String[1];
        codes[0]= "401";
        List<String> details= new ArrayList();
        details.add("Token invalide , se connecter de nouveau");
        errorResponse.setCode(codes);
        errorResponse.setDetails(details);
        errorResponse.setMessage(details.get(0));

        byte[] responseToSend = restResponseBytes(errorResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpStatus.SC_GATEWAY_TIMEOUT);
        response.getOutputStream().write(responseToSend);
    }

    protected byte[] restResponseBytes(ErrorResponse errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }


}

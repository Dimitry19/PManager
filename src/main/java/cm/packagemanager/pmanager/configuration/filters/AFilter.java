package cm.packagemanager.pmanager.configuration.filters;

import cm.packagemanager.pmanager.common.exception.ErrorResponse;
import cm.packagemanager.pmanager.common.session.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AFilter implements Filter {

    @Value("${custom.api.auth.http.tokenValue}")
    protected String token;

    @Value("${custom.api.auth.http.tokenName}")
    protected String tokenName;

    @Value("${custom.session.user}")
    protected String sessionHeader;

    @Value("${url.service}")
    protected String service;

    @Autowired
    protected SessionManager sessionManager;

    protected void error(HttpServletResponse response, boolean isSession) throws IOException {

        String[] codes=new String[1];
        codes[0]=!isSession?"401":"400";
        ErrorResponse errorResponse = new ErrorResponse();
        List<String> details= new ArrayList();
        errorResponse.setCode(codes);
        errorResponse.setDetails(details);
        errorResponse.setMessage(!isSession?"Accès non autorisé": "Session invalide , se connecter de nouveau");

        byte[] responseToSend = restResponseBytes(errorResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(!isSession?401:400);
        response.getOutputStream().write(responseToSend);
    }

    private byte[] restResponseBytes(ErrorResponse errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }
}

package cm.travelpost.tp.configuration.filters;

import cm.travelpost.tp.common.exception.ErrorResponse;
import cm.travelpost.tp.common.session.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AFilter  implements IFilter {

    private static Logger log = LoggerFactory.getLogger(AFilter.class);


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

    @Override
    public void error(HttpServletResponse response) throws Exception {

    }

    protected void error(HttpServletResponse response, boolean isSession) throws IOException {

        log.error("Into Unauthorized");

        String[] codes=new String[1];
        codes[0]=BooleanUtils.isFalse(isSession)?"401":"400";
        ErrorResponse errorResponse = new ErrorResponse();
        List<String> details= new ArrayList();
        errorResponse.setCode(codes);
        errorResponse.setDetails(details);
        errorResponse.setMessage(BooleanUtils.isFalse(isSession)?"Accès non autorisé": "Token invalide , se connecter de nouveau");

        byte[] responseToSend = restResponseBytes(errorResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(BooleanUtils.isFalse(isSession)?401:400);
        response.getOutputStream().write(responseToSend);
    }

    private byte[] restResponseBytes(ErrorResponse errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }
}

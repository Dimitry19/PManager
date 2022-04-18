package cm.travelpost.tp.configuration.filters;

import cm.travelpost.tp.common.exception.ErrorResponse;
import cm.travelpost.tp.common.session.SessionManager;
import cm.travelpost.tp.security.jwt.service.JwtService;
import cm.travelpost.tp.security.jwt.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AFilter  implements IFilter {

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

    @Autowired
    JwtService jwtService;


    @Value("${tp.travelpost.app.escape.announces}")
    private String escapeAnnounce;

    @Value("${tp.travelpost.app.escape.other}")
    private String escapeOther;

    @Value("${tp.travelpost.app.escape.home}")
    private String escapeHome;

    @Value("${tp.travelpost.postman.enable}")
    protected boolean postman;


    @Autowired
    JwtTokenUtils tokenUtils;

    @Value("${custom.user.guest}")
    protected String guest;


    @Override
    public void error(HttpServletResponse response) throws Exception {

    }

    protected void error(HttpServletResponse response, boolean isSession) throws IOException {

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

    protected byte[] restResponseBytes(ErrorResponse errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }
}

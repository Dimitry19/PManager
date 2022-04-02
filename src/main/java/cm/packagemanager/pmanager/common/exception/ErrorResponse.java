package cm.packagemanager.pmanager.common.exception;

import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    protected final Log logger = LogFactory.getLog(ErrorResponse.class);

    private String message;
    private List details = new ArrayList();
    private String[] code;
    private int retCode;


    public ErrorResponse() {
        super();

    }

    public ErrorResponse(String message, List details) {
        super();
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(String message, List details, String[] code, int retCode) {
        super();
        this.message = message;
        this.details = details;
        this.code = code;
        this.retCode = retCode;
    }

    public ErrorResponse(Exception ex) {
        logger.error(ErrorResponse.class +" {}" ,ex);
        StringBuilder stringBuilder = new StringBuilder();
        HttpMessageNotReadableException is;
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ob = (MethodArgumentNotValidException) ex;
            List<ObjectError> errors = ob.getBindingResult().getAllErrors();
            if (CollectionsUtils.isNotEmpty(errors)) {
                this.code = new String[errors.size()];
                for (int i = 0; i < errors.size(); i++) {
                    ObjectError objectError = errors.get(i);
                    this.code[i] = objectError.getCode();
                    stringBuilder.append(objectError.getDefaultMessage() + "\n");
                }
            }
        }
        if (ex instanceof HttpMessageNotReadableException) {
            setDefaultCodes();
            HttpMessageNotReadableException ob = (HttpMessageNotReadableException) ex;
            stringBuilder.append(ob.getCause().getMessage());
        }
        if (ex instanceof UnexpectedTypeException) {
            setDefaultCodes();
            UnexpectedTypeException ob = (UnexpectedTypeException) ex;
            stringBuilder.append(ob.getMessage());
        }

        if (ex instanceof NullPointerException) {
            setDefaultCodes("null.pointer.exception");
            NullPointerException ob = (NullPointerException) ex;
            stringBuilder.append(ob.getMessage());
        }

        if (ex instanceof ExpiredJwtException) {
            setDefaultCodes("401");
            ExpiredJwtException ob = (ExpiredJwtException) ex;
            stringBuilder.append(ob.getMessage());
        }

        if (ex instanceof BadCredentialsException) {
            setDefaultCodes("401");
            BadCredentialsException ob = (BadCredentialsException) ex;
            stringBuilder.append(ob.getMessage());
        }

        if (ex instanceof NullPointerException) {
            setDefaultCodes("null.pointer.exception");
            NullPointerException ob = (NullPointerException) ex;
            stringBuilder.append(ob.getMessage());
        }

        if (ex instanceof AnnounceException) {
            setDefaultCodes("announce.exception");
            AnnounceException ob = (AnnounceException) ex;
            stringBuilder.append(ob.getMessage());
        }
        stringBuilder.append(ex.getMessage());
        this.message = stringBuilder.toString();
        this.retCode = -1;
        this.details.add(stringBuilder.toString());

    }

    void setDefaultCodes() {
        this.code = new String[1];
        this.code[0] = "Generic error";
    }

    void setDefaultCodes(String code) {
        this.code = new String[1];
        this.code[0] = code;
    }


    public String[] getCode() {
        return code;
    }

    public void setCode(String[] code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getDetails() {
        return details;
    }

    public void setDetails(List details) {
        this.details = details;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}

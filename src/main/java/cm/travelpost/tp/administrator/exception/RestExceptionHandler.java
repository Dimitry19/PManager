package cm.travelpost.tp.administrator.exception;


import cm.travelpost.tp.common.exception.*;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cm.travelpost.tp.common.exception.ErrorCode.DEFAULT_ERROR;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handlerAnyException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<Object> handlerValidationException(ValidationException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<Object> handleUserErrorExceptions(UserException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        String[] code = new String[2];
        code[0] = String.valueOf(HttpStatus.NOT_FOUND.value());
        code[1] = "user.unauthorized";

        return new ResponseEntity<>(new ErrorResponse((String) CollectionsUtils.getFirst(details), details, code, DEFAULT_ERROR), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundExceptions(UserNotFoundException ex) {

        return getObjectResponseEntity(ex.getMessage(), "user.notfound");
    }

    @ExceptionHandler({AnnounceException.class})
    public ResponseEntity<Object> handleAnnounceExceptions(AnnounceException ex) {

        return getObjectResponseEntity(ex.getMessage(), "announce.error");
    }

    @ExceptionHandler({PricingException.class, SubscriptionException.class})
    public ResponseEntity<Object> handlePricingExceptions(Exception ex) {

        return getObjectResponseEntity(ex.getMessage(), "pricing.error");
    }

    @ExceptionHandler({DashboardException.class})
    public ResponseEntity<Object> handleDashboardExceptions(DashboardException ex) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        String[] code = new String[1];
        code[0] = ex.getCode();

        HttpStatus status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(new ErrorResponse((String) CollectionsUtils.getFirst(details), details, code, DEFAULT_ERROR), new HttpHeaders(), status);
    }

    @NotNull
    private ResponseEntity<Object> getObjectResponseEntity(String message, String s) {
        List<String> details = new ArrayList<>();
        details.add(message);
        String[] code = new String[2];
        code[0] = String.valueOf(HttpStatus.NOT_FOUND.value());
        code[1] = s;
        return new ResponseEntity<>(new ErrorResponse((String) CollectionsUtils.getFirst(details), details, code, DEFAULT_ERROR), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex) {
        List<ConstraintViolation> details = ex.getConstraintViolations()
                .stream().collect(Collectors.toList());
        String[] code = new String[2];
        code[0] = String.valueOf(HttpStatus.BAD_REQUEST.value());
        code[1] = "bad request";
        return new ResponseEntity<>(new ErrorResponse("Bad Request", details, code, DEFAULT_ERROR), new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();

        CollectionsUtils.makeCopies(ex.getSupportedMethods(),details);

        String[] code = new String[1];
        code[0] = String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value());
        ErrorResponse error = new ErrorResponse("Not supported Methods", details, code, DEFAULT_ERROR);
        return new ResponseEntity(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        StringBuilder messageBuilder= new StringBuilder();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
            messageBuilder.append(error.getDefaultMessage());
            messageBuilder.append("\n");
        }

        String[] code = new String[1];
        code[0] = String.valueOf(HttpStatus.BAD_REQUEST.value());
        ErrorResponse error = new ErrorResponse(messageBuilder.toString(), details, code, DEFAULT_ERROR);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}

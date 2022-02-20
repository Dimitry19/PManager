package cm.packagemanager.pmanager.administrator.exception;


import cm.packagemanager.pmanager.common.exception.ErrorResponse;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
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

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected static final int DEFAULT_ERROR=-1;

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
        String[] code = new String[1];
        code[0] = String.valueOf(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(new ErrorResponse("user.error", details, code, DEFAULT_ERROR), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundExceptions(UserNotFoundException ex) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        String[] code = new String[1];
        code[0] = String.valueOf(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(new ErrorResponse("user.notfound", details, code, DEFAULT_ERROR), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex) {
        List<ConstraintViolation> details = ex.getConstraintViolations()
                .stream().collect(Collectors.toList());
        String[] code = new String[1];
        code[0] = String.valueOf(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(new ErrorResponse("bad request", details, code, DEFAULT_ERROR), new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (String error : ex.getSupportedMethods()) {
            details.add(error);
        }
        String[] code = new String[1];
        code[0] = String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value());
        ErrorResponse error = new ErrorResponse("Not supported Methods", details, code, DEFAULT_ERROR);
        return new ResponseEntity(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        String[] code = new String[1];
        code[0] = String.valueOf(HttpStatus.BAD_REQUEST.value());
        ErrorResponse error = new ErrorResponse("Validation Failed", details, code, DEFAULT_ERROR);
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }
}

package com.wex.purchase.transaction.exception.handler;

import com.wex.purchase.transaction.exception.ResourceBadRequestException;
import com.wex.purchase.transaction.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class AppExceptionHandler {

    public static final String INVALID_REQUEST = "Invalid Request";
    public static final String BAD_REQUEST_EXCEPTION = "Bad request";
    public static final String UNKNOWN_EXCEPTION = "Unknown exception occurred";
    public static final String RESOURCE_NOT_FOUND_EXCEPTION = "Resource not found";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleException(ResourceNotFoundException exception) {
        String exceptionMessage = exception.getMessage() != null ? exception.getMessage() : UNKNOWN_EXCEPTION;
        Map<String, Object> map = ExceptionBuilder.buildError(RESOURCE_NOT_FOUND_EXCEPTION,
                NOT_FOUND, exceptionMessage);
        return new ResponseEntity<>(Collections.singletonList(map), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        Iterator var6 = exception.getBindingResult().getFieldErrors().iterator();

        while (var6.hasNext()) {
            FieldError error = (FieldError) var6.next();
            errors.add(error.getDefaultMessage());
        }

        var6 = exception.getBindingResult().getGlobalErrors().iterator();

        while (var6.hasNext()) {
            ObjectError error = (ObjectError) var6.next();
            errors.add(error.getDefaultMessage());
        }

        Map<String, Object> map = ExceptionBuilder.buildError(INVALID_REQUEST, HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceBadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(ResourceBadRequestException exception) {
        String exceptionMessage = exception.getMessage() != null ? exception.getMessage() : UNKNOWN_EXCEPTION;
        Map<String, Object> map = ExceptionBuilder.buildError(BAD_REQUEST_EXCEPTION,
                BAD_REQUEST, exceptionMessage);
        return new ResponseEntity<>(Collections.singletonList(map), HttpStatus.BAD_REQUEST);
    }
}

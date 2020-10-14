package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public static final String notFound = " not found!";

    @ExceptionHandler({IllegalArgumentException.class, Exception.class})
    protected ResponseEntity handleUnknownException(Exception e, WebRequest request) {
        // Need null because we're overriding handleExceptionInternal
        return handleExceptionInternal(e, null, null, null, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error(requestURI, ex);
        if (ex.getMessage().contains(notFound)) {
            status = HttpStatus.NOT_FOUND;
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}

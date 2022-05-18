package com.bookstore.response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<?> handleInternal(HttpClientErrorException ex) {
        return ResponseObject.build(ex.getStatusCode(), ex.getStatusText(), null);
    }
}

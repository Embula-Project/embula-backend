package com.embula.embula_backend.advisor;

import com.embula.embula_backend.exception.NotFoundException;
import com.embula.embula_backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardResponse> handleNotFoundException(NotFoundException e) {
        ResponseEntity<StandardResponse> responseEntity = new ResponseEntity<>(
                new StandardResponse(404,"Not Found", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
        return responseEntity;
    }
}

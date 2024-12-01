package com.laundrygo.shorturl.exception;

import com.laundrygo.shorturl.exception.url.UrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UrlException.class)
    public ResponseEntity<ErrorResponse> UserException(UrlException e) {
        return new ResponseEntity<>(ErrorResponse.create(e.getCommonErrorCodeType()), e.getCommonErrorCodeType().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validationException(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> "field: " + error.getField() + ", " + "error: " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResponse("REQUEST_VALIDATION_ERROR", "Validation 에러 입니다.", errors), HttpStatus.BAD_REQUEST);
    }
}

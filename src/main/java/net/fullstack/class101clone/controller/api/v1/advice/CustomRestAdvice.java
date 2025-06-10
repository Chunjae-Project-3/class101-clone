package net.fullstack.class101clone.controller.api.v1.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class CustomRestAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(BindException ex) {
        log.error("BindException 발생", ex);

        Map<String, String> body = new HashMap<>();
        BindingResult bindingResult = ex.getBindingResult();

        bindingResult.getFieldErrors().forEach(error ->
                body.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(body);
    }
}

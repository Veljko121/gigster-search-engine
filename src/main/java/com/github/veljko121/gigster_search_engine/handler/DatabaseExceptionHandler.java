package com.github.veljko121.gigster_search_engine.handler;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DatabaseExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handeException(DataAccessException exception) {
        return ResponseEntity.badRequest().body(extractDetail(exception));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> notFound(NoSuchElementException exception) {
        return ResponseEntity.notFound().build();
    }

    private String extractDetail(DataAccessException exception) {
        return extractDetail(exception.getMessage());
    }

    private String extractDetail(String exceptionMessage) {
        String patternString = "Detail: (.*?)]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(exceptionMessage);

        if (matcher.find()) {
            String detailMessage = matcher.group(1);
            return detailMessage;
        } else {
            return "Unknown error.";
        }
    }
    
}

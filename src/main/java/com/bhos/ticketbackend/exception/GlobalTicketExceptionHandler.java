package com.bhos.ticketbackend.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalTicketExceptionHandler extends ResponseEntityExceptionHandler{

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Bookmark Not Found");
        problemDetail.setType(URI.create(request.getContextPath()));
        problemDetail.setProperty("Error message", errors);
        return new ResponseEntity<>(problemDetail, status);


        /*List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);*/
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> conflict(DataIntegrityViolationException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Bookmark Not Found");
        problemDetail.setType(URI.create("ilkin/antaklasu"));
        problemDetail.setProperty("Error message", message);
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);

        /*String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        ApiResponseError responseError = new ApiResponseError(Arrays.asList(message), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);*/
    }






}
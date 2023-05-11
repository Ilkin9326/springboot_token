package com.bhos.ticketbackend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalTicketExceptionHandler extends ResponseEntityExceptionHandler{

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Validation");
        problemDetail.setType(URI.create(request.getContextPath()));
        problemDetail.setProperty("Error message", errors);
        return new ResponseEntity<>(problemDetail, status);

    }


    @ExceptionHandler(UserEmailUniqueException.class)
    public ProblemDetail handleEmailUniqueProblem(UserEmailUniqueException exception, HttpServletRequest httpServletReques){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Could not execute statement");
        problemDetail.setType(URI.create(String.valueOf(httpServletReques.getRequestURL())));

        return  problemDetail;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest httpServletRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("JWT expired ");
        problemDetail.setType(URI.create(String.valueOf(httpServletRequest.getRequestURL())));

        return  problemDetail;
    }

    @ExceptionHandler(NullPointerException.class)
    public ProblemDetail handleNullPointerException(NullPointerException ex, HttpServletRequest httpServletRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Returning Null");
        problemDetail.setType(URI.create(String.valueOf(httpServletRequest.getRequestURL())));

        return  problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex, HttpServletRequest httpServletRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Authentication Exception");
        problemDetail.setType(URI.create(String.valueOf(httpServletRequest.getRequestURL())));

        return  problemDetail;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest httpServletRequest){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Username Not Found Exception");
        problemDetail.setType(URI.create(String.valueOf(httpServletRequest.getRequestURL())));

        return  problemDetail;
    }

}
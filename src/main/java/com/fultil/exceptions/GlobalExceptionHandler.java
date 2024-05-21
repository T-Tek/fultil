package com.fultil.exceptions;


import com.fultil.enums.ResponseCodeAndMessage;
import com.fultil.payload.response.Response;
import com.fultil.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateException.class)
    @ResponseBody
    public Response handleDuplicateEmailException(DuplicateException exception){
        log.error("DuplicateUserException occurred: {}", exception.getMessage());
        return UserUtils.generateResponse(ResponseCodeAndMessage.ALREADY_EXISTS, exception.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public Response handleResourceNotFoundException(ResourceNotFoundException exception){
        log.error("ResourceNotFoundException occurred: {}", exception.getMessage());
        return UserUtils.generateResponse(ResponseCodeAndMessage.RESOURCE_NOT_FOUND, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseBody
    public Response handleIncorrectPasswordException(IncorrectPasswordException exception){
        log.error("ResourceNotFoundException occurred: {}", exception.getMessage());
        return UserUtils.generateResponse(ResponseCodeAndMessage.INVALID_AUTHENTICATION, exception.getMessage());
    }
}


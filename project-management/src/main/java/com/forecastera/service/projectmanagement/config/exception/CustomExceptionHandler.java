package com.forecastera.service.projectmanagement.config.exception;

import com.forecastera.service.projectmanagement.commonResponseUtil.Error;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

/**
 * @Author Uttam Kachhad
 * @Create 22-05-2023
 * @Description
 */
@RestControllerAdvice
@NoArgsConstructor
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Error> handleFieldValidationExceptions(UnauthorizedException ex, HttpServletRequest request, HttpServletResponse response) {

        logger.error("Exception: ", ex);

        Error error = new Error();
        error.setStatus(ex.getStatus());
        error.setMessage(ex.getMessage());
        error.setRequestAt(Objects.isNull(request.getAttribute("startTime")) ? new Date() : new Date((Long) request.getAttribute("startTime")));
        return new ResponseEntity(error, HttpStatus.UNAUTHORIZED);
    }
}

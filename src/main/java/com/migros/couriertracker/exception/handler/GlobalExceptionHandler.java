package com.migros.couriertracker.exception.handler;

import com.migros.couriertracker.exception.CourierIdNotFoundException;
import com.migros.couriertracker.exception.response.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = CourierIdNotFoundException.class)
    protected ResponseEntity<Object> handleCourierIdNotFoundException(CourierIdNotFoundException exception){
        logStackTrace(exception);

        return buildResponseEntity(
                new ApiError(HttpStatus.NOT_FOUND, exception.getMessage(), exception)
        );
    }

    private void logStackTrace(final Throwable throwable) {
        log.error("{} occurred : ", throwable.getClass().getSimpleName());
        final String[] lines = ExceptionUtils.getStackFrames(throwable);
        for (final String line : lines) {
            log.error(line);
        }
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}

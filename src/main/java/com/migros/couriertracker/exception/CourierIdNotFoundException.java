package com.migros.couriertracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CourierIdNotFoundException extends RuntimeException {
    static final long serialVersionUID = -1L;

    public CourierIdNotFoundException(String message) {
        super(message);
    }
}

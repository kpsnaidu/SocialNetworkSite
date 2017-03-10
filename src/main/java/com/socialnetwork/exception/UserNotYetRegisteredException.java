package com.socialnetwork.exception;

import org.springframework.aop.target.ThreadLocalTargetSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotYetRegisteredException extends RuntimeException {

    public UserNotYetRegisteredException(String message) {
        super(message);
    }

}

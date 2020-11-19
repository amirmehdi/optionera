package com.gitlab.amirmehdi.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of the free plan expired user trying to authenticate.
 */
public class UserExpiredPlanException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserExpiredPlanException(String message) {
        super(message);
    }

    public UserExpiredPlanException(String message, Throwable t) {
        super(message, t);
    }
}

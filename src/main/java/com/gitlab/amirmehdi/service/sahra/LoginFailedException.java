package com.gitlab.amirmehdi.service.sahra;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("login failed");
    }
}

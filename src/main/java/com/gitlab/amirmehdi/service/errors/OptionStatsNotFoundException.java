package com.gitlab.amirmehdi.service.errors;

public class OptionStatsNotFoundException extends RuntimeException{

    public OptionStatsNotFoundException() {
        super("option stats not found");
    }
}

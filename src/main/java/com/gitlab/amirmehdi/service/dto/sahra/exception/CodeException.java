package com.gitlab.amirmehdi.service.dto.sahra.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CodeException extends RuntimeException {
    private long code;
    private String desc;

    public CodeException(long code, String desc) {
        this.desc = desc;
        this.code = code;
    }
}

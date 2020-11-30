package com.gitlab.amirmehdi.service.dto.sahra.exception;

import lombok.Getter;

@Getter
public class CodeException extends RuntimeException {
    private long code;
    private String desc;

    public CodeException(long code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    @Override
    public String toString() {
        return "CodeException{" +
            "code=" + code +
            ", desc='" + desc + '\'' +
            '}';
    }
}

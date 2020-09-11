package com.gitlab.amirmehdi.service.dto.core;


import lombok.Setter;

@Setter
public class Board {

    private String code;
    private String name;

    public String getCode() {
        return code.replaceAll(" ", "");
    }

    public String getName() {
        return name.replaceAll(" ", "");
    }
}

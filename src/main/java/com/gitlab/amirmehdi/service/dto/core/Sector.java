package com.gitlab.amirmehdi.service.dto.core;

import lombok.Setter;

@Setter
public class Sector {

    private String code;
    private String name;
    private String superSectorCode;

    public String getCode() {
        return code.replaceAll(" ", "");
    }

    public String getName() {
        return name.replaceAll(" ", "");
    }

    public String getSuperSectorCode() {
        return superSectorCode.replaceAll(" ", "");
    }
}

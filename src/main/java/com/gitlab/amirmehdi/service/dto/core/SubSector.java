package com.gitlab.amirmehdi.service.dto.core;

import lombok.Setter;

@Setter
public class SubSector {

    private String code;
    private String sectorCode;
    private String name;

    public String getCode() {
        return code.replaceAll(" ", "");
    }

    public String getSectorCode() {
        return sectorCode.replaceAll(" ", "");
    }

    public String getName() {
        return name.replaceAll(" ", "");
    }
}

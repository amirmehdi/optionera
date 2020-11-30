package com.gitlab.amirmehdi.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IsinExecuted {
    private String isin;
    private int executed;
}

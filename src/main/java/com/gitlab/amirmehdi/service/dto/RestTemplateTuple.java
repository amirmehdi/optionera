package com.gitlab.amirmehdi.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
public class RestTemplateTuple {
    private RestTemplate restTemplate;
    private int errorCount;

    public RestTemplateTuple(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}

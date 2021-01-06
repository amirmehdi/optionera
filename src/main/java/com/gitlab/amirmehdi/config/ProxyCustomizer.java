package com.gitlab.amirmehdi.config;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

public class ProxyCustomizer implements RestTemplateCustomizer {

    private String hostname;
    private int port;

    public ProxyCustomizer() {
    }

    public ProxyCustomizer(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        HttpHost proxy = new HttpHost(hostname, port);
        HttpClient httpClient = HttpClientBuilder.create()
            .setConnectionTimeToLive(3, TimeUnit.SECONDS)
            .setRoutePlanner(new DefaultProxyRoutePlanner(proxy) {
                @Override
                public HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
                    return super.determineProxy(target, request, context);
                }
            })
            .build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}

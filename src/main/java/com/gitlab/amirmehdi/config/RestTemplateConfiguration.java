package com.gitlab.amirmehdi.config;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Primary
    RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(6000);
        httpRequestFactory.setConnectTimeout(6000);
        httpRequestFactory.setReadTimeout(6000);
        httpRequestFactory.setHttpClient(HttpClientBuilder.create()
            .setMaxConnTotal(500)
            .setMaxConnPerRoute(100)
            .build());
        return new RestTemplate(httpRequestFactory);
    }
    @Bean
    RestTemplate longPollRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(6000);
        httpRequestFactory.setConnectTimeout(6000);
        httpRequestFactory.setReadTimeout(60000);
        return new RestTemplate(httpRequestFactory);
    }

    @Bean
    RestTemplate trustedRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(csf)
            .setMaxConnTotal(300)
            .setMaxConnPerRoute(100)
            .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
            new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(6000);
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }
}

package com.gitlab.amirmehdi.service;

import com.gitlab.amirmehdi.service.dto.RestTemplateTuple;
import com.gitlab.amirmehdi.service.dto.TelegramMessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@Service
@Log4j2
public class TelegramMessageSender {
    private final List<RestTemplateTuple> restTemplates = new ArrayList<>();
    @Value("${application.telegram.token}")
    private String apiToken;

    @PostConstruct
    public void fillRestTemplates() {
        try (Stream<String> lines = Files.lines(Paths.get("valid_proxies.txt"))) {
            lines.forEach(s -> {
                RestTemplate restTemplate = getRestTemplate(s);
                restTemplates.add(new RestTemplateTuple(restTemplate));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(TelegramMessageDto dto) {
        if (restTemplates.isEmpty()) {
            log.error("have no proxy");
            return;
        }
        sendMessage(dto, 3);
    }

    private void sendMessage(TelegramMessageDto dto, int attemptNumber) {
        if (attemptNumber <= 0) {
            log.error("attempt number reached, sending to telegram has a problem");
            return;
        }
        RestTemplateTuple tuple = restTemplates.get(ThreadLocalRandom.current().nextInt(restTemplates.size()));
        try {
            requestToTelegram(tuple.getRestTemplate(), dto.getChatId(), dto.getText());
            tuple.setErrorCount(0);
        } catch (Exception e) {
            tuple.setErrorCount(tuple.getErrorCount() + 1);
            if (tuple.getErrorCount() > 5) {
                restTemplates.remove(tuple);
            }
            sendMessage(dto, attemptNumber - 1);
        }
    }

    public void refreshValidProxies() {
        try (Stream<String> lines = Files.lines(Paths.get("http_proxies.txt"))) {
            List<String> validProxies = new ArrayList<>();
            lines.forEach(s -> {
                try {
                    RestTemplate restTemplate = getRestTemplate(s);
                    //RestTemplate restTemplate = new RestTemplateBuilder(new ProxyCustomizer(hostName, port)).build();
                    requestToTelegram(restTemplate, "-1001468700748", String.format("%s is valid", s));
                    log.info("yes boy! valid proxy {}", s);
                    validProxies.add(s);
                } catch (Exception e) {
                    log.error("invalid proxy {}", s);
                }
            });
            Files.write(Paths.get("valid_proxies.txt"), validProxies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RestTemplate getRestTemplate(String s) {
        String hostName = s.split(":")[0];
        int port = Integer.parseInt(s.split(":")[1]);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(6000);
        requestFactory.setReadTimeout(6000);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }

    private ResponseEntity<String> requestToTelegram(RestTemplate restTemplate, String chatId, String text) {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id={chatId}&text={text}&parse_mode=HTML";
        String url = String.format(urlString, apiToken);
        ResponseEntity<String> res = restTemplate.getForEntity(url, String.class, chatId, text);
        log.debug("url : {} , res : {} ", url, res.toString());
        return res;
    }


}

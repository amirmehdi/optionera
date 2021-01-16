package com.gitlab.amirmehdi.service.sahra;

import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.service.dto.sahra.NegotiateResponse;
import com.gitlab.amirmehdi.service.dto.sahra.SecurityFields;
import com.gitlab.amirmehdi.service.dto.sahra.StartSocketResponse;
import com.gitlab.amirmehdi.util.CaptchaDecoder;
import com.gitlab.amirmehdi.util.SeleniumDriver;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.stream.Collectors;

import static com.gitlab.amirmehdi.util.UrlEncodingUtil.getEncode;

@Service
@Log4j2
public class NegotiateManager {
    private final RestTemplate restTemplate;
    private final String negotiateUrl = "%s/realtime/negotiate?clientProtocol=1.5&token=&connectionData={connectionData}&_={nano}";
    private final String startUrl = "%s/realtime/start?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s&_=%s";
    private final String connectionData = "[{\"name\":\"omsclienthub\"}]";
    private final SeleniumDriver seleniumDriver;

    public NegotiateManager(RestTemplate restTemplate, SeleniumDriver seleniumDriver) {
        this.restTemplate = restTemplate;
        this.seleniumDriver = seleniumDriver;
    }

    public NegotiateResponse negotiate(Token token) {
        ResponseEntity<NegotiateResponse> negotiateResponse =
            restTemplate.exchange(String.format(negotiateUrl, token.getBroker().url)
                , HttpMethod.GET
                , new HttpEntity<>(getNegotiateHeaders(token))
                , NegotiateResponse.class
                , connectionData, System.currentTimeMillis());
        log.info("negotiate, response:{}", negotiateResponse);
        return negotiateResponse.getBody();
    }

    public void start(SecurityFields securityFields) {
        ResponseEntity<StartSocketResponse> startResponse =
            restTemplate.exchange(
                URI.create(String.format(startUrl, securityFields.getToken().getBroker().url, getEncode(securityFields.getConnectionToken()), getEncode(connectionData), System.currentTimeMillis()))
                , HttpMethod.GET
                , new HttpEntity<>(getStartHeaders(securityFields.getToken().getToken()))
                , StartSocketResponse.class);
        log.info("start, response:{}", startResponse);
        if (!startResponse.getBody().getResponse().equals("started")) {
            throw new RuntimeException("not start");
        }
    }

    private LinkedMultiValueMap<String, String> getNegotiateHeaders(Token token) {
        String[] s = token.getToken().split("__");
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Connection", "keep-alive");
        headers.add("Accept", "text/plain, */*; q=0.01");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Sec-Fetch-Site", "same-origin");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Referer", "https://firouzex.ephoenix.ir/");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
        headers.add("User-Agent", s[0]);
        headers.add("Cookie", s[1]);
        return headers;
    }


    private LinkedMultiValueMap<String, String> getStartHeaders(String token) {
        String[] s = token.split("__");
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Connection", "keep-alive");
        headers.add("Accept", "text/plain, */*; q=0.01");
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Sec-Fetch-Site", "same-origin");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Referer", "https://firouzex.ephoenix.ir/");
        headers.add("Accept-Language", "en-US,en;q=0.9,fa-IR;q=0.8,fa;q=0.7");
        headers.add("User-Agent", s[0]);
        headers.add("Cookie", s[1]);
        return headers;
    }

    public String login(BourseCode bourseCode, long attemptNumber) {
        WebDriver driver = null;
        try {
            driver = seleniumDriver.getWebDriver();
            driver.get(bourseCode.getBroker().url);
            String captchaNum;
            captchaNum = getCaptcha(driver);
            WebElement username = driver.findElement(By.id("keyboard-user"));
            WebElement password = driver.findElement(By.id("keyboard-pass"));
            WebElement captcha = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[4]/input"));
            WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[6]/button"));
            username.sendKeys(bourseCode.getUsername());
            password.sendKeys(bourseCode.getPassword());
            captcha.sendKeys(captchaNum);
            loginButton.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e);
            }
            if (driver.manage().getCookies().size() < 2) {
                if (attemptNumber > 0) {
                    attemptNumber--;
                    return login(bourseCode, attemptNumber);
                }
                throw new LoginFailedException();
            }
            String cookies = driver.manage().getCookies().stream().map(cookie -> cookie.getName() + "=" + cookie.getValue()).collect(Collectors.joining(";"));
            String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
            log.info("login succeed for bourse Code:{}, username: {} token saved", bourseCode.getId(), bourseCode.getName());
            return userAgent + "__" + cookies;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new LoginFailedException();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private String getCaptcha(WebDriver driver) {
        String captchaNum;
        while (true) {
            try {
                WebElement logo = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[4]/img"));
                CaptchaDecoder.getInstance().saveCaptcha(logo, "sahra", "png");
                captchaNum = CaptchaDecoder.getInstance().mode2Captcha("sahra", ".png");
                if (captchaNum.length() == 6) {
                    break;
                } else {
                    logo.click();
                    Thread.sleep(1000);
                    //retry
                }
            } catch (IOException | InterruptedException e) {
                log.error("get captcha gor error", e);
                throw new RuntimeException(e);
            }
        }
        return captchaNum;
    }
}

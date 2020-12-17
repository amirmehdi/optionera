package com.gitlab.amirmehdi.service.sahra;

import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.repository.TokenRepository;
import com.gitlab.amirmehdi.service.dto.sahra.NegotiateResponse;
import com.gitlab.amirmehdi.service.dto.sahra.StartSocketResponse;
import com.gitlab.amirmehdi.util.CaptchaDecoder;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.gitlab.amirmehdi.util.UrlEncodingUtil.getEncode;

@Service
@Log4j2
public class NegotiateManager {
    private final RestTemplate restTemplate;
    private final TokenRepository tokenRepository;
    private final String negotiateUrl = "https://firouzex.ephoenix.ir/realtime/negotiate?clientProtocol=1.5&token=&connectionData={connectionData}&_={nano}";
    private final String startUrl = "https://firouzex.ephoenix.ir/realtime/start?transport=longPolling&clientProtocol=1.5&token=&connectionToken=%s&connectionData=%s&_=%s";
    private final String connectionData = "[{\"name\":\"omsclienthub\"}]";

    public NegotiateManager(RestTemplate restTemplate, TokenRepository tokenRepository) {
        this.restTemplate = restTemplate;
        this.tokenRepository = tokenRepository;
    }

    public NegotiateResponse negotiate(Token token) {
        ResponseEntity<NegotiateResponse> negotiateResponse =
            restTemplate.exchange(negotiateUrl
                , HttpMethod.GET
                , new HttpEntity<>(getNegotiateHeaders(token))
                , NegotiateResponse.class
                , connectionData, System.currentTimeMillis());
        log.info("negotiate, response:{}", negotiateResponse);
        return negotiateResponse.getBody();
    }

    public void start(String token, String connectionToken) {
        ResponseEntity<StartSocketResponse> startResponse =
            restTemplate.exchange(
                URI.create(String.format(startUrl, getEncode(connectionToken), getEncode(connectionData), System.currentTimeMillis()))
                , HttpMethod.GET
                , new HttpEntity<>(getStartHeaders(token))
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

    public Token login(long attemptNumber) {
        WebDriver driver = null;
        WebDriverManager.chromedriver().browserVersion("87.0.4280.88").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://firouzex.ephoenix.ir/");
        String captchaNum;
        captchaNum = getCaptcha(driver);
        WebElement username = driver.findElement(By.id("keyboard-user"));
        WebElement password = driver.findElement(By.id("keyboard-pass"));
        WebElement captcha = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[4]/input"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[6]/button"));
        username.sendKeys("fsdro17580");
        password.sendKeys("ciBpyg-bodmax-4socmo");
        captcha.sendKeys(captchaNum);
        loginButton.click();
        new WebDriverWait(driver, 20).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e);
        }
        if (driver.manage().getCookies().size() < 2) {
            if (attemptNumber > 0) {
                attemptNumber--;
                return login(attemptNumber);
            }
            throw new LoginFailedException();
        }
        String cookies = driver.manage().getCookies().stream().map(cookie -> cookie.getName() + "=" + cookie.getValue()).collect(Collectors.joining(";"));
        String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
        driver.quit();
        log.info("login succeed, token saved");
        Token token = tokenRepository.findTopByBrokerOrderByIdDesc(Broker.FIROOZE_ASIA).get();
        token.setToken(userAgent + "__" + cookies);
        return tokenRepository.save(token);
    }

    private String getCaptcha(WebDriver driver) {
        String captchaNum;
        while (true) {
            try {
                WebElement logo = driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[4]/img"));
                WrapsDriver wrapsDriver = (WrapsDriver) logo;
                File screenshot = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
                BufferedImage bufferedImage = ImageIO.read(screenshot);
                java.awt.Rectangle rectangle = new java.awt.Rectangle(logo.getSize().width, logo.getSize().height, logo.getSize().width, logo.getSize().height);
                Point location = logo.getLocation();
                BufferedImage destImage = bufferedImage.getSubimage(location.x, location.y, rectangle.width, rectangle.height);
                ImageIO.write(destImage, "png", screenshot);
                File file = new File("sahra.png");
                FileUtils.copyFile(screenshot, file);
                captchaNum = CaptchaDecoder.getInstance().mode2Captcha("sahra", ".png");
                if (captchaNum.length() == 6) {
                    System.out.print("Success!");
                    System.out.println(captchaNum);
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

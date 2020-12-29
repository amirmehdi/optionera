package com.gitlab.amirmehdi.service.asa;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.repository.BourseCodeRepository;
import com.gitlab.amirmehdi.repository.TokenRepository;
import com.gitlab.amirmehdi.service.sahra.LoginFailedException;
import com.gitlab.amirmehdi.util.CaptchaDecoder;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.gitlab.amirmehdi.util.SeleniumDriver.getWebDriver;

@Service
@Log4j2
public class ConnectionManager implements CommandLineRunner {
    private final BourseCodeRepository bourseCodeRepository;
    private final ApplicationProperties properties;
    private final TokenRepository tokenRepository;

    public ConnectionManager(BourseCodeRepository bourseCodeRepository, ApplicationProperties properties, TokenRepository tokenRepository) {
        this.bourseCodeRepository = bourseCodeRepository;
        this.properties = properties;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<BourseCode> bourseCodes = bourseCodeRepository.findAllByBroker(Broker.AGAH);
        for (BourseCode bourseCode : bourseCodes) {
            if (bourseCode.getToken() != null && ChronoUnit.HOURS.between(bourseCode.getToken().getCreatedAt().toInstant(), new Date().toInstant()) < 6) {
                continue;
            }
            refreshToken(bourseCode);
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void refreshAsaToken() {
        bourseCodeRepository.findAllByBroker(Broker.AGAH).forEach(this::refreshToken);
    }

    private void refreshToken(BourseCode bourseCode) {
        String tokenString = login(bourseCode, 3);
        if (bourseCode.getToken() == null) {
            Token token = new Token()
                .bourseCode(bourseCode)
                .broker(bourseCode.getBroker())
                .token(tokenString);
            tokenRepository.save(token);
            bourseCode.setToken(token);
            bourseCodeRepository.save(bourseCode);
        } else {
            bourseCode.getToken().setToken(tokenString);
            tokenRepository.save(bourseCode.getToken());
        }
    }

    private String login(BourseCode bourseCode, int attemptNumber) {
        WebDriver driver = null;
        try {
            driver = getWebDriver(properties.getSeleniumHubGrid());
            driver.get(bourseCode.getBroker().url);
            String captchaNum;
            captchaNum = getCaptcha(driver);
            WebElement username = driver.findElement(By.id("username"));
            WebElement password = driver.findElement(By.id("password"));
            WebElement captcha = driver.findElement(By.id("captcha"));
            WebElement loginButton = driver.findElement(By.id("submit-btn"));
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
            log.info("login succeed, token saved");
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
                WebElement logo = driver.findElement(By.xpath("//*[@id=\"imgcpatcha\"]"));
                CaptchaDecoder.getInstance().saveCaptcha(logo, "agah", "png");
                captchaNum = CaptchaDecoder.getInstance().mode3Captcha("agah", "png");
                if (captchaNum.length() == 6) {
                    System.out.print("Success!");
                    System.out.println(captchaNum);
                    break;
                } else {
                    WebElement refreshCaptcha = driver.findElement(By.xpath("//*[@id=\"refreshCaptcha\"]"));
                    refreshCaptcha.click();
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

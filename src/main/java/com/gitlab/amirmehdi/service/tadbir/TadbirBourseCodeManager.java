package com.gitlab.amirmehdi.service.tadbir;

import com.gitlab.amirmehdi.config.ApplicationProperties;
import com.gitlab.amirmehdi.domain.BourseCode;
import com.gitlab.amirmehdi.domain.Portfolio;
import com.gitlab.amirmehdi.domain.Token;
import com.gitlab.amirmehdi.domain.enumeration.Broker;
import com.gitlab.amirmehdi.domain.enumeration.OMS;
import com.gitlab.amirmehdi.repository.BourseCodeRepository;
import com.gitlab.amirmehdi.repository.PortfolioRepository;
import com.gitlab.amirmehdi.repository.TokenRepository;
import com.gitlab.amirmehdi.service.dto.tadbir.DailyPortfolioResponse;
import com.gitlab.amirmehdi.service.dto.tadbir.RemainResponse;
import com.gitlab.amirmehdi.service.sahra.LoginFailedException;
import com.gitlab.amirmehdi.util.CaptchaDecoder;
import com.gitlab.amirmehdi.util.SeleniumDriver;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TadbirBourseCodeManager implements CommandLineRunner {
    private final BourseCodeRepository bourseCodeRepository;
    private final TokenRepository tokenRepository;
    private final SeleniumDriver seleniumDriver;
    private final ApplicationProperties applicationProperties;
    private final TadbirService tadbirService;
    private final PortfolioRepository portfolioRepository;

    public TadbirBourseCodeManager(BourseCodeRepository BourseCodeRepository, TokenRepository tokenRepository, SeleniumDriver seleniumDriver, ApplicationProperties applicationProperties, TadbirService tadbirService, PortfolioRepository portfolioRepository) {
        this.bourseCodeRepository = BourseCodeRepository;
        this.tokenRepository = tokenRepository;
        this.seleniumDriver = seleniumDriver;
        this.applicationProperties = applicationProperties;
        this.tadbirService = tadbirService;
        this.portfolioRepository = portfolioRepository;
    }

    @Scheduled(cron = "0 28 8 * * *")
    public void updateTokens() {
        if (!applicationProperties.getBrokers().isTadbirEnable()) {
            return;
        }
        List<BourseCode> bourseCodes = bourseCodeRepository.findAllByBrokerIn(Broker.byOms(OMS.TADBIR));
        for (BourseCode bourseCode : bourseCodes) {
            try {
                updateToken(bourseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("finish initial tadbir tokens");
    }

    public void updateToken(BourseCode bourseCode) {
        String tokenString = login(bourseCode, 3);
        if (bourseCode.getToken() == null) {
            Token token = new Token()
                .bourseCode(bourseCode)
                .broker(bourseCode.getBroker())
                .token(tokenString);
            tokenRepository.save(token);
            bourseCode.setToken(token);
        } else {
            bourseCode.getToken().setToken(tokenString);
            tokenRepository.save(bourseCode.getToken());
        }
        bourseCode = bourseCodeRepository.save(bourseCode);
        updateRemain(bourseCode);
        updatePortfolio(bourseCode);
    }

    private void updatePortfolio(BourseCode bourseCode) {
        DailyPortfolioResponse response = tadbirService.getDailyPortfolio(bourseCode);
        List<Portfolio> portfolios = response.getData().stream()
            .map(datum -> Portfolio.builder()
                .date(LocalDate.now())
                .isin(datum.getISIN())
                .quantity((int) datum.getCSDPortfolioCount())
                .avgPrice((int) datum.getBuyPriceAvg())
                .userId(bourseCode.getId())
                .build())
            .collect(Collectors.toList());
        portfolioRepository.saveAll(portfolios);
    }

    private void updateRemain(BourseCode bourseCode) {
        RemainResponse remainResponse = tadbirService.getRemain(bourseCode);
        bourseCode.setBlocked(remainResponse.getData().getBlockedBalance());
        bourseCode.setBuyingPower(remainResponse.getData().getAccountBalance());
        bourseCode.setRemain(remainResponse.getData().getRealBalance());
        bourseCode.setCredit(remainResponse.getData().getCredit());
        bourseCodeRepository.save(bourseCode);
    }

    private String login(BourseCode bourseCode, long attemptNumber) {
        WebDriver driver = null;
        try {
            driver = seleniumDriver.getWebDriver();
            driver.get(bourseCode.getBroker().url);
            String captchaNum;
            WebElement username = driver.findElement(By.id("txtusername"));
            WebElement password = driver.findElement(By.id("txtpassword"));
            WebElement captcha, loginButton, logo;
            if (bourseCode.getBroker().mode == 0) {
                //refah
                logo = driver.findElement(By.id("captcha-img-plus"));
                captcha = driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[1]/div/div[1]/multi-login/div/div[2]/div[1]/form/div[2]/div[1]/div/div/input"));
                loginButton = driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[1]/div/div[1]/multi-login/div/div[2]/div[1]/form/div[4]/div[1]/button"));
                captchaNum = getCaptcha(logo);
            } else {
                logo = driver.findElement(By.id("captcha-img-plus"));
                captcha = driver.findElement(By.name("capcha"));
                loginButton = driver.findElement(By.xpath("/html/body/div/div[1]/div[1]/multi-login/div/div[2]/div[1]/form/div[4]/div[1]/button"));
                captchaNum = getCaptcha(logo);
            }
            username.sendKeys(bourseCode.getUsername());
            password.sendKeys(bourseCode.getPassword());
            captcha.sendKeys(captchaNum);
            loginButton.click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e);
            }
            String token = seleniumDriver.getLocalStorage(driver, "api-token");
            if (driver.manage().getCookies().size() < 4 || token == null) {
                if (attemptNumber > 0) {
                    attemptNumber--;
                    return login(bourseCode, attemptNumber);
                }
                throw new LoginFailedException();
            }
            String cookies = driver.manage().getCookies().stream()
                .map(cookie -> cookie.getName() + "=" + cookie.getValue()).collect(Collectors.joining(";"));
            String userAgent = seleniumDriver.getUserAgent(driver);
            log.info("login succeed for bourse Code:{}, username: {} token saved", bourseCode.getId(), bourseCode.getName());
            return token + "__" + userAgent + "__" + cookies;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new LoginFailedException();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private String getCaptcha(WebElement logo) {
        String captchaNum;
        while (true) {
            try {
                CaptchaDecoder.getInstance().saveCaptcha(logo, "tadbir", "png");
                captchaNum = CaptchaDecoder.getInstance().mode1Captcha("tadbir", ".png");
                if (captchaNum.length() == 4) {
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

    @Override
    public void run(String... args) {
        updateTokens();
    }
}

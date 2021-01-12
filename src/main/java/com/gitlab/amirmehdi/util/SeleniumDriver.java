package com.gitlab.amirmehdi.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class SeleniumDriver {

    @Value("${application.remoteDriverEnable}")
    protected boolean remoteDriverEnable;

    @Value("${application.seleniumHubGrid}")
    protected String seleniumHubGrid;

    public WebDriver getWebDriver() throws MalformedURLException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");

        WebDriver driver;
        if (remoteDriverEnable) {
            URL browserAddress = new URL(seleniumHubGrid + "/wd/hub");
            driver = new RemoteWebDriver(browserAddress, options);
        } else {
            driver = new ChromeDriver(options);
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        new WebDriverWait(driver, 20).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        return driver;
    }
}

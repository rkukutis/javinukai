package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class BaseTest {
    protected static WebDriver driver;

    LandingPage landingPage;

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
//        test
        options.addArguments("--headless");
        options.addArguments("disable-infobars");
        options.addArguments("--start-fullscreen");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
        driver.get("https://javinukai.rhoopoe.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(3, ChronoUnit.SECONDS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/header/div/div[2]/a/button")));


        landingPage = new LandingPage(driver);
        landingPage.setEnglishLanguage();
    }

//    @AfterEach
//    void closePage() {
//        homePage = new HomePage(driver);
//        driver.quit();
//    }

}

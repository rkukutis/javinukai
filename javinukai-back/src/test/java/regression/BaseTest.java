package regression;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.LandingPage;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class BaseTest {
    protected static WebDriver driver;

    LandingPage landingPage;

    @BeforeEach
    void setup() {
        // Choose the browser you want to use (Chrome, Firefox, Edge)
        String browser = "firefox"; // Change this to "firefox" or "edge" as needed

        switch (browser.toLowerCase()) {
            case "chrome":
                setupChromeDriver();
                break;
            case "firefox":
                setupFirefoxDriver();
                break;
            case "edge":
                setupEdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.get("https://javinukai.rhoopoe.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(3, ChronoUnit.SECONDS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/header/div/div[2]/a/button")));

        landingPage = new LandingPage(driver);
        landingPage.setEnglishLanguage();
    }

    private void setupChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
    }

    private void setupFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
//        options.addArguments("--headless");
        driver = new FirefoxDriver(options);
    }

    private void setupEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--headless");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        driver = new EdgeDriver(options);
    }

    @AfterEach
    void closePage() {
        if (driver != null) {
            driver.quit();
        }
    }
}
package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseTest {
        protected static WebDriver driver;

        @BeforeEach
        void setup () {
            driver = new ChromeDriver();
            driver.get("https://javinukai.rhoopoe.com/");
        }

    @AfterEach
    void closePage() {
       driver.quit();
    }

}

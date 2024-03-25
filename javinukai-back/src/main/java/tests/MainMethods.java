package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class MainMethods extends BasePage{

    @FindBy(xpath = "//div[@role='status']")
    private WebElement incorrectCredentialsPop;
    public MainMethods(WebDriver driver) {
        super(driver);

    }

    public String waitForPopUp(String waitForPopUpText){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@role='status']"),  waitForPopUpText));
        return incorrectCredentialsPop.getText();
    }
}

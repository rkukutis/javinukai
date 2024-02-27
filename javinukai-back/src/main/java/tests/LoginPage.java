package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


public class LoginPage extends BasePage{

    @FindBy (xpath = "//*[@id=\"login-form\"]/section[2]/div/a[2]")
    private WebElement registerButton;
    @FindBy (xpath = "//*[@id=\"login-form\"]/section[1]/input")
    private WebElement validLoginUsername;
    @FindBy (xpath = "//*[@id=\"login-form\"]/section[2]/input")
    private WebElement validLoginPassword;
    @FindBy (xpath = "//*[@id=\"login-submit\"]")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }
    public void clickOnRegister(){registerButton.click();}
    public void setValidLoginUsername(String username){validLoginUsername.sendKeys(username);}
    public void setValidLoginPassword(String password){validLoginPassword.sendKeys(password);}
    public void setLoginButton(){loginButton.click();}


}

package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LandingPage extends BasePage{



    @FindBy (xpath = "/html/body/div/div[2]/header/div/div[2]/a/button")
    private WebElement loginButton;
    @FindBy(xpath ="//*[@id=\"root\"]/div[2]/header/div/div[2]/div[1]/button[1]/img")
    private WebElement englishLanguage;
    public LandingPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnLogin(){loginButton.click();}
    public void setEnglishLanguage(){englishLanguage.click();}
}

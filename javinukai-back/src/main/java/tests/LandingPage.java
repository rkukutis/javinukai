package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LandingPage extends BasePage{



    @FindBy (xpath = "/html/body/div/div[2]/header/div/div[2]/a/button")
    private WebElement loginButton;
    @FindBy(xpath ="(//img)[2]")
    private WebElement englishLanguage;

    @FindBy(xpath = "(//img)[3]")
    private WebElement lithuanianLanguage;

    @FindBy(xpath = "//a[@href='/contests']")
    private WebElement homePage;

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    public void clickLithuanianLanguage(){lithuanianLanguage.click();}
    public void clickEnglishLanguage(){englishLanguage.click();}

    public void clickHomePage(){homePage.click();}

    public void clickOnLogin(){loginButton.click();}
    public void setEnglishLanguage(){englishLanguage.click();}
}

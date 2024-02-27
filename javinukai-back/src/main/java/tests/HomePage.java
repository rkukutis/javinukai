package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    @FindBy(xpath = "//*[@id=\"root\"]/div[2]/header/nav/div/div/div[2]/div[2]/div/a[2]")
    private WebElement imagesButton;

    @FindBy(xpath = "//*[@id=\"headlessui-menu-button-:r0:\"]")
    private WebElement burgerMenu;
    @FindBy(xpath = "//button[contains(@role, 'menuitem')  and text()='Log out']")
    private WebElement logout;


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void setBurgerMenu(){burgerMenu.click();}
    public void setLogout(){logout.click();}

}


//*[contains(@label,"text you want to find")]
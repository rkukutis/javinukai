package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    @FindBy(xpath = "(//img[@class='h-16 hidden md:block pr-4'])[1]")
    private WebElement imagesButton;

    @FindBy(xpath = "//*[@id=\"headlessui-menu-button-:r0:\"]")
    private WebElement burgerMenu;

    @FindBy(xpath = "//a[normalize-space()='Personal information']")
    private WebElement menuItemPersonalInformation;

    @FindBy(xpath = "//a[normalize-space()='Manage users']")
    private WebElement menuItemManageUsers;

    @FindBy(xpath = "//a[normalize-space()='Create contest']")
    private WebElement menuItemCreateContest;

    @FindBy(xpath = "//a[normalize-space()='Approve requests']")
    private WebElement menuItemApproveRequests;
    @FindBy(xpath = "//button[contains(@role, 'menuitem')  and text()='Log out']")
    private WebElement menuItemLogout;

    @FindBy(xpath = "(//button[contains(@type,'button')][normalize-space()='Details'])[1]")
    private WebElement firstContest;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickFirstContest(){firstContest.click();}

    public void setBurgerMenu(){burgerMenu.click();}
    public void setLogout(){
        menuItemLogout.click();}

}


//*[contains(@label,"text you want to find")]
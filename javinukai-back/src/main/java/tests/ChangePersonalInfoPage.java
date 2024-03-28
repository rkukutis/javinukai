package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Random;

public class ChangePersonalInfoPage extends BasePage{
    @FindBy (xpath = "//button[normalize-space()='Edit personal information']")
    private WebElement editPersonalInformation;
    @FindBy (xpath = "//button[normalize-space()='Change password']")
    private WebElement clickChangePassword;
    @FindBy (xpath = "//button[normalize-space()='Back']")
    private WebElement clickBack;
    @FindBy (xpath = "//input[@name='name']")
    private WebElement inputName;
    @FindBy (xpath = "//input[@name='surname']")
    private WebElement inputSurname;
    @FindBy (xpath = "//input[@name='birthYear']")
    private WebElement inputBirthYear;
    @FindBy (xpath = "//input[@name='phoneNumber']")
    private WebElement inputPhoneNumber;
    @FindBy (xpath = "//input[@name='email']")
    private WebElement inputEmail;
    @FindBy (xpath = "//select[@class='text-lg text-wrap text-teal-600 border-teal-600 form-field__input']")
    private WebElement changeFreelanceFromYesToNo;
    @FindBy (xpath = "//input[contains(@name,'institution')]")
    private WebElement inputInstitution;
    @FindBy (xpath = "//button[normalize-space()='Save changes']")
    private WebElement clickSaveChanges;
    @FindBy (xpath = "//button[normalize-space()='Cancel changes']")
    private WebElement clickCancelChanges;
    @FindBy (xpath = "//div[contains(@role,'status')]")
    private WebElement personalInfoSaved;
    public ChangePersonalInfoPage(WebDriver driver) {
        super(driver);
    }

    public void clickEditPersonalInfo() {
        editPersonalInformation.click();
    }
    public void editBirthYear() {
        inputBirthYear.clear();
    }
    public void setBirthYear(){
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(111) + 1900;
        inputBirthYear.sendKeys(String.valueOf(randomInt));
    }
    public void saveChanges() {
        clickSaveChanges.click();
    }
}

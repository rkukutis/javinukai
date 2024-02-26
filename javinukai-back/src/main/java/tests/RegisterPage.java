package tests;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Random;

public class RegisterPage extends BasePage{


    @FindBy (xpath = "//*[@id=\"name\"]")
    private WebElement inputName;
    @FindBy (xpath = "//*[@id=\"surname\"]")
    private WebElement inputSurname;
    @FindBy (xpath = "//*[@id=\"birth-year\"]")
    private WebElement inputBirthYear;
    @FindBy (xpath = "//*[@id=\"phone-number\"]")
    private WebElement inputPhoneNumber;
    @FindBy (xpath = "//*[@id=\"login-form\"]/section[5]/input")
    private WebElement inputEmail;
    @FindBy (xpath = "//*[@id=\"password\"]")
    private WebElement inputPassword;
    @FindBy (xpath = "//*[@id=\"password-confirm\"]")
    private WebElement inputConfirmPassword;
    @FindBy (xpath = "//*[@id=\"registration-submit\"]")
    private WebElement clickToRegisterUser;
    @FindBy (xpath = "/div/div")
    private WebElement userSuccessfullyRegistered;

    public RegisterPage(WebDriver driver) {super(driver);}


    public void setInputName(String name){inputName.sendKeys(name);}
    public void setInputSurname(String surname){inputSurname.sendKeys(surname);}
    public void setInputBirthYear(String birthYear){inputBirthYear.sendKeys(birthYear);}
    public void setInputPhoneNumber(String phoneNumber){inputPhoneNumber.sendKeys(phoneNumber);}
    public void setInputEmail(){Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(10000);
        inputEmail.sendKeys("username"+ randomInt + "@gmail.com");}
    public void setInputPassword(String password){inputPassword.sendKeys(password);}
    public void setInputConfirmPassword(String confirmPassword){inputConfirmPassword.sendKeys(confirmPassword);}
    public void setClickToRegisterUser(){clickToRegisterUser.click();}



}

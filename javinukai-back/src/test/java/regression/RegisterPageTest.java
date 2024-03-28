package regression;

import org.assertj.core.api.AssertionsForClassTypes;

import org.junit.jupiter.api.Test;
import tests.LandingPage;
import tests.LoginPage;
import tests.MainMethods;
import tests.RegisterPage;


public class RegisterPageTest extends BaseTest{
    LandingPage landingPage;
    RegisterPage registerPage;
    LoginPage loginPage;
    MainMethods mainMethods;

    @Test
    void userCanRegister(){
        landingPage = new LandingPage(driver);
        registerPage = new RegisterPage(driver);
        loginPage = new LoginPage(driver);
        mainMethods = new MainMethods(driver);

        landingPage.clickOnLogin();
        loginPage.clickOnRegister();
        registerPage.setInputName("John");
        registerPage.setInputSurname("Smith");
        registerPage.setInputBirthYear("1999");
        registerPage.setInputPhoneNumber("860000000");
        registerPage.setInputEmail();
        registerPage.setInputPassword("Password123@");
        registerPage.setInputConfirmPassword("Password123@");

        registerPage.setClickToRegisterUser();

        mainMethods.waitForPopUp("Registered successfully. Please confirm your email");
        String expectedLogIn = "Registered successfully. Please confirm your email";
        String actualLogIn = mainMethods.waitForPopUp("Registered successfully. Please confirm your email");
        AssertionsForClassTypes.assertThat(actualLogIn).isEqualTo(expectedLogIn);

    }
}

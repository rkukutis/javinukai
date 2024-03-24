package regression;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.HomePage;
import tests.LandingPage;
import tests.LoginPage;
import tests.MainMethods;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginPageTest extends BaseTest {
    LandingPage landingPage;
    LoginPage loginPage;
    HomePage homePage;
    MainMethods mainMethods;


    @Test
    void userCanLogin() {
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();

        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        homePage.setBurgerMenu();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"headlessui-menu-item-:r6:\"]")));
        homePage.setLogout();

        mainMethods.waitForPopUp("Logged out successfully!");
        String expectedLogOut = "Logged out successfully!";
        String actualLogOut = mainMethods.waitForPopUp("Logged out successfully!");
        assertThat(actualLogOut).isEqualTo(expectedLogOut);
    }

    @Test
    void userCanNotLogin() {
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password1");
        loginPage.setLoginButton();

        mainMethods.waitForPopUp("Incorrect credentials");
        String expectedLogIn = "Incorrect credentials";
        String actualLogIn = mainMethods.waitForPopUp("Incorrect credentials");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);


    }
}



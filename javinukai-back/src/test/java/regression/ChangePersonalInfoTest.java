package regression;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.*;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ChangePersonalInfoTest extends BaseTest{

    LandingPage landingPage;
    LoginPage loginPage;
    HomePage homePage;
    MainMethods mainMethods;
    ChangePersonalInfoPage changePersonalInfoPage;

    @Test
    void userCanChangePersonalInfo() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);
        changePersonalInfoPage = new ChangePersonalInfoPage(driver);

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("mbaris@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();

        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);

        homePage.setBurgerMenu();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='headlessui-menu-item-:r2:']")));
        homePage.clickPersonalInformation();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Edit personal information']")));
        changePersonalInfoPage.clickEditPersonalInfo();

        changePersonalInfoPage.editBirthYear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@name,'birthYear')]")));

        changePersonalInfoPage.setBirthYear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='birthYear']")));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Save changes']")));
        changePersonalInfoPage.saveChanges();

        String expectedToSave = "Personal information changed successfully";
        String actualSaved = mainMethods.waitForPopUp("Personal information changed successfully");
        AssertionsForClassTypes.assertThat(actualSaved).isEqualTo(expectedToSave);
    }
}

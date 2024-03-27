package smoke;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import regression.BaseTest;
import tests.*;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SmokeTest extends BaseTest {
    LandingPage landingPage;
    LoginPage loginPage;
    HomePage homePage;
    MainMethods mainMethods;

    @Test
    void personalInformationButtonWorks(){
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();
        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);
        landingPage.clickLithuanianLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickEnglishLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickHomePage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='space-y-4'])[1]//button")));
        homePage.setBurgerMenu();
        homePage.clickPersonalInformation();
    }

    @Test
    void manageUsersButtonWorks(){
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();
        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);
        landingPage.clickLithuanianLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickEnglishLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickHomePage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='space-y-4'])[1]//button")));
        homePage.setBurgerMenu();
        homePage.clickManageUsers();
    }

    @Test
    void createContestButtonWorks(){
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();
        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);
        landingPage.clickLithuanianLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickEnglishLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickHomePage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='space-y-4'])[1]//button")));
        homePage.setBurgerMenu();
        homePage.clickCreateContest();
    }

    @Test
    void approveRequestButtonWorks(){
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();
        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);
        landingPage.clickLithuanianLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickEnglishLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickHomePage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='space-y-4'])[1]//button")));
        homePage.setBurgerMenu();
        homePage.clickApproveRequest();
    }

    @Test
    void archiveButtonWorks(){
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        mainMethods = new MainMethods(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();
        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);
        landingPage.clickLithuanianLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickEnglishLanguage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//img)[2]")));
        landingPage.clickHomePage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='space-y-4'])[1]//button")));
        homePage.setBurgerMenu();
        homePage.clickArchive();
    }
}

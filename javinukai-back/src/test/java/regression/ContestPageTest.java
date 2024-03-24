package regression;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.*;

import java.io.File;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ContestPageTest extends BaseTest {
    LandingPage landingPage;
    LoginPage loginPage;
    HomePage homePage;
    ContestPage contestPage;
    MainMethods mainMethods;
    @Test
    void userCanUploadPhoto() {
        String photo1 = new File("src/test/resources/photos/ZEN_5121.jpg").getAbsolutePath();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        contestPage = new ContestPage(driver);
        mainMethods = new MainMethods(driver);

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();

        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);
        landingPage.clickHomePage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='space-y-4'])[1]//button")));

        homePage.clickFirstContest();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='flex " +
                "hover:cursor-pointer justify-between px-3 py-2 w-full rounded-md bg-slate-50 text-slate-700'])" +
                "//h1[contains(.,'Single photo')][1]")));
        contestPage.clickSinglePhotoCategory();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[normalize-space()='Add new entry'])[1]")));
        contestPage.clickAddNewEntry();
        contestPage.setTitle("Upload photo");
        contestPage.setDescription("Upload photo test pass everything is fine");
        contestPage.setUploadPhoto(photo1);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@role='status']")));
        contestPage.clickConfirmationOfUpload();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='status']")));
        String expectedUpload = "Images have been uploaded";
        String actualUpload = mainMethods.waitForPopUp("Images have been uploaded");
        AssertionsForClassTypes.assertThat(actualUpload).isEqualTo(expectedUpload);
    }

    @Test
    void userCanUploadPhotos() {
        String photo1 = new File("src/test/resources/photos/ZEN_5121.jpg").getAbsolutePath();
        String photo2 = new File("src/test/resources/photos/ZEN_5425.jpg").getAbsolutePath();
        String photo3 = new File("src/test/resources/photos/ZEN_5605.jpg").getAbsolutePath();
        String photo4 = new File("src/test/resources/photos/ZEN_7955.jpg").getAbsolutePath();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        landingPage = new LandingPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        contestPage = new ContestPage(driver);
        mainMethods = new MainMethods(driver);

        landingPage.clickOnLogin();
        loginPage.setValidLoginUsername("jdoe@mail.com");
        loginPage.setValidLoginPassword("password");
        loginPage.setLoginButton();

        mainMethods.waitForPopUp("Logged in successfully");
        String expectedLogIn = "Logged in successfully";
        String actualLogIn = mainMethods.waitForPopUp("Logged in successfully");
        assertThat(actualLogIn).isEqualTo(expectedLogIn);
        landingPage.clickHomePage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='space-y-4'])[1]//button")));

        homePage.clickFirstContest();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='flex " +
                "hover:cursor-pointer justify-between px-3 py-2 w-full rounded-md bg-slate-50 text-slate-700'])" +
                "//h1[contains(.,'Single photo')][1]")));
        contestPage.clickPhotoCollectionsCategory();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[normalize-space()='Add new entry'])[1]")));
        contestPage.clickAddNewEntry();
        contestPage.setTitle("Upload photo");
        contestPage.setDescription("Upload photo test pass everything is fine");
        contestPage.setUploadPhotos(photo1, photo2, photo3, photo4);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@role='status']")));
        contestPage.clickConfirmationOfUpload();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='status']")));
        String expectedUpload = "Images have been uploaded";
        String actualUpload = mainMethods.waitForPopUp("Images have been uploaded");
        AssertionsForClassTypes.assertThat(actualUpload).isEqualTo(expectedUpload);
    }
}

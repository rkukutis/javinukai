package tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;

public class ContestPage extends BasePage {

    @FindBy (xpath = "(//div[@class='flex hover:cursor-pointer justify-between px-3 py-2 w-full rounded-md bg-slate-50 text-slate-700'])//h1[contains(.,'Single photo')][1]")
    private WebElement categorySinglePhoto;

    @FindBy (xpath = "(//div[contains(@class,\"flex hover:cursor-pointer justify-between px-3 py-2 w-full rounded-md bg-slate-50\")][1])//h1[contains(.,'Photo collections')][1]")
    private WebElement categoryPhotoCollections;
    @FindBy (xpath = "(//button[normalize-space()='Add new entry'])[1]")
    private WebElement addNewEntry;

    @FindBy (xpath = "//input[@name='title']")
    private WebElement inputTitle;

    @FindBy (xpath = "//textarea[@name='description']")
    private WebElement inputDescription;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement uploadPhotos;

    @FindBy(xpath = "//input[@id='image-upload-form']")
    private WebElement confirmUpload;

    public ContestPage(WebDriver driver) {
        super(driver);
    }
    public void clickSinglePhotoCategory(){
        categorySinglePhoto.click();
    }

    public void clickPhotoCollectionsCategory(){
        categoryPhotoCollections.click();
    }

    public void clickAddNewEntry(){
        addNewEntry.click();
    }

    public void setTitle(String title){inputTitle.sendKeys(title);}
    public void setDescription(String description){inputDescription.sendKeys(description);}

    public void setUploadPhoto(String filePath){uploadPhotos.sendKeys(filePath);}

    public void setUploadPhotos(String... filePaths) {
        for (String filePath : filePaths) {
            uploadPhotos.sendKeys(filePath);
        }
    }

    public  void  clickConfirmationOfUpload(){confirmUpload.click();}

}

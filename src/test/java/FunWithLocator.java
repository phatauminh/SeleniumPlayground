import helpers.StringHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FunWithLocator {
    WebDriver driver;
    WebDriverWait explicitWait;
    private String path = System.getProperty("user.dir");

    //Exercise 1: https://demoqa.com/automation-practice-form
    private By firstName;
    private By lastName;
    private By gender;
    private By hobby;
    private By email;
    private By phoneNumber;
    private By subject;
    private By uploadFile;
    private By address;
    private By state;
    private By city;
    private By stateOption;
    private By cityOption;
    private By btnSubmit;

    private By result;

    //Exercise 2: https://demoqa.com/webtables
    private By deleteRecord;

    //Exercise 3: https://tiki.vn
    private By searchInput;
    private By searchBtn;
    private By productInfo;

    //Exercise 4:
    private By selectedBox;
    private By popUp;
    private By checkIn;

    private By verifyCheckIn;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", path + "/drivers/chromedriver");
        driver = new ChromeDriver();
        explicitWait = new WebDriverWait(driver, 15);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        firstName = By.id("firstName");
        lastName = By.id("lastName");
        gender = By.xpath("//input[@name='gender']/following-sibling::label[text()='Male']");
        hobby = By.xpath("//input[@type='checkbox']/following-sibling::label[text()='Music']");
        email = By.xpath("//input[@id='userEmail']");
        phoneNumber = By.id("userNumber");
        subject = By.xpath("//input[@id='subjectsInput']");
        uploadFile = By.xpath("//input[@id='uploadPicture']");
        address = By.id("currentAddress");
        state = By.id("state");
        city = By.id("city");
        stateOption = By.xpath("//div[contains(@id,'react-select-3-option')]");
        cityOption = By.xpath("//div[contains(@id,'react-select-4-option')]");
        btnSubmit = By.id("submit");
        result = By.className("modal-header");

        searchInput = By.xpath("//input[@data-view-id='main_search_form_input']");
        searchBtn = By.xpath("//button[@data-view-id='main_search_form_button']");

        selectedBox = By.xpath("//div[@data-element-name='check-in-box']");
        popUp = By.className("Popup__content");
        checkIn = By.xpath("//div[@class='DayPicker-Months']//div[contains(text(),'October')]//parent::div//following-sibling::div//span[text() = '20']");
        verifyCheckIn = By.xpath("//div[contains(@class,'DayPicker-Day--selected DayPicker-Day--checkIn')]");
    }

    @Test
    public void TC_01_StudentRegistration() throws Exception {
        driver.get("https://demoqa.com/automation-practice-form");

        sendKey(firstName, "Phat");
        sendKey(lastName, "Au");
        sendKey(email, StringHelpers.GenerateRandomEmail());

        explicitWait.until(ExpectedConditions.elementToBeClickable(gender)).click();
        sendKey(phoneNumber, "0188886678");

        sendKey(subject, "Maths");
        driver.findElement(subject).sendKeys(Keys.ENTER);
        sendKey(subject, "English");
        driver.findElement(subject).sendKeys(Keys.ENTER);
        driver.findElement(subject).sendKeys(Keys.PAGE_DOWN);

        explicitWait.until(ExpectedConditions.elementToBeClickable(hobby)).click();

        sendKey(uploadFile, path + "/src/main/resources/note.txt");
        sendKey(address, "America NYC");

        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(state)).click();
        selectItemInDropdownList(stateOption, "Haryana");

        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(city)).click();
        selectItemInDropdownList(cityOption, "Panipat");

        explicitWait.until(ExpectedConditions.elementToBeClickable(btnSubmit)).click();

        Assert.assertEquals(explicitWait.until(ExpectedConditions.visibilityOfElementLocated(result)).getText(),
                "Thanks for submitting the form");
        Thread.sleep(3000);
    }

    @Test
    public void TC_02_TableControl() throws Exception {
        driver.get("https://demoqa.com/webtables");
        deleteSpecificItem("cierra@example.com");
        Thread.sleep(3000);
    }

    @Test
    public void TC_03_TikiPage() throws Exception {
        driver.get("https://tiki.vn");
        sendKey(searchInput, "điện thoại samsung");
        driver.findElement(searchBtn).click();
        getProductInfoAndAssertPrice("Điện Thoại Samsung Galaxy A02s (4GB/64GB) - Hàng Chính Hãng", "3.289.000 ₫");
        Thread.sleep(3000);
    }

    @Test
    public void TC_04_AgodaPage() throws  Exception{
        var expectedDayToPick = "20";
        driver.get("https://www.agoda.com/");

        explicitWait.until(ExpectedConditions.elementToBeClickable(selectedBox)).click();
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(popUp));
        pickCurrentMonthCheckInByDay(expectedDayToPick);

        Assert.assertEquals(driver.findElement(verifyCheckIn).getText(),expectedDayToPick);
        Thread.sleep(5000);

    }

    private void selectItemInDropdownList(By by, String expectedText) {
        List<WebElement> allStateItems = explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));

        for (var item : allStateItems) {
            if (item.getText().equals(expectedText)) {
                item.click();
                break;
            }
        }
    }

    private void deleteSpecificItem(String email) {
        deleteRecord = By.xpath("//div[@class = 'rt-tbody']/div[@class = 'rt-tr-group']//div[@class = 'rt-td' and text() ='" + email + "']/following-sibling::div/div[@class = 'action-buttons']/span[@title='Delete']");
        explicitWait.until(ExpectedConditions.elementToBeClickable(deleteRecord)).click();
    }

    private void getProductInfoAndAssertPrice(String productName, String expectedPrice) {
        productInfo = By.xpath("//a[@class = 'product-item']//span[text()='" + productName + "']/parent::div/following-sibling::div/div[@class='price-discount__price']");
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(productInfo));
        Assert.assertEquals(driver.findElement(productInfo).getText(), expectedPrice);
    }

    private void sendKey(By by, String words) {
        driver.findElement(by).sendKeys(words);
    }

    private void pickCurrentMonthCheckInByDay(String day)
    {
        Calendar c = Calendar.getInstance();
        var month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH );
        checkIn = By.xpath("//div[@class='DayPicker-Months']//div[contains(text(),'"+ month +"')]//parent::div//following-sibling::div//span[text() = '"+ day + "']");
        driver.findElement(checkIn).click();
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}

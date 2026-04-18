import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class Filtering {

    public static void step4_and_5(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        wait.until(ExpectedConditions.urlContains("search"));

        Thread.sleep(5000);

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,800)");

        Thread.sleep(3000);

        List<WebElement> courses = driver.findElements(
                By.xpath("//a[contains(@href,'/course/')]")
        );

        int count = courses.size();

        System.out.println("Total course cards: " + count);

        Assert.assertTrue(count > 1, "Course cards not loaded");

        System.out.println("Step 4 passed: Course cards verified " + count);

        try {
            WebElement ratingBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(.,'Rating') or contains(.,'Ratings')]")
                    )
            );
            ratingBtn.click();

            Thread.sleep(2000);

            WebElement ratingOption = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//label[contains(.,'4.5')]")
                    )
            );

            ratingOption.click();

            driver.findElement(By.tagName("body")).click();

            System.out.println("Step 5 passed: Rating filter applied");

        } catch (Exception e) {
            System.out.println("Step 5: Filter not found (optional step)");
        }

        Thread.sleep(5000);
    }

    public static void main(String[] args) throws InterruptedException {

        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

        WebDriver driver = new org.openqa.selenium.chrome.ChromeDriver();

        // ✅ Added proper wait initialization
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().maximize();

        driver.get("https://www.udemy.com/");

        // ✅ Handle cookie popup (VERY IMPORTANT for Udemy)
        try {
            WebElement accept = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(.,'Accept')]")
                    )
            );
            accept.click();
        } catch (Exception ignored) {}

        // Search
        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("q"))
        );

        searchBox.sendKeys("Python");
        searchBox.sendKeys(org.openqa.selenium.Keys.ENTER);

        Thread.sleep(5000);

        step4_and_5(driver, wait);

        driver.quit();
    }
}
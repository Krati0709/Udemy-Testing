import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Course_validation {

    @Test
    public void testCourseDetails() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Simulating that previous steps (1–5) are already done
        driver.get("https://www.udemy.com/courses/search/?q=python");

        // STEP 6: Open first course card with visible title
        List<WebElement> courses = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.xpath("//a[contains(@href,'/course/') and .//div]")
                )
        );

        Assert.assertTrue(courses.size() > 0, "No courses found");

        WebElement firstCourse = courses.get(0);
        String parent = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstCourse);

        // Switch to new tab
        for (String window : driver.getWindowHandles()) {
            if (!window.equals(parent)) {
                driver.switchTo().window(window);
                break;
            }
        }

        System.out.println("Step 6 passed: First course opened");

        // STEP 7: Validate course details

        // Title
        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1"))
        );

        // Instructor
        WebElement instructor = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[contains(@href,'/user/')]")
                )
        );

        // Rating (important as per use case)
        WebElement rating = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//span[contains(@class,'rating')]")
                )
        );

        System.out.println("Title: " + title.getText());
        System.out.println("Instructor: " + instructor.getText());
        System.out.println("Rating: " + rating.getText());

        // POSITIVE ASSERTIONS
        Assert.assertTrue(title.isDisplayed(), "Title not visible");
        Assert.assertTrue(instructor.isDisplayed(), "Instructor not visible");
        Assert.assertTrue(rating.isDisplayed(), "Rating not visible");

        System.out.println("Step 7 passed: Course details validated");

        driver.quit();
    }

    // NEGATIVE TEST
    @Test
    public void testCourseDetailsNegative() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        driver.get("https://www.udemy.com/course/100-days-of-code/");

        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[@id='invalid-element']")
                    )
            );

            Assert.fail("Negative test failed: Element should not exist");

        } catch (TimeoutException e) {
            System.out.println("Negative test passed: Element not found as expected");
        }

        driver.quit();
    }
}
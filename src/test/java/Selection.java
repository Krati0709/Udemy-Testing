import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Selection {

    public static void main(String[] args) throws IOException {

        // Setup driver
        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Open Udemy
        driver.get("https://www.udemy.com/");

        try { Thread.sleep(5000); } catch (Exception ignored) {}

        // Take Screenshot
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        File dest = new File("Udemy_screenshot.png");
        Files.copy(src.toPath(), dest.toPath());

        System.out.println("Screenshot saved");

        driver.quit();
    }
}
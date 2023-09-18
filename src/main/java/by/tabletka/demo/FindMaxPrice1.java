package by.tabletka.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FindMaxPrice1 {
    public  String findMaxPrice(Receipt receipt) throws InterruptedException {
        String second = receipt.getUrl();
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\matve\\OneDrive\\Рабочий стол\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(second);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 200);");
        Thread.sleep(1000);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[contains(@class,'tabs-complement-inn')]"), "Цене"));
        WebElement elementPrice = driver.findElement(By.xpath("//div[contains(@class,'tabs-complement-inn')]"));
        elementPrice.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//label[contains(@for,'r22')]"), "По убыванию"));

        WebElement button = driver.findElement(By.xpath("//label[contains(@for,'r22')]"));
        button.click();
        Thread.sleep(4000);
        WebElement price = driver.findElement(By.xpath("//span[contains(@class,'price-value')]"));
        String res = price.getText();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.close();

        return res;
    }
    }


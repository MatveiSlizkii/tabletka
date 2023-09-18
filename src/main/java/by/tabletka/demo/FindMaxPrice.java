package by.tabletka.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class FindMaxPrice {
    public  String findMaxPrice(Receipt receipt) throws InterruptedException {
        String second = receipt.getUrl();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(second);
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 200);");
        Thread.sleep(1000);
        WebElement elementPrice = driver.findElement(By.xpath("//div[contains(@class,'tabs-complement-inn')]"));
        elementPrice.click();
        Thread.sleep(1500);
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


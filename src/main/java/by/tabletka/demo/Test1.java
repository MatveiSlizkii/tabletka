package by.tabletka.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        String mainURL = "https://tabletka.by/result?ls=3024&region=1001";
        String second = "https://tabletka.by/result?ls=17447";
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\matve\\OneDrive\\Рабочий стол\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(second);
//        WebElement searchBox = driver.findElement(By.name("q"));
//        searchBox.sendKeys("Selenium with Java");
        // Получаем идентификатор основного окна (main window)
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 200);");
        Thread.sleep(1000);
        WebElement elementPrice = driver.findElement(By.xpath("//div[contains(@class,'tabs-complement-inn')]"));
        System.out.println(elementPrice.getText());
        elementPrice.click();
        Thread.sleep(1500);
        WebElement button = driver.findElement(By.xpath("//label[contains(@for,'r22')]"));
        System.out.println(button.getText());
        button.click();
        Thread.sleep(2000);
        WebElement price = driver.findElement(By.xpath("//span[contains(@class,'price-value')]"));
        System.out.println(price.getText());

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.close();


    }
}

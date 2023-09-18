package by.tabletka.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Listok1 {

    public static void main(String[] args) {



        System.setProperty("webdriver.chrome.driver", "C:\\Users\\matve\\OneDrive\\Рабочий стол\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();


        List<Receipt> list = new ArrayList<>();
        list.add(new Receipt("Иммунозин №30 500мг","https://tabletka.by/result?ls=17447"));
        list.add(new Receipt("Иммунозин №1 250мг/5мл","https://tabletka.by/result?ls=19159"));
        list.add(new Receipt("Иммунозин №50 500мг","https://tabletka.by/result?ls=18671"));
        list.forEach((o)-> {
            try {
                System.out.println(o.getUrl());
                o.setMaxPrice(findMaxPrice(o, driver));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        list.forEach(System.out::println);



    }

    public static String findMaxPrice(Receipt receipt, WebDriver driver) throws InterruptedException {
        String second = receipt.getUrl();

        driver.get(second);
//        WebElement searchBox = driver.findElement(By.name("q"));
//        searchBox.sendKeys("Selenium with Java");
        // Получаем идентификатор основного окна (main window)
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

package by.tabletka.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class Listok {
    public static void main(String[] args) {

        List<Receipt> list = new ArrayList<>();
        list.add(new Receipt("Иммунозин №30 500мг", "https://tabletka.by/result?ls=17447"));
        list.add(new Receipt("Иммунозин №1 250мг/5мл", "https://tabletka.by/result?ls=19159"));
        list.add(new Receipt("Иммунозин №50 500мг", "https://tabletka.by/result?ls=18671"));
        FindMaxPrice f = new FindMaxPrice();
        list.forEach((o) -> {
            try {
                System.out.println(o.getUrl());
                o.setMaxPrice(f.findMaxPrice(o));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        list.forEach(System.out::println);


    }

}

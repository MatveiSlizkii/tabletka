package by.tabletka.demo.newVersion.testsFunctions;

import java.text.DecimalFormat;

public class h21 {
    public static void main(String[] args) {
        String m1 = "9.90 ... 17.75";
        String[] values = m1.split(" ... ");
        double min = Double.valueOf(values[0]);
        double max = Double.valueOf(values[1]);
//
//        System.out.println(min); // 9.90
//        System.out.println(max); // 17.75

        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println(df.format(min)); // 9.90
    }
}

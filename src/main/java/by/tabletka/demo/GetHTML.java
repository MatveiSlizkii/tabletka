package by.tabletka.demo;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetHTML {

    public static void main(String[] args) throws IOException, InterruptedException {
        int page = 1;
        long m1 = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 1000; i++) {
            URL url = new URL("https://tabletka.by/result?ls=15283&sort=address&page=" + page);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            String html;
            if (responseCode == 200) {
                Thread.sleep(500);
                InputStream inputStream = connection.getInputStream();
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                stringBuilder.append(bytes);

                System.out.println(i);
                stringBuilder.delete(0, stringBuilder.length());
            } else {
                System.out.println("Error: " + responseCode);
            }
            page++;
        }

        long m2 = System.currentTimeMillis();
        System.out.println(m2-m1);

    }
}
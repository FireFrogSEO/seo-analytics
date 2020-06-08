package page.webx.seoanalytics;

import com.google.api.services.customsearch.model.Result;
import page.webx.seoanalytics.dbWorker;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        Desktop desktop = java.awt.Desktop.getDesktop();

        //далее - работа с консолью и получение основных данных от пользователя
        //TODO: хранить данные локально, чтобы не вводить каждый раз. Простите за то, что не сделал сразу :)
        System.out.println(Config.getWebxText());
        System.out.println("Здравствуйте! Перейдем к делу." + "\n"
                + "Введите ваш Google Api Key");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        Config.setGoogleApiKey(s);

        System.out.println("Теперь ID вашего Custom Search Engine" + "\n" +
                "Его можно получить в консоли CSE. Вы можете ввести \"console\", чтобы открыть ее в браузере, установленном по умолчанию");

        s = br.readLine();
        if (s.equalsIgnoreCase("console")) {
            desktop.browse(java.net.URI.create("https://cse.google.com/cse/all"));
            System.out.println("После нахождения Search Engine ID введите его здесь");
            Config.setSeId(br.readLine());
        } else {
            Config.setSeId(s);
        }

        System.out.println("Теперь введите API-ключ Serpstat");
        s = br.readLine();
        Config.setSerpstatKey(s);

        System.out.println("Введите URL MySQL-базы в формате adress:port" + "\n" +
                "Например, localhost:3306 или 192.168.1.1:3306");
        s = br.readLine();
        Config.setDbUrl(s);
        Config.setDbName("results");

        System.out.println("Введите имя пользователя БД и пароль в формате user:password" + "\n" +
                "Например, root:123456");
        s = br.readLine();
        String[] up = s.split(":");
        Config.setDbUser(up[0]);
        Config.setDbPass(up[1]);

        System.out.println("Создаю базу данных и готовлю ее к работе" +
                "\n" +
                "Ожидайте строку с подтверждением");

        dbWorker.inititialLoad();
        System.out.println("База данных создана и готова к работе");

        System.out.println("Теперь вводите ключевые слова для анализа по очереди или exit для завершения работы программы");


        while (s != "exit") {
            List<Result> results = new ArrayList<>();

            s = br.readLine();
            try {
                results = searchWorker.search(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int position = 1;
            for (Result result : results) {
                dbWorker.setGoogleResults(s, result.getLink(), result.getDisplayLink(), result.getTitle(), position);
                serpstatWorker.serpstatQuery(result.getDisplayLink());

                System.out.println("В базу успешно записан " + result.getLink() + ", с позиции в выдаче номер " + position + ".");
                position++;
                if (position == 20 || position > 20) {System.out.println("Теперь можно ввести следующий запрос");};
            }
        }

        System.out.println("Для перехода на WebX.page введите любой символ или набор символов");
        if (br.readLine().length() > 0) {
            desktop.browse(java.net.URI.create("https://webx.page?utm_source=seo-analyzer"));
        }
        System.out.println("Отлично! Теперь вы можете посмотреть файл R scripts и проанализировать свои данные!");
    }

}

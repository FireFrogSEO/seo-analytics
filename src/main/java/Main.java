import com.google.api.services.customsearch.model.Result;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        dbWorker.inititialLoad();

        List<Result> results = new ArrayList<>();

        String keyword = "конструктор сайта интернет магазина";
        try {
            results = searchWorker.search(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int position = 1;
        for (Result result : results) {
       //     System.out.println(keyword);
        //    System.out.println(results);
        //    System.out.println(result.getDisplayLink());
            dbWorker.setGoogleResults(keyword, result.getLink(), result.getDisplayLink(), result.getTitle(), position);
            serpstatWorker.serpstatQuery(result.getDisplayLink());

            System.out.println("В базу успешно записан " + result.getLink() + ", с позиции в выдаче номер " + position + ". Вместе с данными из серпстата");
            position++;
        }
    }
}

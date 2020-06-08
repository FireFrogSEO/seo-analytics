package page.webx.seoanalytics;

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
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class serpstatWorker {
    protected static void serpstatQuery(String query) {

    HttpClient httpclient = HttpClients.createDefault();
    HttpPost request = new HttpPost("http://api.serpstat.com/v4/?token=" + Config.getSerpstatKey()); //URL, по которому будут проводиться запросы

    JSONObject serpstatQuery = new JSONObject();
        serpstatQuery.put("id", "webx.page"); //id запроса, может быть любым набором символов или чисел
        serpstatQuery.put("method", "SerpstatBacklinksProcedure.getSummary"); //испольуемый метод

    JSONArray serpstatSubQuery = new JSONArray();

    JSONObject subQuery = new JSONObject();
        subQuery.put("query", query); //исследуемый домен
        System.out.println(query);
        serpstatQuery.put("params", subQuery); //совмещаем все вместе

    String jsonStr = serpstatQuery.toString();

        try {
            request.setEntity(new StringEntity(jsonStr)); //добавляем все необходимые данные к запросу
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setHeader("Content-type", "application/json");

        HttpResponse response = null;
        try {
            response = httpclient.execute(request); //получаем ответ от SerpStat
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();

        if (entity != null) {
        try (InputStream instream = entity.getContent()) {
            String result = convertInputStreamToString(instream); //конвертируем полученный ответ в удобную строку
            System.out.println(result);
            try {
                dbWorker.setSerpstatResults(parseResults(result), query); //записываем все в базу
                System.out.println("В базу записаны данные по " + query);
                Thread.sleep(1000*2); //чтобы не было превышения лимитов, ждем 2сек
            } catch (Exception e) {
                System.out.println("Ошибка в парсинге результатов серпстата, скорее всего неподдерживаемый домен");
                Thread.sleep(1000*2); //чтобы не было превышения лимитов
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        }
}

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());

    }

    private static HashMap<String, String> parseResults (String obj){ //в этом методе мы расшифровываем ответ серпстата. ВАЖНО: внутри jsonObject хранится не sonArray, а тоже jsonObject. Странное решение, но не нам с ним спорить :)

        HashMap<String, String> results = new HashMap<String, String>();

        JSONObject json = new JSONObject(obj);

        JSONObject nested = json.getJSONObject("result").getJSONObject("data");

        for (String keyStr : nested.keySet()) {
            Object keyvalue = nested.get(keyStr);
            results.put(keyStr, keyvalue.toString());
        }

        return results;
    }

}

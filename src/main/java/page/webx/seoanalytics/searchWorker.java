package page.webx.seoanalytics;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.util.ArrayList;
import java.util.List;

public class searchWorker {
    private static final int HTTP_REQUEST_TIMEOUT = 3 * 300000; //сколько ждем ответа
    private static final String API_KEY = Config.getGoogleApiKey(); //устанавливаем апи ключ
    private static final String SE_ID = Config.getSeId(); //устанавливаем search engine id

    public static List<Result> search(String keyword) {
        Customsearch customsearch = null;


        try {
            customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest httpRequest) {
                    try {
                        httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
                        httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Result> resultList = null;
        List<Result> totalResults = new ArrayList<Result>();

        try {
            Customsearch.Cse.List list = customsearch.cse().list(keyword);
            list.setKey(API_KEY);
            list.setCx(SE_ID);
            for(long i = 0 ; i < 20 ; i+=10) { //записываем ответ в массив
                   list.setStart(i);
                Search results = list.execute();
                resultList = results.getItems();
                totalResults.addAll(resultList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalResults; //возвращаем получившийся массив для дальнейшей работы
    }
}
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

        //����� - ������ � �������� � ��������� �������� ������ �� ������������
        //TODO: ������� ������ ��������, ����� �� ������� ������ ���. �������� �� ��, ��� �� ������ ����� :)
        System.out.println(Config.getWebxText());
        System.out.println("������������! �������� � ����." + "\n"
                + "������� ��� Google Api Key");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        Config.setGoogleApiKey(s);

        System.out.println("������ ID ������ Custom Search Engine" + "\n" +
                "��� ����� �������� � ������� CSE. �� ������ ������ \"console\", ����� ������� �� � ��������, ������������� �� ���������");

        s = br.readLine();
        if (s.equalsIgnoreCase("console")) {
            desktop.browse(java.net.URI.create("https://cse.google.com/cse/all"));
            System.out.println("����� ���������� Search Engine ID ������� ��� �����");
            Config.setSeId(br.readLine());
        } else {
            Config.setSeId(s);
        }

        System.out.println("������ ������� API-���� Serpstat");
        s = br.readLine();
        Config.setSerpstatKey(s);

        System.out.println("������� URL MySQL-���� � ������� adress:port" + "\n" +
                "��������, localhost:3306 ��� 192.168.1.1:3306");
        s = br.readLine();
        Config.setDbUrl(s);
        Config.setDbName("results");

        System.out.println("������� ��� ������������ �� � ������ � ������� user:password" + "\n" +
                "��������, root:123456");
        s = br.readLine();
        String[] up = s.split(":");
        Config.setDbUser(up[0]);
        Config.setDbPass(up[1]);

        System.out.println("������ ���� ������ � ������� �� � ������" +
                "\n" +
                "�������� ������ � ��������������");

        dbWorker.inititialLoad();
        System.out.println("���� ������ ������� � ������ � ������");

        System.out.println("������ ������� �������� ����� ��� ������� �� ������� ��� exit ��� ���������� ������ ���������");


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

                System.out.println("� ���� ������� ������� " + result.getLink() + ", � ������� � ������ ����� " + position + ".");
                position++;
                if (position == 20 || position > 20) {System.out.println("������ ����� ������ ��������� ������");};
            }
        }

        System.out.println("��� �������� �� WebX.page ������� ����� ������ ��� ����� ��������");
        if (br.readLine().length() > 0) {
            desktop.browse(java.net.URI.create("https://webx.page?utm_source=seo-analyzer"));
        }
        System.out.println("�������! ������ �� ������ ���������� ���� R scripts � ���������������� ���� ������!");
    }

}

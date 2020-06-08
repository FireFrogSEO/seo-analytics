package page.webx.seoanalytics;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class dbWorker {

    private static Connection createConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://" + Config.getDbUrl()+ "/?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Kiev", Config.getDbUser(), Config.getDbPass());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            return connection;
        } else {
            return null;
        }
    }

    static void inititialLoad() throws SQLException {

        Connection conn = createConnection();

    String createDB = "create database if not exists " + Config.getDbName() + ";";
  //  String useDB = "use results;";

        conn.setCatalog(Config.getDbName());

    String createGoogleResults = "CREATE TABLE IF NOT EXISTS google_results ( " +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "keyword VARCHAR(255) NOT NULL, " +
            "link varchar(255), " +
            "title varchar(255), " +
            "displayLink varchar(255), " +
            "position varchar(3)" +
            ");";

    String createSerpstatResults = "CREATE TABLE IF NOT EXISTS serpstat_results (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "link varchar(255)," +
            "referringDomains INT," +
            "referringSubDomains INT," +
            "referringLinks INT," +
            "totalIndexed INT," +
            "externalDomains INT," +
            "noFollowLinks INT," +
            "doFollowLinks INT," +
            "referringIps INT," +
            "referringSubnets INT," +
            "trustRank INT," +
            "citationRank INT," +
            "domainZoneEdu INT," +
            "domainZoneGov INT," +
            "outlinksTotal INT," +
            "outlinksUnique INT," +
            "facebookLinks INT," +
            "pinterestLinks INT," +
            "linkedinLinks INT," +
            "vkLinks INT," +
            "typeText INT," +
            "typeImg INT," +
            "typeRedirect INT," +
            "typeAlt INT," +
            "referringDomainsDynamics INT," +
            "referringSubDomainsDynamics INT," +
            "referringLinksDynamics INT," +
            "totalIndexedDynamics INT," +
            "externalDomainsDynamics INT," +
            "noFollowLinksDynamics INT," +
            "doFollowLinksDynamics INT," +
            "referringIpsDynamics INT," +
            "referringSubnetsDynamics INT," +
            "trustRankDynamics INT," +
            "citationRankDynamics INT," +
            "domainZoneEduDynamics INT," +
            "domainZoneGovDynamics INT," +
            "outlinksTotalDynamics INT," +
            "outlinksUniqueDynamics INT," +
            "facebookLinksDynamics INT," +
            "pinterestLinksDynamics INT," +
            "linkedinLinksDynamics INT," +
            "vkLinksDynamics INT," +
            "typeTextDynamics INT," +
            "typeImgDynamics INT," +
            "typeRedirectDynamics INT," +
            "typeAltDynamics INT," +
            "threats INT," +
            "threatsDynamics INT," +
            "mainPageLinks INT," +
            "mainPageLinksDynamics INT," +
            "domainRank DOUBLE" +
            ");";

        Statement stat = conn.createStatement();
        stat.executeUpdate(createDB);
        stat.executeUpdate(createGoogleResults);
        stat.executeUpdate(createSerpstatResults);

        stat.close();
        conn.close();
    }

    protected static void setGoogleResults(String keyword, String link, String displayLink, String title, int position) throws SQLException {
        Connection conn = createConnection();
        conn.setCatalog(Config.getDbName());
        PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO google_results (keyword, link, displayLink, title, position) " + "VALUES (?, ?, ?, ?, ?)");

        insert.setString(1, keyword);
        insert.setString(2, link);
        insert.setString(3, displayLink);
        insert.setString(4, title);
        insert.setInt(5, position);

        try {
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        insert.close();
        conn.close();
    }

    protected static void setSerpstatResults(HashMap<String, String> serpResults, String link) throws SQLException {
        Connection conn = createConnection();
        conn.setCatalog(Config.getDbName());
        String insert1 ="INSERT INTO serpstat_results (id, "; //50 values excepting link and rank
        String insert2 = "link) values (" + "\"" + dbWorker.getMaxIdGoogleResults() + "\""  + ", ";
        Iterator it = serpResults.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            insert1 += pair.getKey() + ", ";
            insert2 += "\"" + pair.getValue() +  "\"" + ", ";
            it.remove();
        }

        String totalQuery = insert1 + insert2 + "\"" + link +  "\"" + ")";

       // System.out.println(totalQuery);

        PreparedStatement ps=conn.prepareStatement(totalQuery);

        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ps.close();
        conn.close();

    }

    private static int getMaxIdGoogleResults() throws SQLException {
        int id = 0;

        Connection conn = createConnection();
        conn.setCatalog(Config.getDbName());

        try {
            String query = "select id from google_results where  id = (select max(id) from google_results)";


            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        conn.close();

        return id;
    }
}


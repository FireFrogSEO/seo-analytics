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
                    .getConnection("","","");

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

    String createDB = "create database if not exists results;";
  //  String useDB = "use results;";

        conn.setCatalog("results");

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
     //   stat.executeUpdate(useDB);
      //  conn.setCatalog("result");
        stat.executeUpdate(createGoogleResults);
        stat.executeUpdate(createSerpstatResults);

        stat.close();
        conn.close();
    }

    protected static void setGoogleResults(String keyword, String link, String displayLink, String title, int position) throws SQLException {
        Connection conn = createConnection();
        conn.setCatalog("results");
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
        conn.setCatalog("results");
//INSERT INTO serpstat_results (link, domainRank, delfn) values (
        String insert1 ="INSERT INTO serpstat_results (id, "; //50 values excepting link and rank
        String insert2 = "link) values (" + "\"" + dbWorker.getMaxIdGoogleResults() + "\""  + ", ";
        Iterator it = serpResults.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            insert1 += pair.getKey() + ", ";
            insert2 += "\"" + pair.getValue() +  "\"" + ", ";
            it.remove(); // avoids a ConcurrentModificationException
        }

        String totalQuery = insert1 + insert2 + "\"" + link +  "\"" + ")";

        System.out.println(totalQuery);

        PreparedStatement ps=conn.prepareStatement(totalQuery);

        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ps.close();
        conn.close();

    }

    protected static void setSerpstatResults(int maxId) throws SQLException { //перегруженный метод, чтобы не терялся порядок в id

    }

    private static int getMaxIdGoogleResults() throws SQLException {
        int id = 0;

        Connection conn = createConnection();
        conn.setCatalog("results");

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

    /*public static void setTest() throws SQLException {
        Connection conn = createConnection();
        conn.setCatalog("results");

        int position = 0;
        try {
            String googleQuery = "INSERT INTO google_results (keyword, link, displayLink, title, position) " + "VALUES (keyword, link, displayLink, title," + position + ")";
            String serpstatQuery = "INSERT INTO serpstat_results (id, referringIps, domainZoneGov, typeImg, vkLinks, typeRedirectDynamics, trustRankDynamics, vkLinksDynamics, mainPageLinksDynamics, facebookLinks, referringSubDomainsDynamics, typeAlt, linkedinLinksDynamics, mainPageLinks, referringDomains, typeText, outlinksUniqueDynamics, doFollowLinksDynamics, noFollowLinksDynamics, outlinksUnique, doFollowLinks, linkedinLinks, domainRank, referringDomainsDynamics, threatsDynamics, noFollowLinks, typeImgDynamics, domainZoneEdu, pinterestLinksDynamics, threats, typeAltDynamics, referringSubDomains, referringIpsDynamics, outlinksTotal, totalIndexedDynamics, totalIndexed, typeRedirect, typeTextDynamics, domainZoneGovDynamics, externalDomains, referringSubnets, trustRank, facebookLinksDynamics, referringSubnetsDynamics, citationRankDynamics, referringLinks, citationRank, pinterestLinks, referringLinksDynamics, externalDomainsDynamics, domainZoneEduDynamics, outlinksTotalDynamics, link) values (" + getMaxIdGoogleResults() + ", \"31\", \"0\", \"2\", \"0\", \"1000\", \"0\", \"0\", \"-1\", \"0\", \"0\", \"0\", \"0\", \"7\", \"41\", \"2000\", \"0\", \"2000\", \"0\", \"0\", \"5000\", \"0\", \"0\", \"0\", \"0\", \"385\", \"0\", \"0\", \"0\", \"0\", \"0\", \"8\", \"0\", \"0\", \"0\", \"0\", \"4772\", \"9\", \"0\", \"0\", \"40\", \"0\", \"0\", \"0\", \"0\", \"4658\", \"0\", \"0\", \"5\", \"0\", \"0\", \"0\", \"link\")";
        } catch(Exception e) {

        }
        */
}


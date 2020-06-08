package page.webx.seoanalytics;

public class Config {

    private static String DB_USER= "";
    private static String DB_PASS= "";
    private static String DB_URL= "jdbc:mysql://localhost:3306/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Kiev&characterEncoding=utf8";
//^localhost or remote ip
    private static String GOOGLE_API_KEY = "";
    private static String SE_ID = "";

    private static String SERPSTAT_KEY = "";

    private static String DB_NAME = "results";

    public static String getDbUser() {
        return DB_USER;
    }

    public static void setDbUser(String dbUser) {
        DB_USER = dbUser;
    }

    public static String getDbPass() {
        return DB_PASS;
    }

    public static void setDbPass(String dbPass) {
        DB_PASS = dbPass;
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static void setDbUrl(String dbUrl) {
        DB_URL = dbUrl;
    }

    public static String getGoogleApiKey() {
        return GOOGLE_API_KEY;
    }

    public static void setGoogleApiKey(String googleApiKey) {
        GOOGLE_API_KEY = googleApiKey;
    }

    public static String getSeId() {
        return SE_ID;
    }

    public static void setSeId(String seId) {
        SE_ID = seId;
    }

    public static String getSerpstatKey() {
        return SERPSTAT_KEY;
    }

    public static void setSerpstatKey(String serpstatKey) {
        SERPSTAT_KEY = serpstatKey;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static void setDbName(String dbName) {
        DB_NAME = dbName;
    }

    private static String webxText = "From WebX.page with Love";

    public static String getWebxText() {
        return webxText;
    }
}

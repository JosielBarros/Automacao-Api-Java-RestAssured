package features.clients.lojinha;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

public class BaseConfig {
    private static final String BASE_URI = "http://165.227.93.41";
    private static final String BASE_PATH = "/lojinha";
    public static void configApis(){
        baseURI = BASE_URI;
        basePath = BASE_PATH;
    }
}

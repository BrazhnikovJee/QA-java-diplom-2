package constants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class UrlApi {
    public static final String URL = "https://stellarburgers.nomoreparties.site";
    public static final String REGISTER = "api/auth/register";
    public static final String USER = "api/auth/user";
    public static final String LOGIN = "/api/auth/login";
    public static final String ORDERS = "/api/orders";
    public static final String INGREDIENTS = "/api/ingredients";
    //указываем, что нам надо иметь в спецификации URL и Content-Type Json.
    public static RequestSpecification spec() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri(URL)
                .log()
                .all();

    }
}
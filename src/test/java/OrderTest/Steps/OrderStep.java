package OrderTest.Steps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import order.Ingredient;
import order.Order;

import static constants.UrlApi.*;

public class OrderStep {
    @Step("Получение данных об ингредиентах.")
    public static Ingredient getIngredient() {
        return spec()
                .when()
                .get(INGREDIENTS)
                .body()
                .as(Ingredient.class);
    }
    @Step("Создание заказа без токена авторизации")
    public static Response createOrderWithoutToken(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDERS);
    }
    @Step("Создание заказа с токеном авторизации")
    public static Response createOrderWithToken(Order order, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS);
    }
    @Step("Получение заказа с токеном авторизации")
    public static Response getOrders(String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .when()
                .get(ORDERS);
    }
    @Step("Получение заказа без токена авторизации в заголовке")
    public static Response getOrders() {
        return spec()
                .when()
                .get(ORDERS);
    }
}
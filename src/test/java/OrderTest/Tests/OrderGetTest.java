package OrderTest.Tests;

import OrderTest.Steps.OrderStep;
import UserTest.Steps.UserStep;
import order.Ingredient;
import order.Order;
import org.junit.Before;
import user.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

@DisplayName("Получение заказа")
public class OrderGetTest {
    private static String accessToken;
    private static Response response;

    @Before
    public void createUserAndOrder() {
        //Создание пользователя
        UserData userData = UserStep.createNewUser();
        response = UserStep.createUser(userData);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //Создание заказа
        Ingredient ingredient = OrderStep.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredient.getData().get(1).get_id());
        ingredient1.add(ingredient.getData().get(2).get_id());
        ingredient1.add(ingredient.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        OrderStep.createOrderWithToken(order, accessToken);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserStep.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное получение заказа")
    @Description("Пользователь авторизован")
    public void userOrdersWithAuthorization() {
        response = OrderStep.getOrders(accessToken);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true)).and().body("orders", Matchers.notNullValue()).and().body("total", Matchers.any(Integer.class)).and().body("totalToday", Matchers.any(Integer.class));
    }

    @Test
    @DisplayName("Неуспешное получение заказа")
    @Description("Пользователь не авторизован")
    public void userOrdersWithoutAuthorization() {
        response = OrderStep.getOrders();
        response.then().log().all().assertThat().statusCode(401).and().body("success", Matchers.is(false)).and().body("message", Matchers.is("You should be authorised"));
    }
}
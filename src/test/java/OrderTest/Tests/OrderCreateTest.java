package OrderTest.Tests;

import OrderTest.Steps.OrderStep;
import UserTest.Steps.UserStep;
import order.Ingredient;
import order.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.hamcrest.Matchers;
import user.UserData;

import java.util.ArrayList;

@DisplayName("Создание заказа")
public class OrderCreateTest {
    private static String accessToken;
    ArrayList<String> ingredient1 = new ArrayList<>();
    Ingredient ingredient;
    @Before
    public void createUserAndGetIngredient() {
        //Создание пользователя
        UserData userData = UserStep.createNewUser();
        Response response = UserStep.createUser(userData);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //Получение ингредиентов
        ingredient = OrderStep.getIngredient();
        ingredient1.add(ingredient.getData().get(0).get_id());
        ingredient1.add(ingredient.getData().get(1).get_id());
        ingredient1.add(ingredient.getData().get(2).get_id());
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserStep.deleteUser(accessToken);
        }
    }
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Ингредиенты добавлены, токен не передан")
    public void createOrderNoAuthWithIngredients() {
        Order order = new Order(ingredient1);
        Response response = OrderStep.createOrderWithoutToken(order);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Ингредиенты добавлены, токен передан")
    public void createOrderWithAuthAndIngredients() {
        Order order = new Order(ingredient1);
        Response response = OrderStep.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Ингредиенты не добавлены, токен передан.")
    public void createOrderWithAuthNoIngredients() {
        Order order = new Order(null);
        Response response = OrderStep.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов и без авторизации")
    @Description("Ингредиенты не добавлены, токен не передан.")
    public void createOrderNoAuthNoIngredients() {
        Order order = new Order(null);
        Response response = OrderStep.createOrderWithoutToken(order);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа с невалидным ингредиентом")
    @Description("Добавлен один невалидный ингредиент, токен передан.")
    public void createOrderWithAuthIngredientsNotValid() {
        ingredient1.add(ingredient.getData().get(0).get_id() + "qwe");
        Order order = new Order(ingredient1);
        Response response = OrderStep.createOrderWithToken(order, accessToken);
        response.then().log().all().assertThat().statusCode(500);
    }
}
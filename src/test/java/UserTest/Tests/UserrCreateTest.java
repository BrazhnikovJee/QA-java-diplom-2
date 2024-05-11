package UserTest.Tests;

import UserTest.Steps.UserStep;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.hamcrest.Matchers;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import user.UserData;


@RunWith(JUnit4.class)
@DisplayName("Создание пользователя")
public class UserrCreateTest {
    private static String accessToken;
    private static Response response;
    private static UserData userData;

    @Before
    public void createUser() {
        userData = UserStep.createNewUser();
        response = UserStep.createUser(userData);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserStep.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Пользователя не существует в БД, поля уникальные")
    public void createSuccessUser() {
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true)).and().body("accessToken", Matchers.notNullValue()).and().body("refreshToken", Matchers.notNullValue()).and().body("user.email", Matchers.equalTo(userData.getEmail())).and().body("user.name", Matchers.equalTo(userData.getName()));
    }

    @Test
    @DisplayName("Повторное создание существующего пользователя")
    @Description("Указаны данные, зарегистрированного пользователя")
    public void createExistUser() {
        response = UserStep.createUser(userData);
        response.then().log().all().assertThat().statusCode(403).and().body("success", Matchers.is(false)).and().body("message", Matchers.is("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без Email")
    @Description("Заполнены все поля, кроме Email")
    public void createUserWithoutEmail() {
        userData.setEmail(null);
        response = UserStep.createUser(userData);
        response.then().log().all().assertThat().statusCode(403).and().body("success", Matchers.is(false)).and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Заполнены все поля, кроме name")
    public void createUserWithoutName() {
        userData.setName(null);
        response = UserStep.createUser(userData);
        response.then().log().all().assertThat().statusCode(403).and().body("success", Matchers.is(false)).and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без passwrod")
    @Description("Заполнены все поля, кроме password")
    public void createUserWithoutPassword() {
        userData.setPassword(null);
        response = UserStep.createUser(userData);
        response.then().log().all().assertThat().statusCode(403).and().body("success", Matchers.is(false)).and().body("message", Matchers.is("Email, password and name are required fields"));
    }
}

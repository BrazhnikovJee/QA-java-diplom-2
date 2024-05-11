package UserTest.Tests;

import UserTest.Steps.UserStep;
import org.junit.Before;
import user.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

@DisplayName("Логин пользователя")
public class UserrLoginTest {
    String accessToken;
    Response response;
    UserData userData;

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
    @DisplayName("Успешная авторизация")
    @Description("Логин существующего пользователя")
    public void loginSuccessUserExists() {
        response = UserStep.loginUser(userData);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.equalTo(userData.getEmail()))
                .and().body("user.name", Matchers.equalTo(userData.getName()));
    }
    @Test
    @DisplayName("Успешная авторизация без параметра Name")
    @Description("Логин под существующим пользователем. Имя не передано")
    public void loginSuccessUserExistsWithoutName() {
        userData.setName(null);
        response = UserStep.loginUser(userData);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.equalTo(userData.getEmail()));
    }
    @Test
    @DisplayName("Авторизация пользователя без Email")
    @Description("Параметр Email не передан")
    public void loginSuccessUserExistsWithoutEmail() {
        userData.setEmail(null);
        response = UserStep.loginUser(userData);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Авторизация без параметра password")
    @Description("Параметр Password не передан")
    public void loginSuccessUserExistsWithoutPassword() {
        userData.setPassword(null);
        response = UserStep.loginUser(userData);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Авторизация с несуществующим Email")
    @Description("Передан некорректный параметр Email")
    public void loginUnsuccessfulUserNotExists() {
        userData.setEmail("qwe" + userData.getEmail());
        response = UserStep.loginUser(userData);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }
    @Test
    @DisplayName("Авторизация с невалидным Password")
    @Description("Передан некорректный параметр Password")
    public void loginUnsuccessfulPasswordIncorrect() {
        userData.setPassword("qwe" + userData.getPassword());
        response = UserStep.loginUser(userData);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

}
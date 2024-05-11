package UserTest.Tests;

import UserTest.Steps.UserStep;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserData;

@DisplayName("Обновление пользователя")
public class UserrUpdateTest {
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
    @DisplayName("Обновление имя и e-mail пользователя")
    @Description("Переданы новые параметры Name и Email")
    public void updateSuccessUser() {
        userData.setName("qwe" + userData.getName());
        userData.setEmail("qwe" + userData.getEmail());
        response = UserStep.updateUser(userData, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("user.email", Matchers.equalTo(userData.getEmail()))
                .and().body("user.name", Matchers.equalTo(userData.getName()));
    }

    @Test
    @DisplayName("Обновление имя и e-mail с некорректным токеном")
    @Description("Передан некорректный токен")
    public void updateUserWithIncorrectToken() {
        userData.setName("name" + userData.getName());
        userData.setEmail("email" + userData.getEmail());
        String incorrectToken = accessToken + "abc";
        response = UserStep.updateUser(userData, incorrectToken);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("invalid signature"));
    }

    @Test
    @DisplayName("Обновление имя и e-mail без токена")
    @Description("Токен не передан")
    public void updateUserWithoutToken() {
        userData.setName("name" + userData.getName());
        userData.setEmail("email" + userData.getEmail());
        response = UserStep.updateUserWithoutToken(userData);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @Test
    @DisplayName("Обновление пароля пользователя")
    @Description("Передан новый парамер Password")
    public void updatePasswordUser() {
        userData.setPassword("password" + userData.getPassword());
        response = UserStep.updateUser(userData, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200);
        response = UserStep.loginUser(userData);
        response.then().log().all().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Успешное обновление пароля пользователя без токена")
    @Description("токен авторизации не передан")
    public void updatePasswordUserWithoutToken() {
        userData.setPassword("password" + userData.getPassword());
        response = UserStep.updateUserWithoutToken(userData);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
}
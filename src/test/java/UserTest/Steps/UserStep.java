package UserTest.Steps;

import user.CredentialsRandomizer;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.UserData;

import static constants.UrlApi.*;

public class UserStep {
    public static UserData createNewUser() {
        return CredentialsRandomizer.getNewRandomUser();
    }

    @Step("Создание пользователя")
    public static Response createUser(UserData userData) {
        return spec()
                .body(userData)
                .when()
                .post(REGISTER);

    }

    @Step("Удаление пользователя")
    public static void deleteUser(String token) {
        spec()
                .header("Authorization", token)
                .when()
                .delete(USER);
    }

    @Step("Авторизация пользователя")
    public static Response loginUser(UserData userData) {
        return spec()
                .body(userData)
                .when()
                .post(LOGIN);
    }

    @Step("Обновление пользователя")
    public static Response updateUser(UserData userData, String accessToken) {
        return spec()
                .header("Authorization", accessToken)
                .body(userData)
                .when()
                .patch(USER);
    }

    @Step("Обновление пользователя без токена в заголовке")
    public static Response updateUserWithoutToken(UserData userData) {
        return spec()
                .body(userData)
                .when()
                .patch(USER);
    }
}

import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.CourierClient;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoginCourierTest {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        String json = "{\"login\": \"kaktus1\", \"password\": \"12345\", \"firstName\": \"kaktus\"}";
        CourierClient createCourier = new CourierClient();
        createCourier.createCourier(json);
    }

    @AfterClass
    public static void deleteCourier() {
        String json = "{\"login\": \"kaktus1\", \"password\": \"12345\"}";
        CourierClient courierClient = new CourierClient();
        courierClient.deleteCourier(json);
    }

    @Step("Отправка запроса")
    public Response sendPostRequestLoginCourier(String json) {
        CourierClient logInCourier = new CourierClient();
        Response response = logInCourier.logInCourier(json);
        return response;
    }

    @Step("Проверка кода ошибки и текстового сообщения")
    public void checkStatusCodeAndBody(Response response, int statusCode, String message) {
        response.then().assertThat().statusCode(statusCode)
            .and()
            .body("message", equalTo(message));
    }

    @Step("Проверка корректного логина в системе")
    public void checkCorrectLogin(Response response, int statusCode) {
        response.then().assertThat().statusCode(statusCode)
            .and()
            .assertThat().body("$", Matchers.hasKey("id"));
    }

    @Test
    @DisplayName("Check login with all required fields")
    @Description("Проверка логина курьера со всеми необходимыми полями")
    public void checkLoginWithAllRequiredFields() {
        Response response = sendPostRequestLoginCourier(
            "{\"login\": \"kaktus1\", \"password\": \"12345\"}");
        checkCorrectLogin(response, 200);

    }

    @Test
    @DisplayName("Check login courier with wrong login")
    @Description("Проверка попытки залогиниться с неправильным логином")
    public void checkLoginWithWrongLogin() {
        Response response = sendPostRequestLoginCourier(
            "{\"login\": \"kaktusik\", \"password\": \"12345\"}");
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Check login courier with wrong pass")
    @Description("Проверка попытки залогиниться с неправильным паролем")
    public void checkLoginWithWrongPass() {
        Response response = sendPostRequestLoginCourier(
            "{\"login\": \"kaktus1\", \"password\": \"asdf\"}");
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Check login with non existent user")
    @Description("Проверка попытки залогиниться несуществующим пользователем")
    public void checkLoginWithNonExistentUser() {
        Response response = sendPostRequestLoginCourier(
            "{\"login\": \"kjndded\", \"password\": \"asd515f\"}");
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Check login without login")
    @Description("Проверка попытки залогиниться с пустым логином")
    public void checkLoginWithoutLogin() {
        Response response = sendPostRequestLoginCourier(
            "{\"login\": \"\", \"password\": \"12345\"}");
        checkStatusCodeAndBody(response, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Check login without pass")
    @Description("Проверка попытки залогиниться с пустым паролем")
    public void checkLoginWithoutPass() {
        Response response = sendPostRequestLoginCourier(
            "{\"login\": \"kaktus1\", \"password\": \"\"}");
        checkStatusCodeAndBody(response, 400, "Недостаточно данных для входа");
    }

}

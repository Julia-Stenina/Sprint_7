import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.CourierClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void deleteCourier() {
        String json = "{\"login\": \"kaktus\", \"password\": \"12345\"}";
        CourierClient courierClient = new CourierClient();
        courierClient.deleteCourier(json);
    }

    @Step("Отправка запроса")
    public Response sendRequestCreateCourier(String json) {
        CourierClient createCourier = new CourierClient();
        Response response = createCourier.createCourier(json);
        return response;
    }

    @Step("Проверка кода ошибки и сообщения")
    public void checkStatusCodeAndMessage(Response response, int statusCode, String message) {
        response.then().assertThat().statusCode(statusCode).and()
            .body("message", equalTo(message));
    }

    @Test
    @DisplayName("Check create courier with correct status code")
    @Description("Проверка статус-кода при корректном создании пользователя")
    public void checkCreateCourierWithCorrectStatusCode() {
        Response response = sendRequestCreateCourier(
            "{\"login\": \"kaktus\", \"password\": \"12345\", \"firstName\": \"kaktus\"}");
        response.then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("Check create courier with correct status response")
    @Description("Проверка тела ответа при корректном создании пользователя")
    public void checkCreateCourierWithCorrectResponse() {
        Response response = sendRequestCreateCourier(
            "{\"login\": \"kaktus\", \"password\": \"12345\", \"firstName\": \"kaktus\"}");
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check create two identical couriers")
    @Description("Проверка создания двух абсолютно идентичных курьеров")
    public void checkCreateTwoIdenticalCouriers() {
        sendRequestCreateCourier(
            "{\"login\": \"kaktus\", \"password\": \"12345\", \"firstName\": \"kaktus\"}");
        Response response = sendRequestCreateCourier(
            "{\"login\": \"kaktus\", \"password\": \"12345\", \"firstName\": \"kaktus\"}");
        checkStatusCodeAndMessage(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Check create couriers with identical login")
    @Description("Проверка создания двух курьеров с одинаковым логином")
    public void checkCreateCouriersWithIdenticalLogin() {
        sendRequestCreateCourier("{\"login\": \"kaktus\", \"password\": \"12345\", \"firstName\": \"kaktus\"}");
        Response response = sendRequestCreateCourier("{\"login\": \"kaktus\", \"password\": \"54321\"}");
        checkStatusCodeAndMessage(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Check create courier without login")
    @Description("Проверка создания курьера без логина")
    public void checkCreateCourierWithoutLogin() {
        Response response = sendRequestCreateCourier("{\"password\": \"12345\", \"firstName\": \"kaktus\"}");
        checkStatusCodeAndMessage(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check create courier without pass")
    @Description("Проверка создания курьера без пароля")
    public void checkCreateCourierWithoutPass() {
        Response response = sendRequestCreateCourier("{\"login\": \"kaktus\", \"firstName\": \"kaktus\"}");
        checkStatusCodeAndMessage(response, 400, "Недостаточно данных для создания учетной записи");
    }

}

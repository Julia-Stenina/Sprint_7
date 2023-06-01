import static io.restassured.RestAssured.given;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class ListOfOrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Step("Отправка запроса")
    public Response sendGetListOfOrdersRequest() {
        Response response =
            given()
                .get("/api/v1/orders");
        return response;
    }

    @Step("Проверка, что вернулся не пустой список заказов")
    public void checkListOfOrder(Response response) {
        response.then().assertThat().body("$", Matchers.hasKey("orders")).body("orders",
            Matchers.hasSize(Matchers.greaterThan(0)));
    }

    @Test
    @DisplayName("Get list of orders")
    @Description("Проверка получения списка заказов")
    public void getListOfOrders() {
        Response response = sendGetListOfOrdersRequest();
        checkListOfOrder(response);

    }
}

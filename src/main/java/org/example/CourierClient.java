package org.example;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

public class CourierClient {

    public Response createCourier(String json) {
        return given().header("Content-type", "application/json").and().body(json).when()
            .post("/api/v1/courier");
    }

    public void deleteCourier(String json) {
        Response response = given().header("Content-type", "application/json").and().body(json)
            .when().post("/api/v1/courier/login");
        int code = response.statusCode();
        if (code == 200) {
            int courierId = response.jsonPath().getInt("id");

            given().header("Content-type", "application/json").and()
                .delete("/api/v1/courier/" + courierId);
        }
    }

    public Response logInCourier(String json) {
        return given().header("Content-type", "application/json").and().body(json).when()
            .post("/api/v1/courier/login");
    }

}

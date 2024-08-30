package org.ibstraining;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestAPI {



    @ParameterizedTest
    @CsvSource(value = {
            "Кабачок, VEGETABLE, false",
            "Свекла, VEGETABLE, true",
            "Банан, FRUIT, false",
            "Гро-мишель, FRUIT, true"
    })
    public void test(String fruitName, String type, Boolean isExotic) {
        String sessionId;
        Response response = given()
                .baseUri("http://localhost:8080")
                .when()
                .get("/api/food");

        sessionId = response.getCookie("JSESSIONID");

        given()
                .baseUri("http://localhost:8080")
                .sessionId(sessionId)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "   \"name\":  \"" + fruitName + "\",\n" +
                        "   \"type\":   \"" + type + "\",\n" +
                        "   \"exotic\": "+  isExotic + " \n" +
                        "}")
                .basePath("/api/food")
                .when()
                .log().all()
                .post()
                .then()
                .log().all();


        given()
                .baseUri("http://localhost:8080")
                .sessionId(sessionId)
                .log().all()
                .when()
                .get("/api/food")
                .then()
                .log().body();

    }

}

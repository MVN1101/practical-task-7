package org.ibstraining;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestAPI {

    @Test
    public void test() {
        Response response = given()
                .baseUri("http://localhost:8080")
                .when()
                .get("/api/food");

        String cookie = response.getCookie("JSESSIONID");
        System.out.println("\n\n\n" + cookie + "\n\n\n");


        given()
                .baseUri("http://localhost:8080")
                .sessionId(cookie)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "   \"name\": \"Кабачок\",\n" +
                        "   \"type\": \"FRUIT\",\n" +
                        "   \"exotic\": true \n" +
                        "}")
                .basePath("/api/food")
                .when()
//                .log().all()
                .post()
                .then()
                .log().all();

        given()
                .baseUri("http://localhost:8080")
                .sessionId(cookie)
                .log().all()
                .when()
                .get("/api/food")
                .then()
                .log().body();

    }

}

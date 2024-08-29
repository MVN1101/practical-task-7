package org.ibstraining;

import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.authentication;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestAPI {

    @Test
    public void test() {


        given()
                .baseUri("http://localhost:8080")
                .when()
                .log().all()
                .get("/food")
                .then()
                .body("html.body.div.div[1].div.table.tbody.tr[3].td[0]", equalTo("Яблоко"));


    }
}

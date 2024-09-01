package org.ibstraining;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

/**
 * @author Vikor_Mikhaylov
 * Класс для прогонки параметризированного теста корректности добавления товара с помощью API
 */
public class TestAPI {

    private String sessionId;

    @ParameterizedTest
    @CsvSource(value = {
            "Кабачок, VEGETABLE, false",
            "Свекла, VEGETABLE, true",
            "Банан, FRUIT, false",
            "Гро-мишель, FRUIT, true"
    })
    public void addProductTest(String fruitName, String type, Boolean isExotic) {

//        получение списка товаров
        Response response = given()
                .baseUri("http://localhost:8080")
                .when()
                .get("/api/food");

        Assertions.assertEquals(200, response.getStatusCode(), "список товаров не получен");

//        проверка того, что список товаров не пустой
        response.then()
                .assertThat()
                .body("", not(empty()));

//        получение cookie по имени для последующей работы в рамках одной сессии
        sessionId = response.getCookie("JSESSIONID");


//        добавление товара
        given()
                .baseUri("http://localhost:8080")
                .sessionId(sessionId)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "   \"name\":  \"" + fruitName + "\",\n" +
                        "   \"type\":   \"" + type + "\",\n" +
                        "   \"exotic\": " + isExotic + " \n" +
                        "}")
                .basePath("/api/food")
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(200);

//        запрос списка после добавления товара
        response = given()
                .baseUri("http://localhost:8080")
                .sessionId(sessionId)
                .when()
                .get("/api/food");

//        проверка наличия добавленного товара в списке
        Assertions.assertEquals(fruitName, response.jsonPath().getString("name[4]"));
        Assertions.assertEquals(type, response.jsonPath().getString("type[4]"));
        Assertions.assertEquals(isExotic, response.jsonPath().getBoolean("exotic[4]"));

//                .then()
//                .assertThat()
//                .log().all()
//                .assertThat()
//                .extract()
//
//
//
//                .jsonPath().getList("name")
//                ;
////                .jsonPath().getList("name[1]", ProductPojo.class);
//
//        System.out.println(list);


//
//
//
//

    }

}

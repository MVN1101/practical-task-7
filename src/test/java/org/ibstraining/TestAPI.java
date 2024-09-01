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

    @ParameterizedTest
    @CsvSource(value = {
            "Кабачок, VEGETABLE, false",
            "Свекла, VEGETABLE, true",
            "Банан, FRUIT, false",
            "Гро-мишель, FRUIT, true"
    })
    public void addProductTest(String fruitName, String type, Boolean isExotic) {

        String sessionId;

        Specifications.installSpecification(Specifications.requestSpecification(
                "http://localhost:8080", "/api/food", ContentType.JSON),
                Specifications.responseSpecification(200));

//        получение списка товаров
        Response response = given()
                .when()
                .get();

//        проверка того, что список товаров не пустой
        response.then()
                .assertThat()
                .body("", not(empty()));

//        получение cookie по имени для последующей работы в рамках одной сессии
        sessionId = response.getCookie("JSESSIONID");

//        добавление товара
        given()
                .sessionId(sessionId)
                .body("{\n" +
                        "   \"name\":  \"" + fruitName + "\",\n" +
                        "   \"type\":   \"" + type + "\",\n" +
                        "   \"exotic\": " + isExotic + " \n" +
                        "}")
                .when()
                .post()
                .then()
                .assertThat();

//        запрос списка после добавления товара
        response = given()
                .sessionId(sessionId)
                .when()
                .get();

//        проверка наличия добавленного товара в списке
        Assertions.assertEquals(fruitName, response.jsonPath().getString("name[4]"), "товар не добавлен");
        Assertions.assertEquals(type, response.jsonPath().getString("type[4]"), "товар не добавлен");
        Assertions.assertEquals(isExotic, response.jsonPath().getBoolean("exotic[4]"), "товар не добавлен");

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

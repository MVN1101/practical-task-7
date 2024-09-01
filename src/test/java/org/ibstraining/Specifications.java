package org.ibstraining;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * @author Vikor_Mikhaylov
 * Класс спецификаций
 */
public class Specifications {

    public static RequestSpecification requestSpecification(String url, String basePath, ContentType contentType) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setBasePath(basePath)
                .setContentType(contentType)
                .build();
    }

    public static ResponseSpecification responseSpecification(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .build();
    }

    public static void installSpecification(RequestSpecification requestSpecification,
                                            ResponseSpecification responseSpecification) {
        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }
}

package com.k8swatcher;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ApplicationTest {

    @Test
    public void testHelloEndpoint() {
        given().when().get("/k8swatcher").then().statusCode(200).body(is("success"));
    }

}
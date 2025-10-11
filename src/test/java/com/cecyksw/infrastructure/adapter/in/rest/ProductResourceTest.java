package com.cecyksw.infrastructure.adapter.in.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Integration test for ProductResource.
 * Tests the REST API endpoints.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductResourceTest {

    @Test
    @Order(1)
    void testGetAllProducts() {
        given()
            .when().get("/api/products")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    @Order(2)
    void testGetProductById() {
        given()
            .when().get("/api/products/1")
            .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Laptop"));
    }

    @Test
    @Order(3)
    void testGetProductByIdNotFound() {
        given()
            .when().get("/api/products/999")
            .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    void testCreateProduct() {
        String requestBody = """
            {
                "name": "Test Product",
                "description": "Test Description",
                "price": 99.99,
                "stock": 20
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/api/products")
            .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is("Test Product"))
                .body("price", is(99.99f));
    }

    @Test
    @Order(5)
    void testUpdateProduct() {
        String requestBody = """
            {
                "name": "Updated Laptop",
                "description": "Updated high performance laptop",
                "price": 1300.00,
                "stock": 15
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().put("/api/products/1")
            .then()
                .statusCode(200)
                .body("name", is("Updated Laptop"))
                .body("price", is(1300.0f));
    }

    @Test
    @Order(6)
    void testUpdateProductNotFound() {
        String requestBody = """
            {
                "name": "Updated Product",
                "description": "Updated description",
                "price": 100.00,
                "stock": 10
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().put("/api/products/999")
            .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    void testDeleteProduct() {
        // First create a product to delete
        String requestBody = """
            {
                "name": "Product to Delete",
                "description": "This will be deleted",
                "price": 50.00,
                "stock": 5
            }
            """;

        Integer productId = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when().post("/api/products")
            .then()
                .statusCode(201)
                .extract().path("id");

        // Now delete it
        given()
            .when().delete("/api/products/" + productId)
            .then()
                .statusCode(204);

        // Verify it's deleted
        given()
            .when().get("/api/products/" + productId)
            .then()
                .statusCode(404);
    }

    @Test
    @Order(8)
    void testDeleteProductNotFound() {
        given()
            .when().delete("/api/products/999")
            .then()
                .statusCode(404);
    }
}

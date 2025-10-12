package clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.Step;
import model.Order;
import model.User;
import model.UserCred;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final String API_USER_REGISTER = "/api/auth/register";
    private static final String API_USER_LOGIN = "/api/auth/login";
    private static final String API_USER_PATCH_OR_DELETE ="api/auth/user";
    private static final String API_ORDER_CREATE = "/api/orders";
    private static final String API_ORDERS_GET = "/api/orders";



    public ApiClient() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
    }

    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(API_USER_REGISTER);
    }

    @Step("Логин юзера")
    public Response loginUser(UserCred creds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(creds)
                .when()
                .post(API_USER_LOGIN);
    }

    @Step("Изменение юзера")
    public Response updateUser(String accessToken, User user) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .log().body()
                .body(user)
                .when()
                .patch(API_USER_PATCH_OR_DELETE);
    }


    @Step("Удаление юзера")
    public Response delete(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete(API_USER_PATCH_OR_DELETE);
    }

    @Step("Создание заказа c авторизацией")
    public Response createOrder(String accessToken, Order order) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(API_ORDER_CREATE);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutToken(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(API_ORDER_CREATE);
    }

    @Step("Получение списка заказов по юзеру")
    public Response getOrder(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(API_ORDERS_GET);
    }

}
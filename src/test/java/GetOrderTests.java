import clients.ApiClient;
import clients.ResponsesCatalogue;
import generators.OrderGenerator;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static generators.UserGenerator.randomUser;
import static model.UserCred.returnUserCreds;
import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AllureJunit5.class})
public class GetOrderTests {

    private User user;
    private ApiClient apiClient;
    private String accessToken;


    @BeforeEach
    public void setup() {
        apiClient = new ApiClient();
    }

    //получение списка заказов с авторизацией
    @Test
    public void getOrderWithAuth() {

        user = randomUser();
        apiClient.createUser(user);
        Response response = apiClient.loginUser(returnUserCreds(user));
        accessToken = response.as(UserRegisteredResponse.class).getAccessToken();

        apiClient.createOrder(accessToken, OrderGenerator.orderWithIngredients());
        Response response1 = apiClient.getOrder(accessToken);

        assertEquals(SC_OK, response1.statusCode(), "Response code does not match");
        assertTrue(response1.as(GetOrderResponse.class).getSuccess(), "В теле ответа отсутствует success = true");
    }

    //получение списка заказов без авторизации
    @Test
    public void getOrderWithoutAuth() {

        accessToken = "invalid_token_for_tests";
        Response response1 = apiClient.getOrder(accessToken);

        assertEquals(SC_UNAUTHORIZED, response1.statusCode(), "Response code does not match");
        assertEquals(ResponsesCatalogue.getResponseNotAuthorized(), response1.as(UnsuccessfulResponse.class).getMessage(), "В теле ответа отсутствует message");
    }

    @AfterEach
    void deleteUser(){
        if (accessToken != null) {
            apiClient.delete(accessToken);
        }
    }

}

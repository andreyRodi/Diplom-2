import clients.ApiClient;
import generators.OrderGenerator;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.GetOrderResponse;
import model.Order;
import model.User;
import model.UserRegisteredResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static generators.UserGenerator.*;
import static model.UserCred.returnUserCreds;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AllureJunit5.class})
public class CreateOrderTests {

    private User user;
    private ApiClient apiClient;
    private String accessToken;


    @BeforeEach
    public void setup() {
        apiClient = new ApiClient();
    }

    private static Stream<Arguments> orderDataProvider() {
        return Stream.of(
                Arguments.of(OrderGenerator.orderWithIngredients(), SC_OK, true),
                Arguments.of(OrderGenerator.orderWithWrongIngredients(), SC_INTERNAL_SERVER_ERROR, false),
                Arguments.of(OrderGenerator.orderWithoutIngredients(), SC_BAD_REQUEST, false)
        );
    }


    //создание заказа с авторизацией
    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void createOrder(Order order, int expectedResponseCode, boolean expectedSuccessResult) {

        user = randomUser();
        apiClient.createUser(user);
        Response response = apiClient.loginUser(returnUserCreds(user));
        accessToken = response.as(UserRegisteredResponse.class).getAccessToken();

        Response response1 = apiClient.createOrder(accessToken, order);

        assertEquals(expectedResponseCode, response1.statusCode(), "Response code does not match");
        if (response1.statusCode() != SC_INTERNAL_SERVER_ERROR) {
            Boolean actualSuccess = response1.as(GetOrderResponse.class).getSuccess();
            assertEquals(expectedSuccessResult, actualSuccess, "В теле ответа отсутствует success");
        } else {
            System.out.println("Сервер вернул 500, тело отсутствует — проверка success пропущена");
        }
    }

    //создание заказа без авторизации
    @Test
    public void createOrderWithoutAuth() {
        Order order = OrderGenerator.orderWithIngredients();
        Response response = apiClient.createOrderWithoutToken(order);

        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.as(GetOrderResponse.class).getSuccess(), "В теле ответа отсутствует success = true");

    }

    @AfterEach
    void deleteUser(){
        if (accessToken != null) {
            apiClient.delete(accessToken);
        }
    }

}

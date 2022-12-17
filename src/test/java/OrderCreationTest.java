import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private Order order;
    private OrderClient orderClient;
    private final List<String> color;
    private final List<String> expectedColor;
    private int trackID;

    @Before
    public void setUp() {
        order = Order.getRandomOrder(color);
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        orderClient.cancel(trackID);
    }

    public OrderCreationTest(List<String> color, List<String> expectedColor) {
        this.color = color;
        this.expectedColor = expectedColor;
    }

    @Parameterized.Parameters
    public static Object[][] getColorType() {
        return new Object[][]{
                {List.of("BLACK"), List.of("BLACK")},
                {List.of("GREY"), List.of("GREY")},
                {List.of("BLACK", "GREY"), List.of("BLACK", "GREY")},
                {List.of(), List.of()}
        };
    }

    @Test
    @DisplayName("Check the order can be made with different colors")
    public void checkOrderCanBeCreatedTest() {

        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        trackID = response.extract().path("track");
        ValidatableResponse responseId = orderClient.getOrderInfo(trackID);
        List<Object> actualColor = responseId.extract().jsonPath().getList("order.color");

        assertEquals("Некорректный код статуса", 201, statusCode);
        assertThat("Некорректный ID трека", trackID, notNullValue());
        assertEquals("Некорректное значение цвета", expectedColor, actualColor);
    }

}
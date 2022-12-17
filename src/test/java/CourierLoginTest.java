import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp(){
        courier = Courier.getRandomCourier();
        courierClient = new CourierClient();
        courierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Checking the successful authorization of the courier")
    public void checkCourierLoginTest() {
        ValidatableResponse login = courierClient.loginCourier(CourierCredentials.from(courier));
        int statusCode = login.extract().statusCode();
        courierId = login.extract().path("id");

        assertThat ("Некорректный код статуса", statusCode, equalTo(200));
        assertThat("Неверный ID курьера", courierId, notNullValue());
    }

}
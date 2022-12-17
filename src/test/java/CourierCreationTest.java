import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

public class CourierCreationTest extends RestAssuredClient {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandomCourier();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Check successful courier creation")
    public void checkCourierCreationPassedTest() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean isCourierCreated = response.extract().path("ok");
        ValidatableResponse login = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = login.extract().path("id");

        assertTrue("Курьер не создан", isCourierCreated);
        assertThat("Некорректный код статуса", statusCode, equalTo(201));
        assertThat("Неверный ID курьера", courierId, notNullValue());
    }

    @Test
    @DisplayName("Check it's impossible to create two identical couriers")
    public void checkCannotCreateIdenticalCouriersTest() {
        courierClient.createCourier(courier);
        ValidatableResponse login = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = login.extract().path( "id");
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean isIdenticalCourierNotCreated = response.extract().path("message").equals("Этот логин уже используется");

        //Баг в message:
        //ОР: "Этот логин уже используется"
        //ФР: "Этот логин уже используется. Попробуйте другой."
        assertThat("Некорректный код статуса", statusCode, equalTo(409));
        assertTrue("Создано два одинаковых курьера", isIdenticalCourierNotCreated);
    }

}
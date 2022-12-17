import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidationOfCourierCreationTest {

    private CourierClient courierClient;
    private int courierId;

    private Courier courier;
    private int expectedStatus;
    private String expectedErrorMessage;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    public ValidationOfCourierCreationTest(Courier courier, int expectedStatus, String expectedErrorMessage) {
        this.courier = courier;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {Courier.getCourierWithLoginOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getCourierWithPasswordOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getCourierWithFirstNameOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getCourierWithoutFirstNameOnly(), 201, null}
        };
    }

    @Test
    @DisplayName("Checking field validation for courier creation")
    public void checkFieldsValidationOfCourierCreationTest() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        if (statusCode == 201) {
            courierId = courierClient.loginCourier(new CourierCredentials(courier.login, courier.password)).extract().path("id");
            assertThat("Неверный ID курьера", courierId, notNullValue());
        }

        assertEquals("Некорректный код статуса", expectedStatus, statusCode);
        assertEquals("Некорректное сообщение об ошибке", expectedErrorMessage, errorMessage);
    }

}
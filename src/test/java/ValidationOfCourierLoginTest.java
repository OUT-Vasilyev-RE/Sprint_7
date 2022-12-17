import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidationOfCourierLoginTest {

    private static CourierClient courierClient = new CourierClient();
    private static Courier courier = Courier.getRandomCourier();
    private int courierId;
    private int expectedStatus;
    private String expectedErrorMessage;
    private CourierCredentials courierCredentials;

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    public ValidationOfCourierLoginTest(CourierCredentials courierCredentials, int expectedStatus, String expectedErrorMessage) {
        this.courierCredentials = courierCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {CourierCredentials.getCourierPermissionWithLoginOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.getCourierPermissionWithPasswordOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.getCourierPermissionWithNotValidCredentials(), 404, "Учетная запись не найдена"}
        };
    }

    @Test
    @DisplayName("Checking field validation for login courier")
    public void checkFieldsValidationOfCourierLoginTest() {
        //в проверке с авторизацией только с логином баг - отваливается по таймауту:
        //Expected :400
        //Actual   :504
        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = response.extract().path("id");
        ValidatableResponse errorResponse = new CourierClient().loginCourier(courierCredentials);
        int statusCode = errorResponse.extract().statusCode();
        assertEquals ("Некорректный код статуса", expectedStatus, statusCode);
        String errorMessage = errorResponse.extract().path ("message");
        assertEquals ("Некорректное сообщение об ошибке", expectedErrorMessage, errorMessage);
    }

}

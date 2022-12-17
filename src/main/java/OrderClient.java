import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String GET_ORDER_PATH = "/api/v1/orders/track?t=";

    @Step("Create an order")
    public ValidatableResponse createOrder(Order order){
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Get a list of orders")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Get track id of order")
    public ValidatableResponse getOrderInfo(int trackID) {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(GET_ORDER_PATH + trackID)
                .then();
    }

    @Step("Cancel an order")
    public ValidatableResponse cancel(int trackId) {
        return given()
                .spec(getBaseSpec())
                .body(trackId)
                .when()
                .put(ORDER_PATH + "cancel/")
                .then();
    }

}
import com.github.javafaker.Faker;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Order {

    public static Faker faker = new Faker(new Locale("ru"));
    public String firstName;
    public String lastName;
    public String address;
    public String metroStation;
    public String phone;
    public int rentTime;
    public Date deliveryDate;
    public String comment;
    public List<String> color;

    public Order(String firstName, String lastName, String address, String metroStation, String phone, int rentTime,
                 Date deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public static Order getRandomOrder(List<String> colorKind) {
        final String firstName = faker.name().firstName();
        final String lastName = faker.name().lastName();
        final String address = faker.address().streetAddress();
        final String metroStation = faker.address().streetName();
        final String phone = faker.phoneNumber().phoneNumber();
        final int rentTime = faker.number().numberBetween(1, 5);
        final Date deliveryDate = faker.date().future(10, TimeUnit.DAYS);
        final String comment = faker.rickAndMorty().quote();
        final List<String> color = colorKind;
        return new Order(firstName, lastName, address, metroStation, phone, rentTime,
                deliveryDate, comment, color);
    }

}
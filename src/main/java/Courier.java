import com.github.javafaker.Faker;

public class Courier {

    public String login;
    public String password;
    public String firstName;
    public static Faker faker = new Faker();

    public Courier() {

    }


    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static Courier getRandomCourier() {
        final String login = faker.internet().domainName();
        final String password = faker.internet().password();
        final String firstName = faker.name().firstName();
        return new Courier(login, password, firstName);
    }

    public Courier setLogin(String login) {
        this.login = login;
        return this;
    }

    public Courier setPassword(String password) {
        this.password = password;
        return this;
    }

    public Courier setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public static Courier getCourierWithLoginOnly() {
        return new Courier().setLogin(faker.internet().domainName());
    }

    public static Courier getCourierWithPasswordOnly() {
        return new Courier().setPassword(faker.internet().password());
    }

    public static Courier getCourierWithFirstNameOnly() {
        return new Courier().setFirstName(faker.name().firstName());
    }

    public static Courier getCourierWithoutFirstNameOnly() {
        return new Courier().setLogin(faker.internet().domainName()).setPassword(faker.internet().password());
    }

}
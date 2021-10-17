package helpers;
import java.util.Date;
import java.util.UUID;

public class StringHelpers {
    public static String GenerateRandomEmail() {
        return UUID.randomUUID() + "@example.com";
    }
}

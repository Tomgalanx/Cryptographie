import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {


        for(int i=0;i<100;i++) {
            Schnorr again = new Schnorr();
            again.KeyGen(260, 1800);

            new randomStringGenerator();

            String message = randomStringGenerator.generateString();

            System.out.println("Le message est :"+message);

            again.Sign(message);

            again.Verify(message);
        }
    }
}

class randomStringGenerator {
    public static void main(String[] args) {
        System.out.println(generateString());
    }

    public static String generateString() {
        String uuid = UUID.randomUUID().toString();
        return "uuid = " + uuid;
    }
}

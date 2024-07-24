package CDI;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@ApplicationScoped
public class OneTimePasswordGenerator {

        /**
     * Found in: https://stackoverflow.com/questions/7111651/how-to-generate-a-secure-random-alphanumeric-string-in-java-efficiently
     *
     * @param lenght the length of one_time_password of the one-time password
     * @return a one-time password
     */
    public String generateOneTimePassword(int lenght) throws NoSuchAlgorithmException {

        final String chrs = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        final SecureRandom secureRandom = SecureRandom.getInstanceStrong();

        final String one_time_password = secureRandom
                .ints(lenght, 0, chrs.length()) // 9 is the lengthOfone_time_password of the string you want
                .mapToObj(i -> chrs.charAt(i))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        return one_time_password;

    }
}

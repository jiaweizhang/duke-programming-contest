package utilities;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by jiaweizhang on 10/25/2016.
 */
public class RNGUtility {
    private final static SecureRandom secureRandom = new SecureRandom();

    public static String generateToken() {
        return new BigInteger(130, secureRandom).toString(32);
    }
}

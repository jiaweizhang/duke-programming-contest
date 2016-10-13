package utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Properties;

/**
 * Created by jiaweizhang on 9/16/2016.
 */

public class TokenUtility {
    public static String generateToken(long userId) {
        Properties p = PropertiesLoader.loadPropertiesFromPackage("security.properties");
        String secretKey = p.getProperty("secretKey");

        return Jwts.builder().setSubject(Long.toString(userId))
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public static Claims retrieveClaims(String jwt) {
        Properties p = PropertiesLoader.loadPropertiesFromPackage("security.properties");
        String secretKey = p.getProperty("secretKey");

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }
}
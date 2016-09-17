package utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by jiaweizhang on 9/16/2016.
 */
public class TokenCreator {
    public static String generateToken(long userId, String netId) {
        String jwt = Jwts.builder().setSubject(netId)
                .claim("userId", userId)
                .claim("netId", netId)
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
        return jwt;
    }
}
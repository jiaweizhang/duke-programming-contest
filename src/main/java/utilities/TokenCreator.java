package utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by jiaweizhang on 9/16/2016.
 */
public class TokenCreator {
    public static String generateToken(int userId, String netId) {
        String jwt = Jwts.builder().setSubject(netId)
                .claim("user_id", userId)
                .claim("net_id", netId)
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
        return jwt;
    }
}
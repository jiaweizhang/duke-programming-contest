package dpc.auth;

import dpc.std.StdResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import utilities.TokenCreator;

/**
 * Created by jiaweizhang on 9/16/2016.
 */
@Transactional
@Service
public class OAuthService extends dpc.std.Service {

    public StdResponse auth(String authCode) {
        RestTemplate rt = new RestTemplate();

        OAuthRequest res = null;
        try {
            res = rt.getForObject("https://oauth.oit.duke.edu/oauth/resource.php?access_token=" + authCode, OAuthRequest.class);
        } catch (Exception e) {
            return new StdResponse(200, false, "Failed to authenticate");
        }

        // get stuff from the OAuth object if possible
        if (res.eppn == null) {
            return new StdResponse(200, false, "Failed to authenticate");
        }

        String netIdEmail = res.eppn;

        String netId = netIdEmail.substring(0, netIdEmail.indexOf("@"));

        long userId = createUserIfNotExists(netId);

        String token = TokenCreator.generateToken(userId, netId);
        return new OAuthResponse(200, true, "Successfully authenticated", token, userId, netId);
    }

    public long createUserIfNotExists(String netId) {
        // TODO - refactor into procedure
        if (!netIdExists(netId)) {
            this.jt.update("INSERT INTO users (net_id) VALUES (?)", netId);
        }

        return this.jt.queryForObject("SELECT users.user_id FROM users WHERE net_id = ?", Long.class, netId);
    }

    private boolean netIdExists(String netId) {
        return this.jt.queryForObject("SELECT EXISTS(SELECT 1 from USERS where users.net_id = ?)",
                Boolean.class, netId);
    }
}
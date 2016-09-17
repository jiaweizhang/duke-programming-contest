package dpc.auth;

import dpc.std.StdResponse;

/**
 * Created by jiaweizhang on 9/16/2016.
 */
public class OAuthResponse extends StdResponse{
    public String token;
    public int userId;
    public String netId;

    public OAuthResponse(int status, boolean success, String message, String token, int userId, String netId) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.netId = netId;
    }
}

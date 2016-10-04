package dpc.auth.models;

import dpc.std.models.StdResponse;

/**
 * Created by jiaweizhang on 9/16/2016.
 */
public class OAuthResponse extends StdResponse {
    public String token;
    public long userId;
    public String netId;

    public OAuthResponse(int status, boolean success, String message, String token, long userId, String netId) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.netId = netId;
    }
}

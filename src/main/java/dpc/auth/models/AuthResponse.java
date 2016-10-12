package dpc.auth.models;

import dpc.std.models.StdResponse;

/**
 * Created by jiaweizhang on 9/16/2016.
 */
public class AuthResponse extends StdResponse {
    public String token;

    public AuthResponse(int status, boolean success, String message, String token) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.token = token;
    }
}

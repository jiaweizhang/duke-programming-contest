package dpc.user.models;

import dpc.std.models.StdResponse;

/**
 * Created by jiaweizhang on 10/27/2016.
 */
public class UserResponse extends StdResponse {
    public User user;

    public UserResponse(int status, boolean success, String message, User user) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.user = user;
    }
}

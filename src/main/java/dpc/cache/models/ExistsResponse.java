package dpc.cache.models;

import dpc.std.models.StdResponse;

/**
 * Created by jiaweizhang on 10/24/2016.
 */
public class ExistsResponse extends StdResponse {
    public boolean exists;

    public ExistsResponse(int status, boolean success, String message, boolean exists) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.exists = exists;
    }
}

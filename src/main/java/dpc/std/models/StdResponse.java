package dpc.std.models;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 9/14/2016.
 */
public class StdResponse {
    public int status;
    public boolean success;
    public String message;
    public Timestamp timestamp;

    public StdResponse() {
    }

    public StdResponse(int status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }
}

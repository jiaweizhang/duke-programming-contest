package dpc.submits.models;

import dpc.std.models.StdResponse;

import java.util.List;

/**
 * Created by jiaweizhang on 10/3/2016.
 */
public class SubmissionsResponse extends StdResponse {
    public List<Submission> submissions;

    public SubmissionsResponse(int status, boolean success, String message, List<Submission> submissions) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.submissions = submissions;
    }
}

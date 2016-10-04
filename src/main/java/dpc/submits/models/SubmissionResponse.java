package dpc.submits.models;

import dpc.std.models.StdResponse;

/**
 * Created by jiaweizhang on 10/3/2016.
 */
public class SubmissionResponse extends StdResponse {
    public int isCorrect;

    public SubmissionResponse(int status, boolean success, String message, int isCorrect) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.isCorrect = isCorrect;
    }
}

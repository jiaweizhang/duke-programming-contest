package dpc.contest;

import dpc.std.StdResponse;

import java.util.List;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class ContestsResponse extends StdResponse {
    public List<Contest> contests;

    public ContestsResponse(int status, boolean success, String message, List<Contest> contests) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.contests = contests;
    }
}

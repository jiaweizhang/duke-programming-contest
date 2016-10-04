package dpc.contest.models;

import dpc.std.models.StdResponse;

import java.util.List;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class ContestsResponse extends StdResponse {
    private List<Contest> contests;

    public ContestsResponse(int status, boolean success, String message, List<Contest> contests) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.contests = contests;
    }

    public List<Contest> getContests() {
        return contests;
    }
}

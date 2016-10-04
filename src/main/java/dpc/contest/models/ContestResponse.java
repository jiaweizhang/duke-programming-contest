package dpc.contest.models;

import dpc.std.models.StdResponse;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class ContestResponse extends StdResponse {

    private String contestId;
    private String name;
    private Timestamp startTime;
    private Timestamp endTime;

    public ContestResponse(int status, boolean success, String message, Contest contest) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.contestId = contest.getContestId();
        this.name = contest.getName();
        this.startTime = contest.getStartTime();
        this.endTime = contest.getEndTime();
    }

    public String getContestId() {
        return contestId;
    }

    public String getName() {
        return name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }
}

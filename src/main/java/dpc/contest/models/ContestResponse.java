package dpc.contest.models;

import dpc.std.StdResponse;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class ContestResponse extends StdResponse {

    private String contestId;
    private String name;
    private Timestamp startDate;
    private int duration;

    public ContestResponse(int status, boolean success, String message, Contest contest) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.contestId = contest.getContestId();
        this.name = contest.getName();
        this.startDate = contest.getStartDate();
        this.duration = contest.getDuration();
    }

    public String getContestId() {
        return contestId;
    }

    public String getName() {
        return name;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public int getDuration() {
        return duration;
    }
}

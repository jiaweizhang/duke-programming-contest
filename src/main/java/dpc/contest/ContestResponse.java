package dpc.contest;

import dpc.std.StdResponse;

import java.sql.Date;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class ContestResponse extends StdResponse {

    private String contestId;
    private String name;
    private Date startDate;
    private int duration;

    public ContestResponse (int status, boolean success, String message, Contest contest) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.contestId = contest.getContestId();
        this.name = contest.getName();
        this.startDate = contest.getStartDate();
        this.duration = contest.getDuration();
    }
}

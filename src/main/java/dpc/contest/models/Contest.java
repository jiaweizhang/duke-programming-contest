package dpc.contest.models;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class Contest {
    private String contestId;
    private String name;
    private Timestamp startTime;
    private Timestamp endTime;

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}

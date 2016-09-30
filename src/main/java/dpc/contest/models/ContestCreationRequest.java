package dpc.contest.models;

import dpc.std.StdRequest;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class ContestCreationRequest extends StdRequest {
    public String contestId;
    public String name;
    public Timestamp startDate;
    public int duration;
}

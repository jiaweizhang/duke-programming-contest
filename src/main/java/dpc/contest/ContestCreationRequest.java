package dpc.contest;

import dpc.std.StdRequest;

import java.sql.Date;

/**
 * Created by jiaweizhang on 9/17/2016.
 */
public class ContestCreationRequest extends StdRequest {
    public String contestId;
    public String name;
    public Date startDate;
    public int duration;
}

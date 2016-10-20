package dpc.groups.models;

import dpc.std.models.StdResponse;

import java.util.List;

/**
 * Created by jiaweizhang on 10/20/2016.
 */
public class GroupInfoResponse extends StdResponse {
    public long groupId;
    public String groupName;
    public String secret;
    public List<String> memberNames;

    public GroupInfoResponse(int status, boolean success, String message,
                             long groupId, String groupName, String secret, List<String> memberNames) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.groupId = groupId;
        this.groupName = groupName;
        this.secret = secret;
        this.memberNames = memberNames;
    }
}

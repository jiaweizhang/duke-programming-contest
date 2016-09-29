package dpc.groups;

import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.JoinGroupRequest;
import dpc.std.Service;
import dpc.std.StdRequest;
import dpc.std.StdResponse;

/**
 * Created by jiaweizhang on 9/28/2016.
 */

@org.springframework.stereotype.Service
class GroupService extends Service {
    StdResponse createGroup(CreateGroupRequest request, String contestId) {
        return null;
    }

    StdResponse joinGroup(JoinGroupRequest request, String contestId) {
        return null;
    }

    StdResponse leaveGroup(StdRequest request, String contestId) {
        return null;
    }
}

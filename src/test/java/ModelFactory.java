import dpc.auth.models.LoginRequest;
import dpc.auth.models.RegisterRequest;
import dpc.contest.models.ContestCreationRequest;
import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.JoinGroupRequest;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 10/12/2016.
 */
class ModelFactory {
    static RegisterRequest registerRequest(String email, String name, String password, String school, String classInSchool) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.email = email;
        registerRequest.name = name;
        registerRequest.password = password;
        registerRequest.school = school;
        registerRequest.classInSchool = classInSchool;
        return registerRequest;
    }

    static LoginRequest loginRequest(String email, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.email = email;
        loginRequest.password = password;
        return loginRequest;
    }

    static ContestCreationRequest contestCreationRequest(String contestId, String contestName, Timestamp startTime, Timestamp endTime, long userId) {
        ContestCreationRequest contestCreationRequest = new ContestCreationRequest();
        contestCreationRequest.contestId = contestId;
        contestCreationRequest.contestName = contestName;
        contestCreationRequest.startTime = startTime;
        contestCreationRequest.endTime = endTime;
        contestCreationRequest.userId = userId;
        return contestCreationRequest;
    }

    static CreateGroupRequest createGroupRequest(String groupName, long userId) {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.groupName = groupName;
        createGroupRequest.userId = userId;
        return createGroupRequest;
    }

    static JoinGroupRequest joinGroupRequest(String groupName, long userId) {
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.groupName = groupName;
        joinGroupRequest.userId = userId;
        return joinGroupRequest;
    }
}

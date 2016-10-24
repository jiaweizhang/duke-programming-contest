import dpc.auth.models.LoginRequest;
import dpc.auth.models.RegisterRequest;
import dpc.cache.models.EmailRequest;
import dpc.cache.models.GroupNameRequest;
import dpc.contest.models.ContestCreationRequest;
import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.JoinGroupRequest;
import dpc.std.models.StdRequest;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 10/12/2016.
 */
class ModelFactory {
    static StdRequest stdRequest(long userId) {
        StdRequest stdRequest = new StdRequest();
        stdRequest.userId = userId;
        return stdRequest;
    }

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

    static JoinGroupRequest joinGroupRequest(String secret, long userId) {
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.secret = secret;
        joinGroupRequest.userId = userId;
        return joinGroupRequest;
    }

    static EmailRequest emailRequest(String email) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.email = email;
        return emailRequest;
    }

    static GroupNameRequest groupNameRequest(String groupName) {
        GroupNameRequest groupNameRequest = new GroupNameRequest();
        groupNameRequest.groupName = groupName;
        return groupNameRequest;
    }
}

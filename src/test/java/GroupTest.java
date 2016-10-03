import dpc.Application;
import dpc.contest.ContestService;
import dpc.contest.models.ContestCreationRequest;
import dpc.exceptions.GroupFullException;
import dpc.groups.GroupService;
import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.JoinGroupRequest;
import dpc.std.StdResponse;
import dpc.test.TestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;

import static org.junit.Assert.fail;

/**
 * Created by jiaweizhang on 9/30/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class GroupTest {

    private final String netId1 = "jz134";
    private final String netId2 = "ajg51";
    private final String netId3 = "azd2";
    private final String netId4 = "savage";
    private final String CONTEST_ID = "dpc";
    @Autowired
    private TestService testService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private GroupService groupService;
    private long userId1;
    private long userId2;
    private long userId3;
    private long userId4;

    @Before
    public void before() {
        testService.updateDb();
        userId1 = testService.createUser(netId1);
        userId2 = testService.createUser(netId2);
        userId3 = testService.createUser(netId3);
        userId4 = testService.createUser(netId4);
        ContestCreationRequest contestCreationRequest = new ContestCreationRequest();
        contestCreationRequest.userId = userId1;
        contestCreationRequest.contestId = "dpc";
        contestCreationRequest.name = "Duke Programming Contest";
        contestCreationRequest.startDate = new Timestamp(1400000000);
        contestService.createContest(contestCreationRequest);
    }

    @Test
    public void createGroup() {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.userId = userId1;
        createGroupRequest.netId = netId1;
        createGroupRequest.name = "Valid group name";
        createGroupRequest.secret = "good secret";
        StdResponse stdResponse = groupService.createGroup(createGroupRequest, CONTEST_ID);
        assert (stdResponse.success);
    }

    @Test
    public void joinGroupWithoutSecret() {
        joinGroupWithoutSecretWhenThereExistNoGroups();
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.userId = userId2;
        joinGroupRequest.netId = netId2;
        joinGroupRequest.secret = "";
        StdResponse stdResponse = groupService.joinGroup(joinGroupRequest, CONTEST_ID);
        assert (stdResponse.success);
    }

    @Test
    public void joinGroupWithoutSecretWhenThereExistNoGroups() {
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.userId = userId1;
        joinGroupRequest.netId = netId1;
        joinGroupRequest.secret = "";
        StdResponse stdResponse = groupService.joinGroup(joinGroupRequest, CONTEST_ID);
        assert (stdResponse.success);
    }

    @Test
    public void joinGroupWithSecret() {
        createGroup();
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.userId = userId2;
        joinGroupRequest.netId = netId2;
        joinGroupRequest.secret = "good secret";
        StdResponse stdResponse = groupService.joinGroup(joinGroupRequest, CONTEST_ID);
        assert (stdResponse.success);

        JoinGroupRequest joinGroupRequest1 = new JoinGroupRequest();
        joinGroupRequest1.userId = userId3;
        joinGroupRequest1.netId = netId3;
        joinGroupRequest1.secret = "good secret";
        StdResponse stdResponse1 = groupService.joinGroup(joinGroupRequest1, CONTEST_ID);
        assert (stdResponse1.success);

        JoinGroupRequest joinGroupRequest2 = new JoinGroupRequest();
        joinGroupRequest2.userId = userId4;
        joinGroupRequest2.netId = netId4;
        joinGroupRequest2.secret = "good secret";
        try {
            groupService.joinGroup(joinGroupRequest2, CONTEST_ID);
            fail("should have failed");
        } catch (GroupFullException e) {

        }

    }
}

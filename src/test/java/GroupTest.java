import dpc.Application;
import dpc.admin.AdminService;
import dpc.contest.ContestService;
import dpc.contest.models.ContestCreationRequest;
import dpc.exceptions.GroupFullException;
import dpc.groups.GroupService;
import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.JoinGroupRequest;
import dpc.std.models.StdResponse;
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
    private AdminService adminService;
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
        adminService.upgradeDb();
        userId1 = adminService.createUser(netId1);
        userId2 = adminService.createUser(netId2);
        userId3 = adminService.createUser(netId3);
        userId4 = adminService.createUser(netId4);
        ContestCreationRequest contestCreationRequest = new ContestCreationRequest();
        contestCreationRequest.userId = userId1;
        contestCreationRequest.contestId = "dpc";
        contestCreationRequest.name = "Duke Programming Contest";
        contestCreationRequest.startTime = new Timestamp(1400000000);
        contestCreationRequest.endTime = new Timestamp(1500000000);
        contestService.createContest(contestCreationRequest);
    }

    @Test
    public void createGroup() {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.userId = userId1;
        createGroupRequest.netId = netId1;
        createGroupRequest.groupName = "jumping_zebras";
        StdResponse stdResponse = groupService.createGroup(createGroupRequest, CONTEST_ID);
        assert (stdResponse.success);
    }

    @Test
    public void joinGroupWithoutName() {
        joinGroupWithoutSecretWhenThereExistNoGroups();
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.userId = userId2;
        joinGroupRequest.netId = netId2;
        joinGroupRequest.groupName = null;
        StdResponse stdResponse = groupService.joinGroup(joinGroupRequest, CONTEST_ID);
        assert (stdResponse.success);
    }

    @Test
    public void joinGroupWithoutSecretWhenThereExistNoGroups() {
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.userId = userId1;
        joinGroupRequest.netId = netId1;
        joinGroupRequest.groupName = "";
        StdResponse stdResponse = groupService.joinGroup(joinGroupRequest, CONTEST_ID);
        assert (stdResponse.success);
    }

    @Test
    public void joinGroupWithSecret() {
        createGroup();
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.userId = userId2;
        joinGroupRequest.netId = netId2;
        joinGroupRequest.groupName = "jumping_zebras";
        StdResponse stdResponse = groupService.joinGroup(joinGroupRequest, CONTEST_ID);
        assert (stdResponse.success);

        JoinGroupRequest joinGroupRequest1 = new JoinGroupRequest();
        joinGroupRequest1.userId = userId3;
        joinGroupRequest1.netId = netId3;
        joinGroupRequest1.groupName = "jumping_zebras";
        StdResponse stdResponse1 = groupService.joinGroup(joinGroupRequest1, CONTEST_ID);
        assert (stdResponse1.success);

        JoinGroupRequest joinGroupRequest2 = new JoinGroupRequest();
        joinGroupRequest2.userId = userId4;
        joinGroupRequest2.netId = netId4;
        joinGroupRequest2.groupName = "jumping_zebras";
        try {
            groupService.joinGroup(joinGroupRequest2, CONTEST_ID);
            fail("should have failed");
        } catch (GroupFullException e) {

        }

    }
}

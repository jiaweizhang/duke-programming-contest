import dpc.Application;
import dpc.admin.AdminService;
import dpc.auth.AuthService;
import dpc.auth.models.RegisterRequest;
import dpc.contest.ContestService;
import dpc.contest.models.ContestCreationRequest;
import dpc.exceptions.GroupFullException;
import dpc.groups.GroupService;
import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.GroupInfoResponse;
import dpc.groups.models.JoinGroupRequest;
import dpc.std.models.StdRequest;
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
    private final String CONTEST_ID = "dpc";
    private final String[] USER_EMAILS = {"one@gmail.com", "two@gmail.com", "three@gmail.com", "four@gmail.com"};
    private final String[] USER_NAMES = {"user1", "user2", "user3", "user4"};
    @Autowired
    private AdminService adminService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AuthService authService;

    @Before
    public void before() {
        adminService.upgradeDb();

        // create contest with contestId "dpc"
        ContestCreationRequest contestCreationRequest = ModelFactory.contestCreationRequest(
                CONTEST_ID,
                "Duke Programming Contest",
                new Timestamp(1400000000),
                new Timestamp(1500000000),
                1
        );
        assert (contestService.createContest(contestCreationRequest).success);

        // create 4 users
        for (int i = 0; i < 4; i++) {
            RegisterRequest registerRequest = ModelFactory.registerRequest(
                    USER_EMAILS[i],
                    USER_NAMES[i],
                    "password",
                    "Duke",
                    "2017"
            );

            assert (authService.register(registerRequest).success);
        }
    }

    @Test
    public void createGroup() {
        CreateGroupRequest createGroupRequest = ModelFactory.createGroupRequest(
                "jumping zebras",
                1
        );
        assert (groupService.createGroup(createGroupRequest, CONTEST_ID).success);
    }

    @Test
    public void groupInfo() {
        createGroup();

        StdRequest stdRequest = ModelFactory.stdRequest(1);

        assert (groupService.infoGroup(stdRequest, CONTEST_ID).success);
    }

    @Test
    public void joinGroupWithoutNameWhenThereExistNoGroupsEmpty() {
        JoinGroupRequest joinGroupRequest = ModelFactory.joinGroupRequest(
                "",
                1
        );
        assert (groupService.joinGroup(joinGroupRequest, CONTEST_ID).success);
    }

    @Test
    public void joinGroupWithoutNameWhenThereExistNoGroupsNull() {
        JoinGroupRequest joinGroupRequest = ModelFactory.joinGroupRequest(
                null,
                1
        );
        assert (groupService.joinGroup(joinGroupRequest, CONTEST_ID).success);
    }

    @Test
    public void joinGroupWithoutName() {
        joinGroupWithoutNameWhenThereExistNoGroupsNull();

        JoinGroupRequest joinGroupRequest = ModelFactory.joinGroupRequest(
                null,
                2
        );

        assert (groupService.joinGroup(joinGroupRequest, CONTEST_ID).success);
    }

    @Test
    public void joinGroupWithSecret() {
        createGroup();

        StdRequest stdRequest = ModelFactory.stdRequest(1);

        String secret = ((GroupInfoResponse) groupService.infoGroup(stdRequest, CONTEST_ID)).secret;

        for (int i = 2; i <= 3; i++) {
            JoinGroupRequest joinGroupRequest = ModelFactory.joinGroupRequest(
                    secret, i
            );
            assert (groupService.joinGroup(joinGroupRequest, CONTEST_ID).success);
        }

        JoinGroupRequest joinGroupRequest2 = ModelFactory.joinGroupRequest(
                secret, 4
        );
        try {
            groupService.joinGroup(joinGroupRequest2, CONTEST_ID);
            fail("should have failed");
        } catch (GroupFullException e) {

        } catch (Exception e) {
            fail("should have failed");
        }

    }
}

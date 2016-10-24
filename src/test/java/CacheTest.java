import dpc.Application;
import dpc.admin.AdminService;
import dpc.auth.AuthService;
import dpc.auth.models.RegisterRequest;
import dpc.cache.CacheService;
import dpc.cache.models.EmailRequest;
import dpc.cache.models.ExistsResponse;
import dpc.cache.models.GroupNameRequest;
import dpc.contest.ContestService;
import dpc.contest.models.ContestCreationRequest;
import dpc.groups.GroupService;
import dpc.groups.models.CreateGroupRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 10/24/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class CacheTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthService authService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private GroupService groupService;

    @Before
    public void before() {
        // refresh the database
        adminService.upgradeDb();
    }

    private void registerUser() {
        RegisterRequest registerRequest = ModelFactory.registerRequest(
                "abc@gmail.com", "ABC", "password", "Duke", "2017"
        );
        assert (authService.register(registerRequest).success);
    }

    @Test
    public void emailTestExists() {
        registerUser();
        EmailRequest emailRequest = ModelFactory.emailRequest("abc@gmail.com");
        assert ((ExistsResponse) cacheService.checkEmail(emailRequest)).exists;
    }

    @Test
    public void emailTestDoesNotExist() {
        EmailRequest emailRequest = ModelFactory.emailRequest("abc@gmail.com");
        assert !((ExistsResponse) cacheService.checkEmail(emailRequest)).exists;
    }

    @Test
    public void groupNameTestExists() {
        ContestCreationRequest contestCreationRequest = ModelFactory.contestCreationRequest(
                "dpc",
                "Duke Programming Contest",
                new Timestamp(1400000000),
                new Timestamp(1500000000),
                1
        );
        assert (contestService.createContest(contestCreationRequest).success);

        RegisterRequest registerRequest = ModelFactory.registerRequest(
                "jz134@duke.edu",
                "jiawei",
                "password",
                "Duke",
                "2017"
        );
        assert (authService.register(registerRequest).success);

        CreateGroupRequest createGroupRequest = ModelFactory.createGroupRequest(
                "jumping zebras",
                1
        );
        assert (groupService.createGroup(createGroupRequest, "dpc").success);

        GroupNameRequest groupNameRequest = ModelFactory.groupNameRequest("jumping zebras");
        assert ((ExistsResponse) cacheService.checkGroupName(groupNameRequest)).exists;
    }

    @Test
    public void groupNameTestNotNotExist() {
        ContestCreationRequest contestCreationRequest = ModelFactory.contestCreationRequest(
                "dpc",
                "Duke Programming Contest",
                new Timestamp(1400000000),
                new Timestamp(1500000000),
                1
        );
        assert (contestService.createContest(contestCreationRequest).success);

        GroupNameRequest groupNameRequest = ModelFactory.groupNameRequest("jumping zebras");
        assert !((ExistsResponse) cacheService.checkGroupName(groupNameRequest)).exists;
    }
}

import dpc.Application;
import dpc.admin.AdminService;
import dpc.contest.ContestService;
import dpc.contest.models.ContestCreationRequest;
import dpc.contest.models.ContestResponse;
import dpc.contest.models.ContestsResponse;
import dpc.std.models.StdResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by jiaweizhang on 9/17/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ContestTest {

    @Autowired
    private ContestService contestService;

    @Autowired
    private AdminService adminService;

    @Before
    public void before() {
        adminService.upgradeDb();
    }

    @Test
    public void TestContestCreation() {
        String contestId = "creationTest";
        assert (createContest(contestId));

        assert (!createContest(contestId));
    }

    @Test
    public void TestGetSingleContest() {
        String contestId = "Single Contest";

        assert (createContest(contestId));

        ContestResponse contestResponse = (ContestResponse) contestService.getContest(contestId);
        assert (contestResponse.getContestId().equals(contestId));
    }

    @Test
    public void TestGetAllContests() {
        createContest("First contest");
        createContest("Second contest");

        ContestsResponse contestsResponse = (ContestsResponse) contestService.getContests();
        assert (contestsResponse.getContests().size() == 2);
    }

    private boolean createContest(String contestId) {
        ContestCreationRequest contestCreationRequest = ModelFactory.contestCreationRequest(
                contestId,
                "Contest name test",
                new Timestamp(Calendar.getInstance().getTime().getTime()),
                new Timestamp(Calendar.getInstance().getTime().getTime()),
                1
        );

        StdResponse stdResponse = contestService.createContest(contestCreationRequest);
        return stdResponse.success;
    }
}

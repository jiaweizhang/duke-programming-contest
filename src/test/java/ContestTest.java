import dpc.Application;
import dpc.contest.models.ContestCreationRequest;
import dpc.contest.models.ContestResponse;
import dpc.contest.ContestService;
import dpc.contest.models.ContestsResponse;
import dpc.std.StdResponse;
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

    @Test
    public void TestContestCreation() {
        String uuid = "creationTest_" + TestUtilities.generateUUID();
        boolean success = createContest(uuid);
        assert (success);

        boolean success2 = createContest(uuid);
        assert (!success2);
    }

    @Test
    public void TestGetSingleContest() {
        String uuid = "getSingleContest_" + TestUtilities.generateUUID();
        createContest(uuid);

        ContestResponse contestResponse = (ContestResponse) contestService.getContest(uuid);
        assert(contestResponse.getContestId().equals(uuid));
    }

    @Test
    public void TestGetAllContests() {
        createContest(TestUtilities.generateUUID());
        createContest(TestUtilities.generateUUID());

        ContestsResponse contestsResponse = (ContestsResponse) contestService.getContests();
        assert(contestsResponse.getContests().size() >= 2);
    }

    private boolean createContest(String contestId) {
        ContestCreationRequest contestCreationRequest = new ContestCreationRequest();
        contestCreationRequest.contestId = contestId;
        contestCreationRequest.name = "Contest name test";
        contestCreationRequest.startDate = new Timestamp(Calendar.getInstance().getTime().getTime());
        contestCreationRequest.duration = 120;
        contestCreationRequest.netId = "jz134";
        contestCreationRequest.userId = 1;
        StdResponse stdResponse = contestService.createContest(contestCreationRequest);
        return stdResponse.success;
    }
}

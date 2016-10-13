package dpc.contest;

import dpc.checks.CheckService;
import dpc.contest.models.Contest;
import dpc.contest.models.ContestCreationRequest;
import dpc.contest.models.ContestResponse;
import dpc.contest.models.ContestsResponse;
import dpc.std.Service;
import dpc.std.models.StdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jiaweizhang on 9/17/2016.
 */


@Transactional
@org.springframework.stereotype.Service
public class ContestService extends Service {

    @Autowired
    private CheckService checkService;

    public StdResponse getContests() {
        List<Contest> contests = this.jt.query("SELECT contest_id, contest_name, start_time, end_time FROM contests",
                new ContestMapper());
        return new ContestsResponse(200, true, "Successfully retrieved contests", contests);
    }

    public StdResponse getContest(String contestId) {
        try {
            Contest contest = this.jt.queryForObject("SELECT contest_id, contest_name, start_time, end_time FROM contests WHERE contest_id = ?",
                    new Object[]{contestId},
                    new ContestMapper());
            return new ContestResponse(200, true, "Successfully retrieve contest", contest);
        } catch (EmptyResultDataAccessException e) {
            return new StdResponse(200, false, "Contest does not exist");
        }
    }


    public StdResponse createContest(ContestCreationRequest req) {
        // check that contestId isn't taken
        if (checkService.contestExists(req.contestId)) {
            return new StdResponse(200, false, "Contest id is taken already");
        }

        this.jt.update("INSERT INTO contests (contest_id, contest_name, start_time, end_time) VALUES (?, ?, ?, ?)",
                req.contestId, req.contestName, req.startTime, req.endTime);

        return new StdResponse(200, true, "Contest created successfully");
    }

    private static final class ContestMapper implements RowMapper<Contest> {
        public Contest mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contest contest = new Contest();
            contest.setContestId(rs.getString("contest_id"));
            contest.setContestName(rs.getString("contest_name"));
            contest.setStartTime(rs.getTimestamp("start_time"));
            contest.setEndTime(rs.getTimestamp("end_time"));
            return contest;
        }
    }
}
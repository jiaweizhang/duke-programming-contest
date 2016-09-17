package dpc.contest;

import dpc.std.Service;
import dpc.std.StdRequest;
import dpc.std.StdResponse;
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

    public StdResponse getContests() {
        List<Contest> contests = this.jt.query("SELECT contest_id, name, start_date, duration FROM contests",
                new ContestMapper());
        return new ContestsResponse(200, true, "Successfully retrieved contests", contests);
    }

    public StdResponse getContest(String contestId) {
        try {
            Contest contest = this.jt.queryForObject("SELECT contest_id, name, start_date, duration FROM contests WHERE contest_id = ?",
                    new Object[]{contestId},
                    new ContestMapper());
            return new ContestResponse(200, true, "Successfully retrieve contest", contest);
        } catch (EmptyResultDataAccessException e) {
            return new StdResponse(200, false, "Contest does not exist");
        }
    }


    public StdResponse createContest(ContestCreationRequest req) {
        // TODO
        // check that contestId isn't taken
        if (contestExists(req.contestId)) {
            return new StdResponse(200, false, "Contest id is taken already");
        }

        this.jt.update("INSERT INTO contests (contest_id, name, start_date, duration) VALUES (?, ?, ?, ?)",
                req.contestId, req.name, req.startDate, req.duration);

        return new StdResponse(200, true, "Contest created successfully");
    }

    public StdResponse joinContest(StdRequest req) {
        // TODO
        return null;
    }

    private boolean contestExists(String contestId) {
        return this.jt.queryForObject("SELECT EXISTS(SELECT 1 FROM USERS WHERE contests.contest_id = ?)",
                Boolean.class, contestId);
    }

    private static final class ContestMapper implements RowMapper<Contest> {
        public Contest mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contest contest = new Contest();
            contest.setContestId(rs.getString("contest_id"));
            contest.setName(rs.getString("name"));
            contest.setStartDate(rs.getDate("start_date"));
            contest.setDuration(rs.getInt("duration"));
            return contest;
        }
    }
}
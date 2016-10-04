package dpc.scores;

import dpc.scores.models.ScoreboardGroup;
import dpc.scores.models.ScoreboardResponse;
import dpc.std.Service;
import dpc.std.models.StdResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jiaweizhang on 10/3/2016.
 */

@org.springframework.stereotype.Service
public class ScoresService extends Service {

    public StdResponse getScoreboard(String contestId) {
        List<ScoreboardGroup> scoreboardGroups = jt.query(
                "SELECT " +
                        "(select groups.name FROM groups WHERE groups.group_id = submissions.group_id) AS group_name" +
                        ", sum(is_correct) AS score" +
                        ", sum(floor(EXTRACT(EPOCH FROM (submissions.submit_time - (SELECT start_time FROM contests WHERE contest_id =?))) * is_correct / 60)) AS penalty_from_correct_submissions" +
                        ", sum( " +
                        "CASE WHEN is_correct = 0 AND problem_number IN (SELECT problem_number FROM submissions WHERE is_correct = 1) " +
                        "THEN 20 " +
                        "ELSE 0 " +
                        "END ) AS penalty_from_incorrect_submissions " +
                        "FROM submissions " +
                        "WHERE submissions.contest_id = ? " +
                        "GROUP BY submissions.group_id;",
                new ScoreboardGroupMapper(), contestId, contestId
        );

        return new ScoreboardResponse(200, true, "Successfully fetched scoreboard", scoreboardGroups);
    }

    private static final class ScoreboardGroupMapper implements RowMapper<ScoreboardGroup> {
        public ScoreboardGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScoreboardGroup scoreboardGroup = new ScoreboardGroup();
            scoreboardGroup.groupName = rs.getString("group_name");
            scoreboardGroup.score = rs.getInt("score");
            scoreboardGroup.penaltyFromCorrectSubmissions = rs.getLong("penalty_from_correct_submissions");
            scoreboardGroup.penaltyFromIncorrectSubmissions = rs.getLong("penalty_from_incorrect_submissions");
            return scoreboardGroup;
        }
    }
}

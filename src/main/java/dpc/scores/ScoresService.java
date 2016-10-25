package dpc.scores;

import dpc.scores.models.FullScoreboardResponse;
import dpc.scores.models.GroupPart;
import dpc.scores.models.ScoreboardGroup;
import dpc.scores.models.ScoreboardResponse;
import dpc.std.Service;
import dpc.std.models.StdResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaweizhang on 10/3/2016.
 */

@org.springframework.stereotype.Service
public class ScoresService extends Service {

    public StdResponse getScoreboard(String contestId) {
        List<ScoreboardGroup> scoreboardGroups = getFullScore(contestId);

        List<GroupPart> allGroups = getAllGroups(contestId);

        return new ScoreboardResponse(200, true, "Successfully fetched scoreboard", scoreboardGroups, allGroups);
    }

    public StdResponse getFullScoreboard(String contestId) {
        List<List<ScoreboardGroup>> scoreboardGroupsList = new ArrayList<List<ScoreboardGroup>>();

        for (int i = 1; i <= 8; i++) {
            List<ScoreboardGroup> scoreboardGroupForProblem = jt.query(
                    "SELECT\n" +
                            "  (SELECT g.group_name\n" +
                            "   FROM groups g\n" +
                            "   WHERE g.group_id = s.group_id) AS group_name,\n" +
                            "  sum(is_correct)                 AS score,\n" +
                            "  sum(floor(EXTRACT(EPOCH FROM (s.submit_time - (SELECT start_time\n" +
                            "                                                 FROM contests\n" +
                            "                                                 WHERE contest_id = s.contest_id))) * is_correct / 60))\n" +
                            "                                  AS penalty_from_correct_submissions,\n" +
                            "  sum(\n" +
                            "      CASE WHEN is_correct = 0 AND (SELECT exists(SELECT *\n" +
                            "                                                  FROM submissions inner_s\n" +
                            "                                                  WHERE s.group_id = inner_s.group_id AND\n" +
                            "                                                        s.problem_number = inner_s.problem_number AND\n" +
                            "                                                        contest_id = s.contest_id AND is_correct = 1))\n" +
                            "        THEN 20\n" +
                            "      ELSE 0\n" +
                            "      END\n" +
                            "  )                               AS penalty_from_incorrect_submissions\n" +
                            "FROM submissions s\n" +
                            "WHERE s.contest_id = ?\n" +
                            "      AND s.problem_number = ?\n" +
                            "GROUP BY s.group_id;", new ScoreboardGroupMapper(), contestId, i
            );
            scoreboardGroupsList.add(scoreboardGroupForProblem);
        }

        List<ScoreboardGroup> fullScoreboard = getFullScore(contestId);

        List<GroupPart> allGroups = getAllGroups(contestId);

        return new FullScoreboardResponse(200, true, "Retrieved full scoreboard", fullScoreboard, allGroups, scoreboardGroupsList);
    }

    private List<ScoreboardGroup> getFullScore(String contestId) {
        return jt.query(
                "SELECT " +
                        "group_id AS group_id " +
                        ", (SELECT groups.name FROM groups WHERE groups.group_id = submissions.group_id) AS group_name" +
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
    }

    private List<GroupPart> getAllGroups(String contestId) {
        return jt.query("SELECT group_id, group_name FROM groups WHERE contest_id = ?", new GroupPartMapper(), contestId);
    }

    private static final class ScoreboardGroupMapper implements RowMapper<ScoreboardGroup> {
        public ScoreboardGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScoreboardGroup scoreboardGroup = new ScoreboardGroup();
            scoreboardGroup.groupId = rs.getLong("group_id");
            scoreboardGroup.groupName = rs.getString("group_name");
            scoreboardGroup.score = rs.getInt("score");
            scoreboardGroup.penaltyFromCorrectSubmissions = rs.getLong("penalty_from_correct_submissions");
            scoreboardGroup.penaltyFromIncorrectSubmissions = rs.getLong("penalty_from_incorrect_submissions");
            return scoreboardGroup;
        }
    }

    private static final class GroupPartMapper implements RowMapper<GroupPart> {
        public GroupPart mapRow(ResultSet rs, int rowNum) throws SQLException {
            GroupPart groupPart = new GroupPart();
            groupPart.groupId = rs.getLong("group_id");
            groupPart.groupName = rs.getString("group_name");
            return groupPart;
        }
    }
}

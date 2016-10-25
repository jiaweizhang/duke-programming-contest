package dpc.scores.models;

import java.util.List;

/**
 * Created by jiaweizhang on 10/25/2016.
 */
public class FullScoreboardResponse extends ScoreboardResponse {
    public List<List<ScoreboardGroup>> problemScores;

    public FullScoreboardResponse(int status, boolean success, String message, List<ScoreboardGroup> scores, List<GroupPart> allGroups, List<List<ScoreboardGroup>> problemScore) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.scores = scores;
        this.problemScores = problemScores;
        this.allGroups = allGroups;
    }
}

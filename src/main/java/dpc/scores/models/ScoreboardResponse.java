package dpc.scores.models;

import dpc.std.models.StdResponse;

import java.util.List;

/**
 * Created by jiaweizhang on 10/3/2016.
 */
public class ScoreboardResponse extends StdResponse {
    public List<ScoreboardGroup> scores;
    public List<GroupPart> allGroups;

    public ScoreboardResponse() {

    }

    public ScoreboardResponse(int status, boolean success, String message, List<ScoreboardGroup> scores, List<GroupPart> allGroups) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.scores = scores;
        this.allGroups = allGroups;
    }
}

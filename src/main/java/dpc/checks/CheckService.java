package dpc.checks;

import dpc.exceptions.ContestHasEndedException;
import dpc.exceptions.ContestNotStartedException;
import dpc.std.Service;

import java.sql.Timestamp;

/**
 * Created by jiaweizhang on 9/29/2016.
 */

@org.springframework.stereotype.Service
public class CheckService extends Service {

    public boolean contestExists(String contestId) {
        return this.jt.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM contests WHERE contests.contest_id = ?)",
                Boolean.class,
                contestId);
    }

    public boolean groupNameExists(String groupName) {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT 1 from groups WHERE group_name = ?);",
                Boolean.class,
                groupName
        );
    }

    public boolean randomGroupExists() {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM groups WHERE group_name = '')",
                Boolean.class
        );
    }

    public long getRandomGroupId() {
        // assuming one exists
        return jt.queryForObject(
                "SELECT group_id FROM groups WHERE group_name = '' ORDER BY group_id DESC LIMIT 1",
                Long.class
        );
    }

    public long groupMemberCount(long groupId) {
        return jt.queryForObject("SELECT COUNT(*) FROM group_membership WHERE group_id = ?",
                Long.class,
                groupId
        );
    }

    public long getGroupId(String groupName) {
        return jt.queryForObject(
                "SELECT group_id FROM groups WHERE group_name = ?",
                Long.class,
                groupName
        );
    }

    public boolean inGroupForContest(long userId, String contestId) {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT groups.group_id FROM groups LEFT JOIN group_membership " +
                        "ON groups.group_id = group_membership.group_id " +
                        "WHERE group_membership.user_id = ? " +
                        "AND groups.contest_id = ?)",
                Boolean.class,
                userId, contestId
        );
    }

    public long getGroupId(long userId, String contestId) {
        return jt.queryForObject(
                "SELECT groups.group_id FROM groups LEFT JOIN group_membership " +
                        "ON groups.group_id = group_membership.group_id " +
                        "WHERE group_membership.user_id = ? " +
                        "AND groups.contest_id = ?",
                Long.class,
                userId, contestId
        );
    }

    public boolean userIsInGroup(long userId, long groupId) {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM group_membership WHERE user_id = ? AND group_id = ?)",
                Boolean.class,
                userId, groupId
        );
    }

    public void checkContestOpen(String contestId) {
        // check that contest is open
        long unixTime = System.currentTimeMillis() / 1000L;
        Timestamp current = new Timestamp(unixTime);
        Timestamp startTime = jt.queryForObject(
                "SELECT start_time FROM contests WHERE contest_id = ?",
                Timestamp.class,
                contestId
        );
        Timestamp endTime = jt.queryForObject(
                "SELECT end_time FROM contests WHERE contest_id = ?",
                Timestamp.class,
                contestId
        );

        if (current.before(startTime)) {
            throw new ContestNotStartedException();
        }

        if (current.after(endTime)) {
            throw new ContestHasEndedException();
        }
    }

    public boolean hasCorrectSubmission(String contestId, long groupId, int problemNumber) {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM submissions " +
                        "WHERE contest_id = ? AND group_id = ? AND problem_number = ? AND is_correct = 1)",
                Boolean.class,
                contestId, groupId, problemNumber
        );
    }
}

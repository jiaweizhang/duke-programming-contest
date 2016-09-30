package dpc.validations;

import dpc.std.Service;

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
                "SELECT EXISTS(SELECT 1 from groups WHERE name = ?);",
                Boolean.class,
                groupName
        );
    }

    public boolean groupSecretExists(String secret) {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT 1 from groups WHERE secret = ?);",
                Boolean.class,
                secret
        );
    }

    public boolean randomGroupExists() {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM groups WHERE secret = '')",
                Boolean.class
        );
    }

    public long getRandomGroupId() {
        // assuming one exists
        return jt.queryForObject(
                "SELECT group_id FROM groups WHERE secret = '' ORDER BY group_id DESC LIMIT 1",
                Long.class
        );
    }

    public long groupMemberCount(long groupId) {
        return jt.queryForObject("SELECT COUNT(*) FROM group_membership WHERE group_id = ?",
                Long.class,
                groupId
        );
    }

    public long groupMemberCount(String secret) {
        return jt.queryForObject(
                "SELECT COUNT(*) FROM group_membership WHERE group_id = (SELECT group_id FROM groups WHERE secret = ?)",
                Long.class,
                secret
        );
    }

    public long getGroupId(String secret) {
        return jt.queryForObject(
                "SELECT group_id FROM groups WHERE secret = ?",
                Long.class,
                secret
        );
    }

    public long getGroupId(long userId, String contestId) {
        return jt.queryForObject(
                "SELECT groups.group_id FROM groups LEFT JOIN group_membership " +
                        "ON groups.group_id = group_membership.group_id " +
                        "WHERE group_membership.user_id = ? " +
                        "AND groups.contestId = ?",
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
}

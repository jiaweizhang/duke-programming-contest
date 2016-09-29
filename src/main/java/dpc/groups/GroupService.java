package dpc.groups;

import dpc.exceptions.GroupFullException;
import dpc.exceptions.NotGroupMemberException;
import dpc.groups.models.CreateGroupRequest;
import dpc.groups.models.JoinGroupRequest;
import dpc.std.Service;
import dpc.std.StdRequest;
import dpc.std.StdResponse;
import dpc.validations.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by jiaweizhang on 9/28/2016.
 */

@org.springframework.stereotype.Service
class GroupService extends Service {

    @Autowired
    private CheckService checkService;

    @Transactional
    StdResponse createGroup(CreateGroupRequest request, String contestId) {
        // validate request
        // check that contestId is valid
        if (!checkService.contestExists(contestId)) {
            throw new IllegalArgumentException("contest id does not exist");
        }
        // check that group name isn't taken
        if (!checkService.groupNameExists(request.name)) {
            throw new IllegalArgumentException("group name already exists");
        }
        // check that secret isn't taken and is not ""
        if (request.secret.equals("") || !checkService.groupSecretExists(request.secret)) {
            throw new IllegalArgumentException("group secret already exists");
        }

        // add to groups and group_membership tables
        createGroupAndInsertUserSQL(request.name, request.secret, contestId, request.userId);

        // return success
        return new StdResponse(200, true, "Successfully created group");
    }

    @Transactional
    StdResponse joinGroup(JoinGroupRequest request, String contestId) {
        // validate request
        // check that contestId is valid
        if (!checkService.contestExists(contestId)) {
            throw new IllegalArgumentException("contest id does not exist");
        }

        // if secret is empty, join a group with no secret or create one if no group exists
        if (request.secret.length() == 0 && checkService.randomGroupExists()) {
            if (checkService.randomGroupExists()) {
                long groupId = checkService.getRandomGroupId();
                joinGroupSQL(groupId, request.userId);
                return new StdResponse(200, true, "Successfully joined a random group");
            } else {
                createGroupAndInsertUserSQL(UUID.randomUUID().toString(), "", contestId, request.userId);
                return new StdResponse(200, true, "Successfully created new group and joined");
            }
        }

        // check that secret exists
        if (!checkService.groupSecretExists(request.secret)) {
            throw new IllegalArgumentException("secret does not exist");
        }

        // check that there are currently fewer than 3 members
        if (checkService.groupMemberCount(request.secret) > 2) {
            throw new GroupFullException();
        }

        // add user to group
        joinGroupSecretSQL(request.secret, request.userId);

        // return success
        return new StdResponse(200, true, "Successfully joined group");
    }

    @Transactional
    StdResponse leaveGroup(StdRequest request, String contestId) {
        // validate request
        // check that contestId is valid
        if (!checkService.contestExists(contestId)) {
            throw new IllegalArgumentException("contest id does not exist");
        }

        // check that user is member of a group in the contest
        long groupId = 0;
        try {
            groupId = checkService.getGroupId(request.userId, contestId);
        } catch (Exception e) {
            throw new NotGroupMemberException();
        }

        // remove user
        jt.update(
                "DELETE FROM group_membership WHERE user_id = ?",
                request.userId
        );

        // if number of members is 0, delete the group
        long members = checkService.groupMemberCount(groupId);
        if (members == 0) {
            jt.update(
                    "DELETE FROM groups WHERE group_id = ?",
                    groupId
            );
        }

        // return success
        return new StdResponse(200, true, "Successfully left group");
    }

    private void createGroupAndInsertUserSQL(String name, String secret, String contestId, long userId) {
        final String INSERT_SQL =
                "INSERT INTO groups (name, secret, contest_id) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jt.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"group_id"});
                        ps.setString(1, name);
                        ps.setString(2, secret);
                        ps.setString(3, contestId);
                        return ps;
                    }
                },
                keyHolder);
        long groupId = keyHolder.getKey().longValue();

        joinGroupSQL(groupId, userId);
    }

    private void joinGroupSQL(long groupId, long userId) {
        jt.update(
                "INSERT INTO group_membership (group_id, user_id) VALUES (?, ?)",
                groupId, userId);
    }

    private void joinGroupSecretSQL(String secret, long userId) {
        jt.update(
                "INSERT INTO group_membership (group_id, user_id) VALUES (SELECT group_id FROM groups WHERE secret = ?, ?)",
                secret, userId);
    }
}

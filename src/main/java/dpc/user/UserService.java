package dpc.user;

import dpc.std.Service;
import dpc.std.models.StdRequest;
import dpc.std.models.StdResponse;
import dpc.user.models.User;
import dpc.user.models.UserResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jiaweizhang on 10/27/2016.
 */

@org.springframework.stereotype.Service
public class UserService extends Service {
    public StdResponse userInfo(StdRequest stdRequest) {
        // no need to check user exists because token was somehow created
        User user = jt.queryForObject(
                "SELECT user_id, email, name, school, class_in_school FROM users WHERE user_id = ?",
                new UserMapper(), stdRequest.userId
        );
        return new UserResponse(200, true, "Returning user", user);
    }

    private static final class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.userId = rs.getLong("user_id");
            user.email = rs.getString("email");
            user.name = rs.getString("name");
            user.school = rs.getString("school");
            user.classInSchool = rs.getString("class_in_school");
            return user;
        }
    }
}

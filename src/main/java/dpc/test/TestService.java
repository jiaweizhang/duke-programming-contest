package dpc.test;

import dpc.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.PreparedStatement;

/**
 * Created by jiaweizhang on 9/30/2016.
 */

@Service
public class TestService extends dpc.std.Service {

    @Autowired
    private AdminService adminService;

    public void updateDb() {
        try {
            adminService.upgradeDb();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long createUser(String netId) {

        final String INSERT_SQL =
                "INSERT INTO users (net_id) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jt.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"user_id"});
                    ps.setString(1, netId);
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }
}

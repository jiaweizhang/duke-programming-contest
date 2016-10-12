package dpc.admin;

import dpc.auth.AuthService;
import dpc.auth.models.AuthResponse;
import dpc.std.Service;
import dpc.std.models.StdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import utilities.TokenUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiaweizhang on 9/14/2016.
 */

@Transactional
@org.springframework.stereotype.Service
public class AdminService extends Service {

    @Autowired
    private AuthService authService;

    public StdResponse getToken(String userId) {
        String token = TokenUtility.generateToken(Long.parseLong(userId));
        return new AuthResponse(200, true, "Successfully authenticated", token);
    }

    public StdResponse upgradeDb() {
        try {
            String query = readQuery("sql/setup.sql");
            jt.execute(query);

            return new StdResponse(200, true, "Successfully upgraded database");
        } catch (Exception e) {
            e.printStackTrace();
            return new StdResponse(500, false, "Failed to upgrade database");
        }
    }

    private List<String> getFilesInDirectory(String directory) {
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        return Arrays.stream(listOfFiles).filter(File::isFile).map(File::getAbsolutePath).collect(Collectors.toList());
    }

    private String readQuery(String file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        StringBuilder sb = new StringBuilder();
        while ((str = in.readLine()) != null) {
            int index;
            // removes single-line comments
            if ((index = str.indexOf("--")) >= 0) {
                str = str.substring(0, index);
            }
            sb.append(str).append(" ");
        }
        in.close();

        return sb.toString();
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

package dpc.auth;

import dpc.auth.models.AuthResponse;
import dpc.auth.models.LoginRequest;
import dpc.auth.models.RegisterRequest;
import dpc.exceptions.IncorrectPasswordException;
import dpc.exceptions.UserNotFoundException;
import dpc.std.models.StdResponse;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.TokenUtility;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by jiaweizhang on 9/16/2016.
 */
@Transactional
@Service
public class AuthService extends dpc.std.Service {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public StdResponse register(RegisterRequest registerRequest) {
        // check that email doesn't exist
        if (emailExists(registerRequest.email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // check that name doesn't exist
        if (nameExists(registerRequest.name)) {
            throw new IllegalArgumentException("Name already exists");
        }

        // hash the password
        String passhash = passwordEncoder.encode(registerRequest.password);

        // create the user
        long userId = createUser(registerRequest.email,
                registerRequest.name,
                passhash,
                registerRequest.school,
                registerRequest.classInSchool);

        // generate the token
        String token = TokenUtility.generateToken(userId);

        // return the auth token
        return new AuthResponse(200, true, "Successfully registered account", token);
    }

    public StdResponse login(LoginRequest loginRequest) {
        // check the passhash
        if (!passwordEncoder.matches(loginRequest.password, getPasshash(loginRequest.email))) {
            throw new IncorrectPasswordException();
        }

        // generate a token
        long userId = getUserId(loginRequest.email);

        // return the token
        String token = TokenUtility.generateToken(userId);
        return new AuthResponse(200, true, "Successfully logged in", token);
    }

    private long getUserId(String email) {
        List<Long> userIds = jt.queryForList("SELECT user_id FROM users WHERE email = ?", Long.class, email);
        if (userIds.size() == 1) {
            return userIds.get(0);
        } else {
            throw new UserNotFoundException();
        }
    }

    private String getPasshash(String email) {
        List<String> passhashes = jt.queryForList("SELECT passhash FROM users WHERE email = ?", String.class, email);
        if (passhashes.size() == 1) {
            return passhashes.get(0);
        } else {
            throw new UserNotFoundException();
        }
    }

    private long createUser(String email, String name, String passhash, String school, String classInSchool) {
        final String INSERT_SQL =
                "INSERT INTO users (email, name, passhash, school, class_in_school) " +
                        "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jt.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"user_id"});
                    ps.setString(1, email);
                    ps.setString(2, name);
                    ps.setString(3, passhash);
                    ps.setString(4, school);
                    ps.setString(5, classInSchool);
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }

    private boolean emailExists(String email) {
        return this.jt.queryForObject(
                "SELECT EXISTS(SELECT 1 from users WHERE users.email = ?)",
                Boolean.class, email);
    }

    private boolean nameExists(String name) {
        return this.jt.queryForObject("SELECT EXISTS(SELECT 1 from USERS where users.name = ?)",
                Boolean.class, name);
    }
}
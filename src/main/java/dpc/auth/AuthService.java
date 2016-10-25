package dpc.auth;

import dpc.auth.models.*;
import dpc.exceptions.IncorrectPasswordException;
import dpc.exceptions.UserNotFoundException;
import dpc.std.models.StdResponse;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.EmailUtility;
import utilities.RNGUtility;
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

        // send email
        EmailUtility.sendEmail(registerRequest.email, "Duke Programming Contest Registration", "Hi " + registerRequest.name + ",\n\nYour email has been registered.\n\nCheers,\nDPC");

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

    public StdResponse forgot(ForgotRequest forgotRequest) {
        // validate email exists
        if (!emailExists(forgotRequest.email)) {
            throw new IllegalArgumentException("Email does not exist");
        }

        // generate random token
        String token = RNGUtility.generateToken();

        // insert/replace token associated with email address
        insertPasswordRequest(forgotRequest.email, token);

        // send email with token
        EmailUtility.sendEmail(forgotRequest.email, "Duke Programming Contest - Reset Password",
                "Hi, Please enter the following token: " + token + ". Thanks!");

        return new StdResponse(200, true, "Sent email with password recovery instructions");
    }

    public StdResponse validateToken(ValidateTokenRequest validateTokenRequest) {
        if (passwordRecoveryTokenExists(validateTokenRequest.token)) {
            // exists and is valid
            return new StdResponse(200, true, "Token is valid");
        } else {
            // token is not found
            return new StdResponse(200, false, "Token is not valid");
        }
    }

    public StdResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        if (passwordRecoveryTokenExists(resetPasswordRequest.token)) {
            // retrieve corresponding email
            String email = findMatchingEmail(resetPasswordRequest.token);

            // remove the token from the table
            deletePasswordRequest(resetPasswordRequest.token);

            // hash the password
            String passhash = passwordEncoder.encode(resetPasswordRequest.password);

            // update the passhash
            updatePasshash(email, passhash);

            return new StdResponse(200, true, "Password has been reset");
        } else {
            // token is not found
            return new StdResponse(200, false, "Token is not valid");
        }
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
                "SELECT EXISTS(SELECT 1 FROM users WHERE users.email = ?)",
                Boolean.class, email);
    }

    private boolean passwordRecoveryRequestExists(String email) {
        return this.jt.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM password_recovery WHERE password_recovery.email = ?)",
                Boolean.class, email);
    }

    private boolean passwordRecoveryTokenExists(String token) {
        return jt.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM password_recovery WHERE password_recovery.token = ?)",
                Boolean.class, token);
    }

    private void insertPasswordRequest(String email, String token) {
        if (passwordRecoveryRequestExists(email)) {
            // update
            jt.update("UPDATE password_recovery SET token = ? WHERE email = ?", token, email);
        } else {
            // insert
            jt.update("INSERT INTO password_recovery (email, token) VALUES (?, ?)", email, token);
        }
    }

    private String findMatchingEmail(String token) {
        return jt.queryForObject(
                "SELECT email FROM password_recovery WHERE token = ?",
                String.class, token);
    }

    private void deletePasswordRequest(String token) {
        jt.update(
                "DELETE FROM password_recovery WHERE token = ?",
                token);
    }

    private void updatePasshash(String email, String passhash) {
        jt.update(
                "UPDATE users SET passhash = ? WHERE email = ?", passhash, email);
    }
}
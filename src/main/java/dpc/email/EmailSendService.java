package dpc.email;

import dpc.email.models.Email;
import dpc.std.Service;
import dpc.std.models.StdResponse;
import org.springframework.jdbc.core.RowMapper;
import utilities.EmailUtility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiaweizhang on 10/27/2016.
 */

@org.springframework.stereotype.Service
public class EmailSendService extends Service {

    public StdResponse start() {
        ScheduledExecutorService execService = Executors.newScheduledThreadPool(1);
        execService.scheduleWithFixedDelay(() -> {
            while (true) {
                // check if there exists an email to send
                boolean emailExists = jt.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM email_queue)", Boolean.class);
                System.out.println("Checking for email. Email exists: " + emailExists);
                if (emailExists) {
                    Email email = jt.queryForObject(
                            "SELECT email_id, to_email, subject, content FROM email_queue ORDER BY email_id LIMIT 1",
                            new EmailMapper());

                    // send email
                    EmailUtility.sendEmail(email.to, email.subject, email.content);

                    // delete the email
                    jt.update(
                            "DELETE FROM email_queue WHERE email_id = ?", email.email_id
                    );
                } else {
                    // no more emails
                    // go to sleep
                    break;
                }
            }

        }, 0, 15, TimeUnit.SECONDS);
        return new StdResponse(200, true, "Email service started");
    }

    private static final class EmailMapper implements RowMapper<Email> {
        public Email mapRow(ResultSet rs, int rowNum) throws SQLException {
            Email email = new Email();
            email.email_id = rs.getLong("email_id");
            email.to = rs.getString("to_email");
            email.subject = rs.getString("subject");
            email.content = rs.getString("content");
            return email;
        }
    }
}

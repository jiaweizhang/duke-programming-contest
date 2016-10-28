package dpc.email;

import dpc.std.Service;

/**
 * Created by jiaweizhang on 10/27/2016.
 */

@org.springframework.stereotype.Service
public class EmailQueueService extends Service {

    public void sendEmail(String to, String subject, String content) {
        jt.update(
                "INSERT INTO email_queue (to_email, subject, content) VALUES (?, ?, ?);",
                to, subject, content
        );
    }
}

package utilities;

import dpc.exceptions.EmailException;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by jiaweizhang on 10/24/2016.
 */

public class EmailUtility {

    public static void sendEmail(String to, String subject, String content) {
        try {
            Properties properties1 = System.getProperties();
            properties1.put("mail.smtp.port", "587");
            properties1.put("mail.smtp.auth", "true");
            properties1.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getDefaultInstance(properties1, null);
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(content, "text/html");

            Transport transport = session.getTransport("smtp");

            Properties properties = PropertiesLoader.loadPropertiesFromPackage("email.properties");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            transport.connect("smtp.gmail.com", username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            throw new EmailException();
        }
    }
}
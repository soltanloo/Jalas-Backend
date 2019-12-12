package Services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final String USERNAME = "jalas.jugger@outlook.com";
    private static final String PASSWORD = "jalasjalas123";
    private static final String EMAIL_FROM = "jalas.jugger@outlook.com";
    private static final String EMAIL_SUBJECT = "Jalas new notification";

    public static void sendMail(String receiverAddress, String content) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "outlook.office365.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(receiverAddress)
            );
            message.setSubject(EMAIL_SUBJECT);
            message.setText(content);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
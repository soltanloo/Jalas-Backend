package Services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ErrorClasses.InvalidEmailAddressException;
import org.apache.commons.validator.routines.EmailValidator;

public class EmailService {
    private static final String USERNAME = "jalas.jugger@outlook.com";
    private static final String PASSWORD = "jalasjalas123";
    private static final String EMAIL_FROM = "jalas.jugger@outlook.com";
    private static final String EMAIL_SUBJECT = "Jalas new notification";

    public static boolean isEmailValid(String emailAddress) {
        EmailValidator validator = EmailValidator.getInstance(false);
        return validator.isValid(emailAddress);
    }

    public static void sendMail(String receiverAddress, String content) {
        new Thread(() -> {
            try {
                sendMailAsync(receiverAddress, content);
            } catch (InvalidEmailAddressException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static ArrayList<String> parseEmailsFromText(String text) {
        ArrayList<String> emails = new ArrayList<>();

        Matcher matcher = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").matcher(text);
        while (matcher.find()) {
            emails.add(matcher.group());
        }
        return emails;
    }
    private static void sendMailAsync(String receiverAddress, String content) throws InvalidEmailAddressException {
        if (!isEmailValid(receiverAddress))
            throw new InvalidEmailAddressException();
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
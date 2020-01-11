package Services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

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

    public static String parseEmailFromText(String text){
        String ePattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(text);
        if(m.matches()) {
            String mail = m.toString();
            System.out.print(mail);
            return mail;
        }
        else{
            return null;
        }
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
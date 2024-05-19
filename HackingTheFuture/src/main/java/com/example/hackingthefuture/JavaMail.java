package com.example.hackingthefuture;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class JavaMail {
    public static void sendmail(String recipient,String code,String title) throws MessagingException {
        String username = "jojochaw731@gmail.com";
        final String password = "1024YH_jo99%";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = prepareMessage(session, username, recipient, code, title);
        Transport.send(message);
        System.out.println("Email sent successfully!");
    }

    private static Message prepareMessage(Session session, String username, String recipient, String code, String title) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(title);
        message.setText(code);
        return message;
    }
}

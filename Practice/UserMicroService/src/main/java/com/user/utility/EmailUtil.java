package com.user.utility;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String FROM_EMAIL = "suryansh.pushpkar@webkorps.com";
    private static final String APP_PASSWORD = "roxwscxofarqqjdj"; 

    public static void sendOtpEmail(String recipientEmail, String name, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            
            message.setSubject("Your One-Time Password (OTP) Verification");
            
            String emailContent = "Hello " + name + ",\n\n"
                    + "Your verification code is: " + otp + "\n\n"
                    + "This code will expire in 5 minutes. Do not share this OTP with anyone.\n\n"
                    + "Regards,\nYour App Team";
            
            message.setText(emailContent);

            Transport.send(message);
            System.out.println("OTP successfully sent to: " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
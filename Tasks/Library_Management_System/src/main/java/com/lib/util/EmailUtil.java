package com.lib.util;
//
//import java.util.Properties;
//import jakarta.mail.*;
//import jakarta.mail.internet.*;
//
//public class EmailUtil {
//
//	private static final String SMTP_HOST = "smtp.gmail.com";
//	private static final String SMTP_PORT = "587";
//	private static final String SENDER_EMAIL = "suryanshpushpkar183@gmail.com";
//	private static final String SENDER_PASSWORD = "Suryansh@2004";
//
//	public static void sendWelcomeEmail(String recipientEmail, String name, String membershipNo, String rawPassword) {
//
//		Properties props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", SMTP_HOST);
//		props.put("mail.smtp.port", SMTP_PORT);
//
//		Session session = Session.getInstance(props, new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
//			}
//		});
//
//		try {
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress(SENDER_EMAIL));
//			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
//			message.setSubject("Library Management System - Registration Successful");
//
//			String htmlContent = "<h2>Hello " + name + ",</h2>"
//					+ "<p>Welcome to the Library Management System! Your registration is complete.</p>"
//					+ "<p>Please keep your login credentials safe:</p>"
//					+ "<div style='background-color: #f4f4f4; padding: 10px; border-radius: 5px;'>"
//					+ "<strong>Membership No:</strong> " + membershipNo + "<br>" + "<strong>Password:</strong> "
//					+ rawPassword + "</div>" + "<p>You can now log in and explore your dashboard.</p>"
//					+ "<br><p>Regards,<br>Library Management Team</p>";
//
//			message.setContent(htmlContent, "text/html");
//
//			Transport.send(message);
//			System.out.println("Success: Registration email sent to " + recipientEmail);
//
//		} catch (MessagingException e) {
//			e.printStackTrace();
//			System.err.println("Error: Could not send email. Check your SMTP settings.");
//		}
//	}
//}
//

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    public static void sendWelcomeEmail(String recipientEmail, String name, String membershipNo, String rawPassword) {
        // 1. Setup SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Required for Gmail
        
        // 2. Credentials
        final String username = "suryansh.pushpkar@webkorps.com";
        final String appPassword = "roxwscxofarqqjdj"; // The 16-digit code from Google

        // 3. Create Session with Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        try {
            // 4. Compose Message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Welcome to Our Library!");
            message.setText("Hello! Your registration was successful. \n your membershipNo.:"+ membershipNo + "and Password:"+rawPassword);

            // 5. Send
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

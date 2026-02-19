package com.lib.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

	private static final String SMTP_HOST = "smtp.gmail.com";
	private static final String SMTP_PORT = "587";
	private static final String USERNAME = "suryansh.pushpkar@webkorps.com";
	private static final String APP_PASSWORD = "roxwscxofarqqjdj";

	public static void sendWelcomeEmail(String recipientEmail, String name, String membershipNo, String rawPassword) {
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST);
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, APP_PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

			if (membershipNo.startsWith("AD")) {
				message.setSubject("Library Management - Admin Credentials");
				message.setText("Dear " + name + ",\n\n"
						+ "You have been successfully registered as an Administrator.\n"
						+ "Please use the following credentials to access the Admin Dashboard:\n\n" + "Membership No: "
						+ membershipNo + "\n" + "Password: " + rawPassword + "\n\n" + "Regards,\nSystem Administrator");
			} else {
				message.setSubject("Welcome to the Library!");
				message.setText("Hello " + name + ",\n\n" + "Welcome! You are now a registered member of our Library.\n"
						+ "You can login to your student portal using:\n\n" + "Membership No: " + membershipNo + "\n"
						+ "Password: " + rawPassword + "\n\n" + "Happy Reading!");
			}

			Transport.send(message);
			System.out.println("Email sent successfully to: " + recipientEmail);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
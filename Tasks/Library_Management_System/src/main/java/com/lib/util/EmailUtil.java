package com.lib.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

	public static void sendWelcomeEmail(String recipientEmail, String name, String membershipNo, String rawPassword) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		final String username = "suryansh.pushpkar@webkorps.com";
		final String appPassword = "roxwscxofarqqjdj";

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, appPassword);
			}
		});
		String adminMessage = "Hello "+name+"! You are successfully registered as the admin of the Library \n your membershipNo.:"
				+ membershipNo + " \nand Password:" + rawPassword;
		String userMessage = "Welcome! " + name
				+ " You are registered as the memeber of the Library : \n Your \n membershipNo. : " + membershipNo
				+ " \t and \n Password: " + rawPassword + " ";

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject("Welcome to Our Library!");
			if (membershipNo.startsWith("AD")) {
				message.setText(adminMessage);
			} else {
				message.setText(userMessage);
			}

			Transport.send(message);
			System.out.println("Email sent successfully!");

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

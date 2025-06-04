package WebProject.ReRover.util;

import java.util.Properties;

import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSending {

    private static final String SMTP_HOST = "smtp.gmail.com";  // For Gmail SMTP server
    private static final String SMTP_PORT = "587"; // SMTP port for Gmail
    private static final String USERNAME = "baoduy.song@gmail.com"; // Hotel email address
    private static final String PASSWORD = "vyxz jvwf qddf dkzg"; // Your app-specific password for Gmail

    /**
     * Sends an email using Gmail's SMTP server
     *
     * @param to      The recipient's email address
     * @param subject The subject of the email
     * @param body    The body of the email
     */
    public static void sendEmail(String to, String subject, String body) {
        // Set up the SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create the session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME)); // From address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // To address
            message.setSubject(subject); // Subject
            message.setText(body); // Email body

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully to " + to);
        } catch (AuthenticationFailedException e) {
            System.err.println("Authentication failed. Please check your username, password, and app-specific settings.");
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email.");
        }
    }

    /**
     * Sends a simple email (for use in forgot password, etc.)
     *
     * @param to      The recipient's email address
     * @param subject The subject of the email
     * @param body    The body of the email
     */
    public static void sendSimpleEmail(String to, String subject, String body) {
        sendEmail(to, subject, body);
    }

    public static void main(String[] args) {
        // Example usage
        sendSimpleEmail("baoduy.song@gmail.com", "Test Subject", "This is a test email body.");
    }
}
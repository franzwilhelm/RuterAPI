import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class Mail {

    private String
            tag,
            sender,
            subject,
            body;

    public Mail(String tag, String sender, String subject, String body) {
        this.tag = tag;
        this.sender = sender;
        this.subject = subject;
        this.body = body;
    }

    void send() throws FileNotFoundException {
        File file = new File("./data/gmail_password.txt");
        Scanner sc = new Scanner(file);
        String to = "trigger@applet.ifttt.com";
        String from = sender;
        final String username = sender;
        final String password = sc.nextLine();

        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(tag + " " + subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("\n------------- MAIL SENT -------------"
                    + "\nFROM ----> " + sender
                    + "\nTO ------> " + to
                    + "\nSUBJECT -> " + subject
                    + "\n\n--------------- BODY ----------------\n" + body + "\n");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
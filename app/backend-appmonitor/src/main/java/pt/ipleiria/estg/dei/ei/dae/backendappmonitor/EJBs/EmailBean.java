package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.EJBs;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;

@Stateless
public class EmailBean {
    @Resource(name = "java:/jboss/mail/fakeSMTP")
    private Session mailSession;

    public void send(String to, String subject, String text) throws
            MessagingException {
        Message message = new MimeMessage(mailSession);
        try {
            // Adjust the recipients. Here we have only one recipient.
            // The recipient's address must be an instance of InternetAddress
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false));

            // Set the message's subject
            message.setSubject(subject);

            // Insert the message's body
            message.setText(text);

            // Adjust the date of sending the message
            Date timeStamp = new Date();
            message.setSentDate(timeStamp);

            // Send the message
            Transport.send(message);
        } catch (MessagingException e) {
            throw e;
        }
    }
}

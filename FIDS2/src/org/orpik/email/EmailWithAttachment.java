/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.orpik.email;

/**
 *
 * @author Kiprotich Chemweno
 */
import java.sql.ResultSet;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import org.orpik.data.ExecuteQuery;
import org.orpik.data.QueryBuilder;
import org.orpik.logging.LogManager;

public class EmailWithAttachment {

    private String sender = "";
    private String receiver = "";
    private String receiverCc = "";
    private String host = "";
    private String port = "";
    private String senderPassword = "";
    private String messageBody = "";
    private String messageSubject = "";
    private Properties properties = null;
    private ResultSet resultset = null;
    private QueryBuilder queryBuilder = new QueryBuilder();
    private ExecuteQuery executeQuery = new ExecuteQuery();
    private static LogManager logManager = new LogManager();

    public Properties getEmailProperties() {
        logManager.logInfo("Entering 'getEmailProperties()' method");
        String sqlString = "";
        try {
            sqlString = queryBuilder.querySelectEmailProperties();
            resultset = executeQuery.executeSelect(sqlString);
            //properties = System.getProperties();
            properties = new Properties();
            if (resultset.next()) {
                properties.put("message", resultset.getString("email_message").trim());
                properties.put("attachment_location", resultset.getString("attachment_default_location").trim());
                properties.put("sender", resultset.getString("sender_email").trim());
                properties.put("password", resultset.getString("sender_password").trim());
                properties.put("subject", resultset.getString("email_subject").trim());
                properties.put("recipient", resultset.getString("email_recipient").trim());
                properties.put("recipient_cc", resultset.getString("email_cc").trim());

                properties.put("mail.smtp.host", resultset.getString("sender_host").trim());
                properties.put("mail.smtp.port", resultset.getInt("sender_port"));
                properties.put("mail.smtps.auth", "true");
                properties.put("mail.smtps.starttls.enable", "true");
                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                properties.put("mail.debug", "false");
            }
        } catch (Exception exception) {
            logManager.logError("Exception was thrown and caught in 'getEmailProperties()' method");
        }
        logManager.logInfo("Exiting 'getEmailProperties()' method");
        return properties;
    }

    public boolean sendEmailWithAttachment(String fileAttachment) {
        logManager.logInfo("Entering 'sendEmailWithAttachment(String fileAttachment)' method");
        //Dataset is one of slims or markets
        boolean debug = false;
        boolean emailSendSuccess = false;
        Transport transport = null;
        Properties props = null;

        try {            
              props = getEmailProperties(); 
              sender = props.getProperty("sender"); 
              receiver = props.getProperty("recipient"); 
              receiverCc =props.getProperty("recipient_cc"); 
              host = props.getProperty("mail.smtp.host"); 
              port = props.getProperty("mail.smtp.port"); 
              senderPassword = props.getProperty("password"); 
              messageBody = props.getProperty("message"); 
              messageBody = messageBody.replace("#", "\n");
              messageSubject = props.getProperty("subject");

            // Get session
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    //return new PasswordAuthentication(sender, senderPassword);
                    return new PasswordAuthentication("som.field.data@gmail.com", "field.data");
                    //return new PasswordAuthentication(senderEmail,senderPass);
                }
            });
            // Define message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(receiverCc));
            message.setSubject(messageSubject);

            // create the message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            //Fill message
            messageBodyPart.setText(messageBody);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            //Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileAttachment);
            messageBodyPart.setDataHandler(new DataHandler(source));

            messageBodyPart.setFileName(fileAttachment);
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            message.setContent(multipart);
            //Connect
            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "som.field.data@gmail.com", "field.data");
            //transport.connect(host, sender, senderPassword);//host
            //transport.connect(host, sender, senderPassword);
            message.saveChanges();

            transport.sendMessage(message, message.getAllRecipients());
            // Send the message
            emailSendSuccess = true;//if program counter reaches here, the message must have been sent successfully
        } catch (MessagingException messagingException) {
              JOptionPane.showMessageDialog(null, "Internet connection does not exist on this computer. "
                     + "\nPlease send data later or use a computer connected to the internet.", "Internet connection not available",
                   JOptionPane.WARNING_MESSAGE);
            //messagingException.getMessage();
            //messagingException.getLocalizedMessage();
            //messagingException.printStackTrace();
            logManager.logError("MessagingException was thrown and caught in 'sendEmailWithAttachment(String fileAttachment)' method");
        } catch (Exception exception) {
            //exception.printStackTrace();
            emailSendSuccess = false;
        } finally {
            try {
                transport.close();
            } catch (MessagingException messagingException) {
                //messagingException.printStackTrace();
                logManager.logError("MessagingException was thrown and caught in 'sendEmailWithAttachment(String fileAttachment)' finally section");
            }
        }
        logManager.logInfo("Exiting 'sendEmailWithAttachment(String fileAttachment)' method");
        return emailSendSuccess;
    }
}

package com.Imap;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;



public class GmailLatestMailBySubject {

    public static void main(String[] args) {
        String host = "imap.gmail.com";
        String username = "stevetest112233@gmail.com";   // your Gmail
        String password = "vcrb mkwg gpjg woxo";      // Gmail App Password

        String searchSubject = "Application KT"; // <<< subject to search

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imaps");
            store.connect(host, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (int i = messages.length - 1; i >= 0; i--) {
                String subject = messages[i].getSubject();
                if (subject != null && subject.contains(searchSubject)) { 
                    // partial match

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    System.out.println("Received: " + sdf.format(messages[i].getReceivedDate()));
                    System.out.println("Found matching subject: " + subject);
                    saveHtmlFromMessage(messages[i], "C:\\Users\\User\\Downloads\\email_page.html");
                    break; // stop at latest match
                }
            }
            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveHtmlFromMessage(Message message, String filePath) throws Exception {
        String htmlContent = getHtmlFromMessage(message);

        if (htmlContent != null && !htmlContent.isEmpty()) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(htmlContent);
                System.out.println("Mail saved as HTML: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No HTML content found in this email.");
        }
    }

    private static String getHtmlFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getHtmlFromMimeMultipart(mimeMultipart);
        }
        return null;
    }

    private static String getHtmlFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("text/html")) {
                return (String) bodyPart.getContent();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                return getHtmlFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return null;
    }
}


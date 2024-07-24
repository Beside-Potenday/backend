package alphamail.com.backend.mail;

import alphamail.com.backend.google.util.ConfigUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Properties;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final ConfigUtils configUtils;

    public MailService(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

    public Gmail getGmailService(String accessToken) throws GeneralSecurityException, IOException {
        GoogleCredential credential = new GoogleCredential.Builder()
            .setClientSecrets(configUtils.getGoogleClientId(), configUtils.getGoogleSecret())
            .setJsonFactory(JSON_FACTORY)
            .setTransport(GoogleNetHttpTransport.newTrustedTransport())
            .build()
            .setAccessToken(accessToken);

        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
            .setApplicationName("YOUR_APPLICATION_NAME")
            .build();
    }

    public void sendEmail(String accessToken, String to, String from, String subject, String bodyText) throws MessagingException, IOException, GeneralSecurityException {
        MimeMessage email = createEmail(to, from, subject, bodyText);
        sendMessage(getGmailService(accessToken), "me", email);
    }

    public MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(baos.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        service.users().messages().send(userId, message).execute();
    }
}

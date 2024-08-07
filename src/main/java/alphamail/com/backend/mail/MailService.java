package alphamail.com.backend.mail;

import alphamail.com.backend.google.util.ConfigUtils;
import alphamail.com.backend.mail.model.MailEntity;
import alphamail.com.backend.user.entity.MemberEntity;
import alphamail.com.backend.user.repository.TokenRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailService {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final ConfigUtils configUtils;
    private final TokenRepository tokenRepository;
    private final MailRepository mailRepository;

    public MailService(ConfigUtils configUtils, TokenRepository tokenRepository,
        MailRepository mailRepository) {
        this.configUtils = configUtils;
        this.tokenRepository = tokenRepository;
        this.mailRepository = mailRepository;
    }

    public void sendEmail(String accessToken, MailRequest.Send emailToSend)
        throws MessagingException, IOException, GeneralSecurityException {
        MimeMessage email = createEmail(emailToSend.to(), emailToSend.from(), emailToSend.subject(),
            emailToSend.body());
        sendMessage(getGmailService(accessToken), "me", email);
    }

    @Transactional
    public void saveEmail(String accessToken, MailRequest.Save emailToSave) {
        MemberEntity member = tokenRepository.findByAccessToken(accessToken).getMemberEntity();
        MailEntity mail = new MailEntity(member, emailToSave.type(), emailToSave.subject(), emailToSave.body());
        mailRepository.save(mail);
    }

    @Transactional
    public Page<MailResponse> getEmails(String accessToken, String type, Pageable pageable) {
        MemberEntity member = tokenRepository.findByAccessToken(accessToken).getMemberEntity();
        return mailRepository.findAllByMemberEntityIdAndType(member.getId(), type, pageable).map(MailResponse::from);
    }

    private Gmail getGmailService(String accessToken) throws GeneralSecurityException, IOException {
        GoogleCredential credential = new GoogleCredential.Builder()
            .setClientSecrets(configUtils.getGoogleClientId(), configUtils.getGoogleSecret())
            .setJsonFactory(JSON_FACTORY)
            .setTransport(GoogleNetHttpTransport.newTrustedTransport())
            .build()
            .setAccessToken(accessToken);

        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
            credential)
            .setApplicationName("YOUR_APPLICATION_NAME")
            .build();
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText)
        throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    private void sendMessage(Gmail service, String userId, MimeMessage email)
        throws MessagingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(baos.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        service.users().messages().send(userId, message).execute();
    }
}

package alphamail.com.backend.mail;

import alphamail.com.backend.mail.model.MailEntity;

import java.time.LocalDateTime;

public record MailResponse(String subject,
                           String body,
                           LocalDateTime createDate) {

    public static MailResponse from(MailEntity mail) {
        return new MailResponse(mail.getSubject(),
            mail.getBody(), mail.getCreatedDate());
    }
}

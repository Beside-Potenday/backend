package alphamail.com.backend.mail;

import alphamail.com.backend.mail.model.MailEntity;

public record MailResponse(String subject,
                           String body) {

    public static MailResponse from(MailEntity mail) {
        return new MailResponse(mail.getSubject(),
            mail.getBody());
    }
}

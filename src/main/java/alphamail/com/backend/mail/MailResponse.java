package alphamail.com.backend.mail;

import alphamail.com.backend.mail.model.MailEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MailResponse(String subject,
                           String body,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
                           LocalDateTime createDate) {

    public static MailResponse from(MailEntity mail) {
        return new MailResponse(mail.getSubject(),
                mail.getBody(), mail.getCreatedDate());
    }
}

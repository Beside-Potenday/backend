package alphamail.com.backend.mail;

public class MailRequest {

    public record Send(String to,
                       String from,
                       String subject,
                       String body
                       ) {
    }

    public record Save(String subject,
                       String body) {

    }
}

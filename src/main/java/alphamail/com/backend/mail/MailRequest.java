package alphamail.com.backend.mail;

public class MailRequest {
    private String to;
    private String from;
    private String subject;
    private String body;

    public MailRequest() {
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}

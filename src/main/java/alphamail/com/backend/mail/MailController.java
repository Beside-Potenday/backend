package alphamail.com.backend.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping("/send-email")
    public String sendEmail(@RequestHeader("Authorization") String authorizationHeader,
        @RequestBody MailRequest emailRequest) {
        try {
            String accessToken = authorizationHeader.split(" ")[1];
            mailService.sendEmail(accessToken, emailRequest.getTo(), emailRequest.getFrom(), emailRequest.getSubject(), emailRequest.getBody());
            return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email.";
        }
    }
}

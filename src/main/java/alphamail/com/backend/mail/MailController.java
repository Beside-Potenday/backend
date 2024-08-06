package alphamail.com.backend.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        @RequestBody MailRequest.Send emailToSend) {
        try {
            String accessToken = authorizationHeader.split(" ")[1];
            mailService.sendEmail(accessToken, emailToSend);
            return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send email.";
        }
    }

    @PostMapping("/save-email")
    public ResponseEntity<Void> saveEmail(@RequestHeader("Authorization") String authorizationHeader,
        @RequestBody MailRequest.Save emailToSave) {
        String accessToken = authorizationHeader.split(" ")[1];
        mailService.saveEmail(accessToken, emailToSave);
        return ResponseEntity.ok().build();
    }
}

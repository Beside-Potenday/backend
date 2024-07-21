package alphamail.com.backend.Model;

public record InputMessageDTO(String sender,
                              String receiver,
                              String department,
                              String studentId,
                              String subject,
                              String content) {
}

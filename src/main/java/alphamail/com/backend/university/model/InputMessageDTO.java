package alphamail.com.backend.university.model;

public record InputMessageDTO(String sender,
                              String receiver,
                              String department,
                              String studentId,
                              String subject,
                              String content) {
}

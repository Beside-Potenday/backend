package alphamail.com.backend.university.model;

public record UniversityRequest(String sender,
                                String receiver,
                                String department,
                                String studentId,
                                String subject,
                                String content) {
}

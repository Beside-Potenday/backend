package alphamail.com.backend.business.model;

public record BusinessMailRequest(String content,
                                  String sender,
                                  String company,
                                  String department,
                                  String additional,
                                  String receiver
                                  ) {

}

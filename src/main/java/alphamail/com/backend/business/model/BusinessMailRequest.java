package alphamail.com.backend.business.model;

public record BusinessMailRequest(String sendType, BusinessSender businessSender, BusinessReceiver businessReceiver, String content) {

}

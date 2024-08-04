package alphamail.com.backend.business;

import alphamail.com.backend.business.model.BusinessMailRequest;
import alphamail.com.backend.business.model.BusinessMailResponse;
import alphamail.com.backend.common.model.ClovaRequest;
import alphamail.com.backend.common.model.ClovaResponse;
import alphamail.com.backend.common.model.ClovaTask;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BusinessService {

    @Value("${BusinessApiUrl}")
    private String businessApiUrl;

    @Value("${ApiGatewayKey}")
    private String businessApiGatewayKey;

    @Value("${clovaStudioApiKey}")
    private String clovaStudioApiKey;

    private final Random random = new Random();
    private final RestTemplate restTemplate;

    public BusinessService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BusinessMailResponse getBusinessMail(BusinessMailRequest businessMailRequest) {
        HttpHeaders httpHeaders = setHeaders();
        ClovaRequest clovaRequest = getBody(businessMailRequest);
        RequestEntity<ClovaRequest> request = new RequestEntity<>(clovaRequest, httpHeaders,
            HttpMethod.POST, URI.create(businessApiUrl));
        ResponseEntity<ClovaResponse> response = restTemplate.exchange(request,
            ClovaResponse.class);
        String emailContent = response.getBody().getResult().getMessage().getContent();
        String title = getTitle(emailContent);
        String mainText = getMainText(emailContent);
        return new BusinessMailResponse(title, mainText);
    }

    private String getMainText(String emailContent) {
        String title = emailContent.split("\n")[0];
        return emailContent.substring(title.length()+1);
    }

    private String getTitle(String emailContent) {
        return emailContent.split("\n")[0].substring(5);
    }

    private HttpHeaders setHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-NCP-CLOVASTUDIO-API-KEY", clovaStudioApiKey);
        httpHeaders.add("X-NCP-APIGW-API-KEY", businessApiGatewayKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private ClovaRequest getBody(BusinessMailRequest businessMailRequest) {
        ClovaTask systemTask = new ClovaTask("system",
            "이메일을 길게 작성해줘. !(송신자 이름)은 실제 송신자 이름을 채워줬으면 좋겠어.\n"
                + " 형식은 : '제목 : [(송신자 소속)] (제목)\n(본문 내용)'\n");
        ClovaTask userTask = new ClovaTask("user", makeUserContent(businessMailRequest));
        List<ClovaTask> tasks = new ArrayList<>();
        tasks.add(systemTask);
        tasks.add(userTask);
        return ClovaRequest.builder()
            .messages(tasks)
            .seed(random.nextInt(200_000_000))
            .build();
    }

    private String makeUserContent(BusinessMailRequest businessMailRequest) {
        StringBuilder sb = new StringBuilder();
        if(!businessMailRequest.sendType().isEmpty()) sb.append("송신 타입 : ")
            .append(businessMailRequest.sendType()).append("\n");
        if(!businessMailRequest.businessSender().name().isEmpty()) sb.append("송신자 이름 : ")
            .append(businessMailRequest.businessSender().name()).append("\n");
        if(!businessMailRequest.businessSender().level().isEmpty()) sb.append("송신자 직급 : ")
            .append(businessMailRequest.businessSender().level()).append("\n");
        if(!businessMailRequest.businessSender().company().isEmpty()) sb.append("송신자 소속 : ")
            .append(businessMailRequest.businessSender().company()).append("\n");
        if(!businessMailRequest.businessReceiver().name().isEmpty()) sb.append("수신자 이름 : ")
            .append(businessMailRequest.businessReceiver().name()).append("\n");
        if(!businessMailRequest.businessReceiver().level().isEmpty()) sb.append("수신자 직급 : ")
            .append(businessMailRequest.businessReceiver().level()).append("\n");
        if(!businessMailRequest.content().isEmpty()) sb.append("작성 내용 : ")
            .append(businessMailRequest.content()).append("\n");
        return sb.toString();
    }
}

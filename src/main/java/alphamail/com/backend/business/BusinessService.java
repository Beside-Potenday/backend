package alphamail.com.backend.business;

import alphamail.com.backend.business.model.BusinessMailRequest;
import alphamail.com.backend.business.model.BusinessMailResponse;
import alphamail.com.backend.business.model.BusinessReceiver;
import alphamail.com.backend.business.model.BusinessSender;
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
        ClovaRequest clovaRequest = getBody(businessMailRequest.sendType(),
            businessMailRequest.businessSender(), businessMailRequest.businessReceiver(),
            businessMailRequest.contents());
        RequestEntity<ClovaRequest> request = new RequestEntity<>(clovaRequest, httpHeaders,
            HttpMethod.POST, URI.create(businessApiUrl));
        ResponseEntity<ClovaResponse> response = restTemplate.exchange(request, ClovaResponse.class);
        System.out.println(response.getBody().getResult().getMessage().getContent());
        return new BusinessMailResponse(null, null);
    }

    private HttpHeaders setHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-NCP-CLOVASTUDIO-API-KEY", clovaStudioApiKey);
        httpHeaders.add("X-NCP-APIGW-API-KEY", businessApiGatewayKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private ClovaRequest getBody(String sendType, BusinessSender businessSender,
        BusinessReceiver businessReceiver, String contents) {
        ClovaTask systemTask = new ClovaTask("system", "안녕이라고 보내줘");
        ClovaTask userTask = new ClovaTask("user", "안녕");
        List<ClovaTask> tasks = new ArrayList<>();
        tasks.add(systemTask);
        tasks.add(userTask);
        return ClovaRequest.builder()
            .messages(tasks)
            .seed(random.nextInt(200_000_000))
            .build();
    }
}

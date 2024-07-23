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

    private ClovaRequest getBody(BusinessMailRequest businessMailRequest) {
        ClovaTask systemTask = new ClovaTask("system",
            "이메일을 길게 작성해줘. 형식은 : '제목 : [제목] \n\n [본문 내용]");
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
        String a = "";
        return a;
    }
}

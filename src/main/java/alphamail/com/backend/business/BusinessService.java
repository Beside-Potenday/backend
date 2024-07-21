package alphamail.com.backend.business;

import alphamail.com.backend.business.model.BusinessMailRequest;
import alphamail.com.backend.business.model.BusinessMailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @Value("${BusinessApiUrl}")
    private String businessApiUrl;

    @Value("${ApiGateWayKey}")
    private String businessApiGatewayKey;

    @Value("${clovaStudioApiKey}")
    private String clovaStudioApiKey;

    public BusinessMailResponse getBusinessMail(BusinessMailRequest businessMailRequest) {
        HttpHeaders httpHeaders = setHeaders();
        return new BusinessMailResponse(null, null);
    }

    private HttpHeaders setHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-NCP-CLOVASTUDIO-API-KEY", clovaStudioApiKey);
        httpHeaders.add("X-NCP-APIGW-API-KEY", businessApiGatewayKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}

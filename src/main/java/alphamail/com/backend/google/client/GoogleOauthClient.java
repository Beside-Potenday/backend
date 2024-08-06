package alphamail.com.backend.google.client;

import alphamail.com.backend.google.model.GoogleLoginRequest;
import alphamail.com.backend.google.model.GoogleLoginResponse;
import alphamail.com.backend.google.util.ConfigUtils;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleOauthClient {

    private final RestTemplate restTemplate;
    private final ConfigUtils configUtils;

    public GoogleOauthClient(RestTemplate restTemplate, ConfigUtils configUtils) {
        this.restTemplate = restTemplate;
        this.configUtils = configUtils;
    }

    public String googleInitUrl() {
        return configUtils.googleInitUrl();
    }

    public GoogleLoginResponse getGoogleLoginResponse(String authCode) {
        GoogleLoginRequest body = getGoogleLoginRequest(authCode);
        RequestEntity<GoogleLoginRequest> request = new RequestEntity<>(body, HttpMethod.POST, URI.create(
            configUtils.getTokenUrl()));
        ResponseEntity<GoogleLoginResponse> response = restTemplate.exchange(request, GoogleLoginResponse.class);
        return response.getBody();
    }

    public void getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(accessToken);
        headers.setBearerAuth(accessToken);
        RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(configUtils.getUserInfoUrl()));
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        System.out.println(response);
    }

    private GoogleLoginRequest getGoogleLoginRequest(String authCode) {
        GoogleLoginRequest body = GoogleLoginRequest.builder()
            .clientId(configUtils.getGoogleClientId())
            .clientSecret(configUtils.getGoogleSecret())
            .code(authCode)
            .redirectUri(configUtils.getGoogleRedirectUri())
            .grantType("authorization_code")
            .build();
        return body;
    }
}

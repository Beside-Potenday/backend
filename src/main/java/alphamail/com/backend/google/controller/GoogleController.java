package alphamail.com.backend.google.controller;

import alphamail.com.backend.google.model.TokenResponse;
import alphamail.com.backend.google.model.GoogleLoginRequest;
import alphamail.com.backend.google.model.GoogleLoginResponse;
import alphamail.com.backend.google.util.ConfigUtils;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/google")
public class GoogleController {

    private final ConfigUtils configUtils;
    private final RestTemplate restTemplate;

    GoogleController(ConfigUtils configUtils, RestTemplate restTemplate) {
        this.configUtils = configUtils;
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/login")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
            .location(URI.create(configUtils.googleInitUrl())).build();
    }

    @GetMapping(value = "/login/redirect")
    public ResponseEntity<?> redirectGoogleLogin(
        @RequestParam(value = "code") String authCode
    ) {
        // HTTP 통신을 위해 RestTemplate 활용
        GoogleLoginRequest body = GoogleLoginRequest.builder()
            .clientId(configUtils.getGoogleClientId())
            .clientSecret(configUtils.getGoogleSecret())
            .code(authCode)
            .redirectUri(configUtils.getGoogleRedirectUri())
            .grantType("authorization_code")
            .build();

        try {
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();


            RequestEntity<GoogleLoginRequest> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(
                configUtils.getTokenUrl()));

            ResponseEntity<GoogleLoginResponse> response = restTemplate.exchange(request, GoogleLoginResponse.class);

            GoogleLoginResponse googleLoginResponse = response.getBody();
            String accessToken = googleLoginResponse.getAccessToken();

            return ResponseEntity.ok(new TokenResponse(accessToken));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(null);
    }
}

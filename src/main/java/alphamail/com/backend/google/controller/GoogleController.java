package alphamail.com.backend.google.controller;

import alphamail.com.backend.google.model.GoogleLoginDTO;
import alphamail.com.backend.google.model.GoogleLoginRequest;
import alphamail.com.backend.google.model.GoogleLoginResponse;
import alphamail.com.backend.google.util.ConfigUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<GoogleLoginDTO> redirectGoogleLogin(
        @RequestParam(value = "code") String authCode
    ) {
        // HTTP 통신을 위해 RestTemplate 활용
        GoogleLoginRequest requestParams = GoogleLoginRequest.builder()
            .clientId(configUtils.getGoogleClientId())
            .clientSecret(configUtils.getGoogleSecret())
            .code(authCode)
            .redirectUri(configUtils.getGoogleRedirectUri())
            .grantType("authorization_code")
            .build();

        try {
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleLoginRequest> httpRequestEntity = new HttpEntity<>(requestParams,
                headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(
            configUtils.getTokenUrl(), httpRequestEntity, String.class);

            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(
                JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleLoginResponse googleLoginResponse = objectMapper.readValue(
                apiResponseJson.getBody(), new TypeReference<GoogleLoginResponse>() {
                });

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(
                    configUtils.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", jwtToken)
                .toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if (resultJson != null) {
                GoogleLoginDTO userInfoDto = objectMapper.readValue(resultJson,
                    new TypeReference<GoogleLoginDTO>() {
                    });

                return ResponseEntity.ok().body(userInfoDto);
            } else {
                throw new Exception("Google OAuth failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(null);
    }
}

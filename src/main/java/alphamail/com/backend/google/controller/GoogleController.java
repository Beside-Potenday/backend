package alphamail.com.backend.google.controller;

import alphamail.com.backend.google.client.GoogleOauthClient;
import alphamail.com.backend.google.model.GoogleLoginResponse;
import alphamail.com.backend.google.model.TokenResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/google")
public class GoogleController {

    private final GoogleOauthClient googleOauthClient;

    public GoogleController(GoogleOauthClient googleOauthClient) {
        this.googleOauthClient = googleOauthClient;
    }

    @GetMapping(value = "/login")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
            .location(URI.create(googleOauthClient.googleInitUrl())).build();
    }

    @GetMapping(value = "/login/redirect")
    public ResponseEntity<TokenResponse> redirectGoogleLogin(
        @RequestParam(value = "code") String authCode
    ) {
        GoogleLoginResponse googleLoginResponse = googleOauthClient.getGoogleLoginResponse(
            authCode);
        String accessToken = googleLoginResponse.getAccessToken();
        googleOauthClient.getUserInfo(accessToken);
        return ResponseEntity.ok(TokenResponse.builder().accessToken(accessToken).build());
    }
}

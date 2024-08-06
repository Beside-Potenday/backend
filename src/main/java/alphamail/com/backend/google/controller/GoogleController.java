package alphamail.com.backend.google.controller;

import alphamail.com.backend.google.client.GoogleOauthClient;
import alphamail.com.backend.google.model.GoogleLoginResponse;
import alphamail.com.backend.google.model.GoogleUserInfoResponse;
import alphamail.com.backend.google.model.TokenResponse;
import alphamail.com.backend.user.service.MemberService;
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
    private final MemberService memberService;

    public GoogleController(GoogleOauthClient googleOauthClient, MemberService memberService) {
        this.googleOauthClient = googleOauthClient;
        this.memberService = memberService;
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
        GoogleUserInfoResponse googleUserInfoResponse = googleOauthClient.getUserInfo(accessToken);
        memberService.createMember(googleLoginResponse, googleUserInfoResponse);
        return ResponseEntity.ok(TokenResponse.builder()
            .accessToken(accessToken)
            .email(googleUserInfoResponse.email())
            .name(googleUserInfoResponse.name())
            .picture(googleUserInfoResponse.picture())
            .build());
    }
}

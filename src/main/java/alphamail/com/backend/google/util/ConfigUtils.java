package alphamail.com.backend.google.util;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConfigUtils {
    @Getter
    @Value("${google.auth.url}")
    private String googleAuthUrl;

    @Getter
    @Value("${google.login.url}")
    private String googleLoginUrl;

    @Getter
    @Value("${google.redirect.uri}")
    private String googleRedirectUri;

    @Getter
    @Value("${google.client.id}")
    private String googleClientId;

    @Getter
    @Value("${google.secret}")
    private String googleSecret;

    @Value("${google.auth.scope}")
    private String scopes;

    @Getter
    @Value("${google.auth.user-info}")
    private String userInfoUrl;
    // Google 로그인 URL 생성 로직
    public String googleInitUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getGoogleClientId());
        params.put("redirect_uri", getGoogleRedirectUri());
        params.put("response_type", "code");
        params.put("scope", getScopeUrl());
        params.put("access_type", "offline");

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        return getGoogleLoginUrl()
                + "/o/oauth2/v2/auth"
                + "?"
                + paramStr;
    }

    // scope의 값을 보내기 위해 띄어쓰기 값을 UTF-8로 변환하는 로직 포함
    public String getScopeUrl() {
        return scopes.replaceAll(",", "%20");
    }

    public String getTokenUrl() {
        return getGoogleAuthUrl() + "/token";
    }

}

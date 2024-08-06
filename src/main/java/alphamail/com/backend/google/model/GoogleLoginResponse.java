package alphamail.com.backend.google.model;

// 일회성 토큰을 통해 얻은 Response VO
// idToken을 전달해 사용자 정보를 얻을 예정

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleLoginResponse {

    @JsonProperty("access_token")
    private String accessToken; // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    @JsonProperty("expires_in")
    private Integer expiresIn;   // Access Token의 남은 수명
    @JsonProperty("refresh_token")
    private String refreshToken;    // 새 액세스 토큰을 얻는 데 사용할 수 있는 토큰
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;   // 반환된 토큰 유형(Bearer 고정)
    @JsonProperty("id_token")
    private String idToken;
}

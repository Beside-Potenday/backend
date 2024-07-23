package alphamail.com.backend.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenResponse {

    private String idToken;
    private String accessToken;
}

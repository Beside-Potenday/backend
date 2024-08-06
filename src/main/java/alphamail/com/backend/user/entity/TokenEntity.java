package alphamail.com.backend.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "token")
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "expires_in", nullable = false)
    private LocalDateTime expiresIn;

    public TokenEntity() {}

    public TokenEntity(MemberEntity memberEntity, String refreshToken, String accessToken,
        Integer expiresIn) {
        this.memberEntity = memberEntity;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.expiresIn = LocalDateTime.now().plusSeconds(expiresIn);
    }
}

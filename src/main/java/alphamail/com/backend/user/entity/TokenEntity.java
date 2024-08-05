package alphamail.com.backend.user.entity;

import jakarta.persistence.*;
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
    @Column(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "id_token", nullable = false)
    private String idToken;

    public TokenEntity() {}

    public TokenEntity(MemberEntity memberEntity, String refreshToken, String idToken){
        this.memberEntity = memberEntity;
        this.refreshToken = refreshToken;
        this.idToken = idToken;
    }
}

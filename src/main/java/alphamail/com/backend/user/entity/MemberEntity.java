package alphamail.com.backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profileUrl", nullable = false)
    private String profileUrl;

    public MemberEntity(){}

    public MemberEntity(String name, String email, String profileUrl){
        this.name = name;
        this.email = email;
        this.profileUrl = profileUrl;
    }
}

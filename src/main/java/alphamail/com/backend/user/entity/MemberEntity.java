package alphamail.com.backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class memberEntity {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String profileUrl;

    public memberEntity(){}

    public memberEntity(String name, String email, String profileUrl){
        this.name = name;
        this.email = email;
        this.profileUrl = profileUrl;
    }
}

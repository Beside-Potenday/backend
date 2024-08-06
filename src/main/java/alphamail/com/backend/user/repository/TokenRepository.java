package alphamail.com.backend.user.repository;

import alphamail.com.backend.user.entity.MemberEntity;
import alphamail.com.backend.user.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByAccessToken(String accessToken);
    TokenEntity findByMemberEntity(MemberEntity memberEntity);
}

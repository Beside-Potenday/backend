package alphamail.com.backend.user.repository;

import alphamail.com.backend.user.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
}

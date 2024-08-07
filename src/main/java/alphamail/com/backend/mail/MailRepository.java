package alphamail.com.backend.mail;

import alphamail.com.backend.mail.model.MailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<MailEntity, Long> {

    Page<MailEntity> findAllByMemberEntityIdAndType(Long id, String type, Pageable pageable);
}

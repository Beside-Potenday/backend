package alphamail.com.backend.mail;

import alphamail.com.backend.mail.model.MailEntity;
import alphamail.com.backend.user.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<MailEntity, Long> {

    Page<MailEntity> findAllByMember(MemberEntity member);
}

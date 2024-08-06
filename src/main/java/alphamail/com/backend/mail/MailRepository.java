package alphamail.com.backend.mail;

import alphamail.com.backend.mail.model.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<MailEntity, Long> {

}

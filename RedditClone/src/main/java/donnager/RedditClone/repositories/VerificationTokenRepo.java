package donnager.RedditClone.repositories;

import donnager.RedditClone.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
}

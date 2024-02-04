package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.UserToken;
import lt.javinukai.javinukai.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, UUID> {

    Optional<UserToken> findByTokenValueAndType(String tokenValue, TokenType type);
}

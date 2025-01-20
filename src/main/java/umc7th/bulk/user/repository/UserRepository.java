package umc7th.bulk.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByKakaoId(String kakaoId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findById(Long id);
}

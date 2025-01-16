package umc7th.bulk.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.user.domain.User;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByKakaoId(String kakaoId);
    boolean existsByEmail(String email);
}

package umc7th.bulk.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByKakaoId(String kakaoId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findById(Long userId);
    List<User> findByGroupGroupIdAndRecordCompleteTrue(Long groupId); // 해당 그룹 id 에서 오늘 기록 달성한 사용자 리스트
    Optional<User> findByKakaoId(String kakaoId);
    Optional<User> findByEmail(String email);
}

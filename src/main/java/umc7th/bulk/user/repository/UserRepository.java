package umc7th.bulk.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import umc7th.bulk.group.entity.Group;
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

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.recordComplete = false")
    int resetRecordComplete();

    // 특정 그룹 기록 달성한 인원 수 조회 (오늘 기록 완료한 인원 수)
    @Query("SELECT COUNT(u) FROM User u WHERE u.group = :group AND u.recordComplete = true")
    int countByGroupAndRecordCompleteTrue(@Param("group") Group group);

    // 특정 그룹 총 인원 수 조회
    @Query("SELECT COUNT(u) FROM User u WHERE u.group = :group")
    int countByGroup(@Param("group") Group group);





}

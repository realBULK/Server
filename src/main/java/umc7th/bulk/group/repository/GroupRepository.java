package umc7th.bulk.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc7th.bulk.group.entity.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    // 그룹 인원 수가 10 이하인 그룹 찾는 메서드, 없으면 새로 생성
    @Query("SELECT g FROM Group g WHERE SIZE(g.members) < 10 ORDER BY g.groupId ASC")
    Optional<Group> findGroupWithSpace();
}

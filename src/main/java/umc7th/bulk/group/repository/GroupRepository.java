package umc7th.bulk.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.group.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
}

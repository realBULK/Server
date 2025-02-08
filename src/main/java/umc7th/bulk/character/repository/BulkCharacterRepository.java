package umc7th.bulk.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc7th.bulk.character.entity.BulkCharacter;

@Repository
public interface BulkCharacterRepository extends JpaRepository<BulkCharacter, Long> {

}

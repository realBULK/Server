package umc7th.bulk.recordedFood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.recordedFood.entity.RecordedFood;

public interface RecordedFoodRepository extends JpaRepository<RecordedFood, Long> {
}

package umc7th.bulk.dailyMeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc7th.bulk.dailyMeal.entity.DailyMeal;

public interface DailyMealRepository extends JpaRepository<DailyMeal, Long> {
}

package umc7th.bulk.dailyMeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc7th.bulk.dailyMeal.entity.DailyMeal;

import java.util.Optional;

public interface DailyMealRepository extends JpaRepository<DailyMeal, Long> {

    @Query("""
        SELECT dm FROM DailyMeal dm
        JOIN FETCH dm.mealPlan mp
        JOIN FETCH mp.user u
        WHERE dm.id = :dailyMealId
    """)
    Optional<DailyMeal> findByIdWithMealPlanAndUser(@Param("dailyMealId") Long dailyMealId);
}

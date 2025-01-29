package umc7th.bulk.mealPlan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc7th.bulk.mealPlan.entity.MealPlan;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

//    @Query("SELECT mp FROM MealPlan mp WHERE mp.user.id = :userId ORDER BY mp.createdAt DESC")
    MealPlan findByUserId(Long userId);

//    List<MealPlan> findByIdWithDailyMeals(Long mealPlanId);
}

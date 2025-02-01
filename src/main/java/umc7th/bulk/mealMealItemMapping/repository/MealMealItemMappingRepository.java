package umc7th.bulk.mealMealItemMapping.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealMealItemMapping.entity.MealMealItemMapping;

import java.time.LocalDate;
import java.util.List;

public interface MealMealItemMappingRepository extends JpaRepository<MealMealItemMapping, Long> {

    // 특정 끼니 해당하는 매핑 리스트
    List<MealMealItemMapping> findByMealId(Long mealId);

    @Query("SELECT mmim FROM MealMealItemMapping mmim " +
            "WHERE mmim.meal.dailyMeal.mealPlan.user.id = :userId " +
            "AND mmim.meal.type = :type " +
            "AND (:cursorId IS NULL OR :cursorId = 0 OR mmim.mealItem.id >= :cursorId) " +
            "ORDER BY mmim.mealItem.id ASC ")
    Slice<MealMealItemMapping> findMealItemsByUserAndMealWithCursor(
            @Param("userId") Long userId,
            @Param("type") MealType type,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("SELECT m FROM MealMealItemMapping m " +
            "JOIN FETCH m.meal meal " +
            "WHERE meal.localDate = :date AND meal.type = :mealType")
    List<MealMealItemMapping> findByMeal_LocalDateAndMeal_Type(
            @Param("date") LocalDate date,
            @Param("mealType") MealType mealType
    );

}

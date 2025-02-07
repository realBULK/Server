package umc7th.bulk.mealItem.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealItem.entity.MealItem;

import java.util.Optional;

public interface MealItemRepository extends JpaRepository<MealItem, Long> {

    @Query("SELECT mi FROM MealItem mi " +
            "JOIN FETCH mi.mealMealItemMappings mmim " +
            "JOIN FETCH mmim.meal m " +
            "JOIN FETCH m.dailyMeal dm " +
            "JOIN FETCH dm.mealPlan mp " +
            "WHERE mp.user.id = :userId " +
            "AND m.type = :type " +
            "AND (:cursorId IS NULL OR mi.id < :cursorId)")
    Slice<MealItem> findMealItemsByUserAndMealWithCursor(
            @Param("userId") Long userId,
            @Param("type") MealType type,
            @Param("cursorId") Long cursorId,
            Pageable pageable);

//    @Query("SELECT mi FROM MealItem mi WHERE mi.name = :name")
    Optional<MealItem> findByName(String name);

    Boolean existsByName(String name);

    void deleteLastByName(String name);

}

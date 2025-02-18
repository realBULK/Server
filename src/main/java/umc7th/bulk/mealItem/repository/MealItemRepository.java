package umc7th.bulk.mealItem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.mealItem.entity.MealItem;

import java.util.List;
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

    // 최신순 정렬
    @Query("SELECT m FROM MealItem m WHERE m.id < :cursor ORDER BY m.id DESC")
    Page<MealItem> findByCursor(Long cursor, Pageable pageable);

    // 검색어 기반
    @Query("SELECT m FROM MealItem m WHERE m.name LIKE %:keyword% AND m.id < :cursor ORDER BY m.id DESC")
    Page<MealItem> searchByKeyword(String keyword, Long cursor, Pageable pageable);

    // 음식 이름으로 조회
    Optional<MealItem> findByName(String name);

    // 상위 5개 음식 조회
    @Query("SELECT mi FROM MealItem mi ORDER BY mi.recordCount DESC")
    List<MealItem> findTop5MealItemByRecordCount(Pageable pageable);

}

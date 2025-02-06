package umc7th.bulk.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.record.entity.Record;
import umc7th.bulk.user.domain.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findByUserAndDateAndMealType(User user, LocalDate date, MealType mealType);

    @Query("SELECT r FROM Record r LEFT JOIN FETCH r.foods WHERE r.user = :user AND r.date = :date AND r.mealType = :mealType")
    Optional<Record> findByUserAndDateAndMealTypeWithFoods(@Param("user") User user, @Param("date") LocalDate date, @Param("mealType") MealType mealType);

}

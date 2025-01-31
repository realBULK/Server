package umc7th.bulk.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc7th.bulk.meal.entity.MealType;
import umc7th.bulk.record.entity.Record;
import umc7th.bulk.user.domain.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findByUserAndDateAndMealType(User user, LocalDate date, MealType mealType);
}
